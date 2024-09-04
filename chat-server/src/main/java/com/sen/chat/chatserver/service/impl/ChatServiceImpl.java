package com.sen.chat.chatserver.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sen.chat.chatserver.cache.UserOnlineCache;
import com.sen.chat.chatserver.constant.RedisConstant;
import com.sen.chat.chatserver.dao.*;
import com.sen.chat.chatserver.dto.msg.BaseFileDTO;
import com.sen.chat.chatserver.dto.req.ChatMessagePageReq;
import com.sen.chat.chatserver.dto.req.ChatMessageReq;
import com.sen.chat.chatserver.dto.req.CursorPageBaseReq;
import com.sen.chat.chatserver.dto.resp.ChatFileMessageResp;
import com.sen.chat.chatserver.dto.resp.ChatMemberStatisticResp;
import com.sen.chat.chatserver.dto.resp.ChatMessageResp;
import com.sen.chat.chatserver.dto.resp.CursorPageBaseResp;
import com.sen.chat.chatserver.dto.vo.UploadFileResp;
import com.sen.chat.chatserver.entity.*;
import com.sen.chat.chatserver.entity.query.MessageMarkQuery;
import com.sen.chat.chatserver.event.MessageSendEvent;
import com.sen.chat.chatserver.service.ChatService;
import com.sen.chat.chatserver.service.FileService;
import com.sen.chat.chatserver.service.handler.AbstractMsgHandler;
import com.sen.chat.chatserver.service.handler.MessageAdapter;
import com.sen.chat.chatserver.service.handler.MsgHandlerFactory;
import com.sen.chat.chatserver.utils.MinioUtil;
import com.sen.chat.common.constant.dict.*;
import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;
import com.sen.chat.common.constant.errorcode.ResultCodeEnum;
import com.sen.chat.common.domain.RequestInfo;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.interceptor.RequestHolder;
import com.sen.chat.common.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: sensen
 * @date: 2023/7/30 15:27
 */
@Slf4j
@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private RoomDAO roomDAO;

    private ContactDAO contactDAO;

    private MessageDAO messageDAO;

    private MessageMarkDAO messageMarkDAO;

    private UserOnlineCache userOnlineCache;

    private FriendDAO friendDAO;

    private GroupDAO groupDAO;

    private GroupMemberDAO groupMemberDAO;

    private ApplicationEventPublisher applicationEventPublisher;

    private FileService fileService;

    private MinioUtil minioUtil;

    private TransactionTemplate transactionTemplate;

    private RedisService redisService;

    @Override
    public CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq request, @Nullable Long receiveUid) {
        //用最后一条消息id，来限制被踢出的人能看见的最近一条消息
        Long lastMsgId = getLastMsgId(request.getRoomId(), receiveUid);
        CursorPageBaseResp<MessageDO> cursorPage = getCursorPageByMysql(messageDAO, request, wrapper -> {
            wrapper.eq(MessageDO::getRoomId, request.getRoomId());
            wrapper.eq(MessageDO::getStatus, MessageStatusEnum.NORMAL.getCode());
            wrapper.le(Objects.nonNull(lastMsgId), MessageDO::getId, lastMsgId);
        }, MessageDO::getId);
        if (cursorPage.isEmpty()) {
            return CursorPageBaseResp.empty();
        }
        return CursorPageBaseResp.init(cursorPage, getMsgRespBatch(cursorPage.getList(), receiveUid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendMsg(ChatMessageReq request, Long uid) {

        //检查消息
        checkMessage(request, uid);

        //根据消息类型选择对应的消息处理器
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(request.getMsgType());
        Long msgId = msgHandler.checkAndSaveMsg(request, uid);

        //发布消息发送事件，异步推送消息给前端
        applicationEventPublisher.publishEvent(new MessageSendEvent(this, msgId));
        return msgId;
    }

    @Override
    public ChatMessageResp getMsgResp(Long msgId, Long uid) {
        MessageDO msg = messageDAO.selectById(msgId);
        return getMsgResp(msg, uid);
    }

    @Override
    public ChatMessageResp getMsgResp(MessageDO message, Long receiveUid) {
        return CollUtil.getFirst(getMsgRespBatch(Collections.singletonList(message), receiveUid));
    }

    @Override
    public ChatMemberStatisticResp getMemberStatistic() {
        Long onlineNum = userOnlineCache.getOnlineNum();
//        Long offlineNum = userCache.getOfflineNum();不展示总人数
        ChatMemberStatisticResp resp = new ChatMemberStatisticResp();
        resp.setOnlineNum(onlineNum);
//        resp.setTotalNum(onlineNum + offlineNum);
        return resp;
    }

    @Override
    public Long sendFile(Long roomId, Integer msgType, MultipartFile file, String fileName, String md5) {
        RequestInfo requestInfo = RequestHolder.get();
        if (Objects.isNull(requestInfo)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setRoomId(roomId);
        chatMessageReq.setMsgType(msgType);
        BaseFileDTO fileDTO = MessageAdapter.buildFileMsgReq(msgType, file);
        if (Objects.isNull(fileDTO)) {
            throw new BusinessException("不支持该消息类型");
        }
        chatMessageReq.setBody(fileDTO);
        fileDTO.setSize(file.getSize());
        fileDTO.setFileName(fileName);
        fileDTO.setMd5(md5);

        //先上传文件
        String fileId = UUID.randomUUID().toString();
        String fileSuffix = fileService.getFileSuffix(fileName);
        String minioFileName = fileId + fileSuffix;
        minioUtil.upload(file, minioFileName);
        fileDTO.setFileId(fileId);

        //上传之后再发布事件
        fileDTO.setUrl(minioUtil.getFileUrl(minioFileName));

        return transactionTemplate.execute(status -> sendMsg(chatMessageReq, requestInfo.getUid()));
    }

    @Override
    public ChatFileMessageResp sendBigFile(String fileId, Long roomId, Integer msgType, MultipartFile file,
                                           String fileName, String md5, Integer chunkIndex, Integer chunks) {
        RequestInfo requestInfo = RequestHolder.get();
        if (Objects.isNull(requestInfo)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }
        Long uid = requestInfo.getUid();
        if (StringUtils.isEmpty(fileId)) {
            fileId = UUID.randomUUID().toString();
        }
        UploadFileResp uploadFileResp = fileService.uploadFile(fileId, file, fileName, md5, chunkIndex, chunks);
        String redisKey = RedisConstant.TEMP_FILE_SIZE + fileId;
        Long currentSize = (Long) redisService.get(redisKey);
        if (Objects.isNull(currentSize)) {
            currentSize = 0L;
        }
        ChatFileMessageResp chatFileMessageResp = new ChatFileMessageResp();
        if (StringUtils.equals(UploadingStatusEnum.UPLOADING.getCode(), uploadFileResp.getUploadingStatus())) {
            //上传中
            redisService.set(redisKey, file.getSize() + currentSize, RedisConstant.TEM_MINUTE);

            //返回给前端当前上传状态
            chatFileMessageResp.setUploadFileResp(uploadFileResp);
        } else if (StringUtils.equals(UploadingStatusEnum.UPLOAD_FINISH.getCode(), uploadFileResp.getUploadingStatus())) {
            //上传完成
            ChatMessageReq chatMessageReq = new ChatMessageReq();
            chatMessageReq.setRoomId(roomId);
            chatMessageReq.setMsgType(msgType);
            BaseFileDTO fileDTO = MessageAdapter.buildFileMsgReq(msgType, file);
            if (Objects.isNull(fileDTO)) {
                throw new BusinessException("不支持该消息类型");
            }
            chatMessageReq.setBody(fileDTO);
            fileDTO.setSize(currentSize + file.getSize());
            fileDTO.setFileName(fileName);
            fileDTO.setMd5(md5);
            fileDTO.setFileId(fileId);
            String fileSuffix = fileService.getFileSuffix(fileName);
            String minioFileName = fileId + fileSuffix;
            fileDTO.setUrl(minioUtil.getFileUrl(minioFileName));
            //上传完成，发送文件消息
            Long msgId = transactionTemplate.execute(status -> sendMsg(chatMessageReq, uid));
            ChatMessageResp msgResp = getMsgResp(msgId, uid);
            chatFileMessageResp.setFromUser(msgResp.getFromUser());
            chatFileMessageResp.setMessage(msgResp.getMessage());
            chatFileMessageResp.setUploadFileResp(uploadFileResp);
        }
        return chatFileMessageResp;
    }


    /**
     * 检查消息是否可发送，好友关系和群组关系是否正常
     *
     * @param request 消息体
     * @param uid     发送人UID
     */
    private void checkMessage(ChatMessageReq request, Long uid) {
        RoomDO room = roomDAO.selectByRoomId(request.getRoomId());
        if (Objects.isNull(room)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }
        if (Objects.equals(RoomTypeEnum.FRIEND.getCode(), room.getRoomType())) {
            FriendDO friendDO = friendDAO.selectByRoomId(uid, request.getRoomId());
            if (Objects.isNull(friendDO)) {
                throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
            }
            if (Objects.equals(FriendStatusEnum.BE_BLACKED.getCode(), friendDO.getStatus())) {
                throw new BusinessException(BaseErrorCodeEnum.BE_BLACKED);
            }
            if (Objects.equals(FriendStatusEnum.BE_DELETED.getCode(), friendDO.getStatus())) {
                throw new BusinessException(BaseErrorCodeEnum.BE_DELETED);
            }
            if (Objects.equals(FriendStatusEnum.DELETE_FRIEND.getCode(), friendDO.getStatus())) {
                throw new BusinessException(BaseErrorCodeEnum.DELETE_FRIEND);
            }
            if (Objects.equals(FriendStatusEnum.NOT_FRIEND.getCode(), friendDO.getStatus())) {
                throw new BusinessException(BaseErrorCodeEnum.NOT_FRIEND);
            }
        }
        if (Objects.equals(RoomTypeEnum.GROUP.getCode(), room.getRoomType())) {
            RoomGroupDO groupDO = groupDAO.selectByRoomId(request.getRoomId());
            GroupUserRelationDO groupMember = groupMemberDAO.selectByPrimaryKey(groupDO.getGroupId(), uid);
            if (Objects.isNull(groupMember)) {
                throw new BusinessException(BaseErrorCodeEnum.BE_GROUP_REMOVED);
            }
        }

    }

    public <T> CursorPageBaseResp<T> getCursorPageByMysql(BaseMapper<T> mapper, CursorPageBaseReq request, Consumer<LambdaQueryWrapper<T>> initWrapper, SFunction<T, ?> cursorColumn) {
        //游标字段类型
        Class<?> cursorType = getReturnType(cursorColumn);
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        //额外条件
        initWrapper.accept(wrapper);
        //游标条件
        if (StrUtil.isNotBlank(request.getCursor())) {
            wrapper.lt(cursorColumn, parseCursor(request.getCursor(), cursorType));
        }
        //游标方向
        wrapper.orderByDesc(cursorColumn);

        Page<T> page = mapper.selectPage(request.plusPage(), wrapper);
        //取出游标
        String cursor = Optional.ofNullable(CollectionUtil.getLast(page.getRecords()))
                .map(cursorColumn)
                .map(this::toCursor)
                .orElse(null);
        //判断是否最后一页
        Boolean isLast = page.getRecords().size() != request.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
    }

    private String toCursor(Object o) {
        if (o instanceof Date) {
            return String.valueOf(((Date) o).getTime());
        } else {
            return o.toString();
        }
    }

    private Object parseCursor(String cursor, Class<?> cursorClass) {
        if (Date.class.isAssignableFrom(cursorClass)) {
            return new Date(Long.parseLong(cursor));
        } else {
            return cursor;
        }
    }

    @SneakyThrows
    public <T> Class<?> getReturnType(SFunction<T, ?> func) {
        LambdaMeta lambda = LambdaUtils.extract(func);
        Class<?> aClass = lambda.getInstantiatedClass();
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        Field field = aClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getType();
    }


    /**
     * 构造查询会话消息响应内容
     * 在基础信息基础上查询消息标志表补充消息内容
     * 然后根据消息类型选择不同的处理
     *
     * @param messages   消息
     * @param receiveUid 接收人UID
     * @return 会话消息响应内容
     */
    public List<ChatMessageResp> getMsgRespBatch(List<MessageDO> messages, Long receiveUid) {
        //没有收到消息
        if (CollectionUtil.isEmpty(messages)) {
            return new ArrayList<>();
        }

        //查询消息标志
        List<Long> msgIdList = messages.stream().map(MessageDO::getId).collect(Collectors.toList());
        MessageMarkQuery query = MessageMarkQuery.builder()
                .msgIds(msgIdList)
                .status(MessageMarkStatusEnum.NORMAL.getCode()).build();
        List<MessageMarkDO> msgMark = messageMarkDAO.selectList(query);

        //构造完整的消息响应内容
        return MessageAdapter.buildMsgResp(messages, msgMark, receiveUid);
    }

    /**
     * 获取聊天室中最近的消息ID
     *
     * @param roomId     聊天室ID
     * @param receiveUid 接收人UID
     * @return 聊天室中最近的消息ID
     */
    private Long getLastMsgId(Long roomId, Long receiveUid) {
        if (Objects.isNull(receiveUid)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        RoomDO room = roomDAO.selectByRoomId(roomId);
        if (Objects.isNull(room)) {
            throw new BusinessException(BaseErrorCodeEnum.ROOM_NOT_EXIST);
        }
        ContactDO contact = contactDAO.selectByPrimaryKey(receiveUid, roomId);
        if (Objects.isNull(contact)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }
        return contact.getLastMsgId();
    }
}
