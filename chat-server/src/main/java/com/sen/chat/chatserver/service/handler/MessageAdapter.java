package com.sen.chat.chatserver.service.handler;

import cn.hutool.core.bean.BeanUtil;
import com.sen.chat.chatserver.dto.msg.*;
import com.sen.chat.chatserver.dto.req.ChatMessageReq;
import com.sen.chat.chatserver.dto.resp.ChatMessageResp;
import com.sen.chat.chatserver.entity.MessageDO;
import com.sen.chat.chatserver.entity.MessageMarkDO;
import com.sen.chat.common.constant.dict.FlagEnum;
import com.sen.chat.common.constant.dict.MessageMarkTypeEnum;
import com.sen.chat.common.constant.dict.MessageStatusEnum;
import com.sen.chat.common.constant.dict.MessageTypeEnum;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 消息适配器
 * @author: sensen
 * @date: 2023/6/30 20:12
 */
@Slf4j
public class MessageAdapter {

    public static final int CAN_CALLBACK_GAP_COUNT = 100;

    /**
     * 创建保存到数据库的消息体
     *
     * @param request 发送消息的请求体
     * @param uid     发送人uid
     * @return 消息体数据库记录
     */
    public static MessageDO buildMsgSave(ChatMessageReq request, Long uid) {

        return MessageDO.builder()
                .fromUid(uid)
                .roomId(request.getRoomId())
                .type(request.getMsgType())
                .status(MessageStatusEnum.NORMAL.getCode())
                .build();

    }

    public static List<ChatMessageResp> buildMsgResp(List<MessageDO> messages, List<MessageMarkDO> msgMark, Long receiveUid) {
        Map<Long, List<MessageMarkDO>> markMap = msgMark.stream().collect(Collectors.groupingBy(MessageMarkDO::getMsgId));
        return messages.stream().map(a -> {
                    ChatMessageResp resp = new ChatMessageResp();
                    resp.setFromUser(buildFromUser(a.getFromUid()));
                    resp.setMessage(buildMessage(a, markMap.getOrDefault(a.getId(), new ArrayList<>()), receiveUid));
                    return resp;
                })
                .sorted(Comparator.comparing(a -> a.getMessage().getSendTime()))//帮前端排好序，更方便它展示
                .collect(Collectors.toList());
    }

    public static ChatMessageReq buildAgreeMsg(Long roomId) {
        ChatMessageReq chatMessageReq = new ChatMessageReq();
        chatMessageReq.setRoomId(roomId);
        chatMessageReq.setMsgType(MessageTypeEnum.TEXT.getCode());
        TextMsgReq textMsgReq = new TextMsgReq();
        textMsgReq.setContent("我们已经成为好友了，开始聊天吧");
        chatMessageReq.setBody(textMsgReq);
        return chatMessageReq;
    }

    private static ChatMessageResp.Message buildMessage(MessageDO message, List<MessageMarkDO> marks, Long receiveUid) {
        ChatMessageResp.Message messageVO = new ChatMessageResp.Message();
        BeanUtil.copyProperties(message, messageVO);
        messageVO.setSendTime(DateUtil.formatTime(message.getCreateTime()));
        AbstractMsgHandler<?> msgHandler = MsgHandlerFactory.getStrategyNoNull(message.getType());
        if (Objects.nonNull(msgHandler)) {
            messageVO.setBody(msgHandler.showMsg(message));
        }
        //消息标记
        messageVO.setMessageMark(buildMsgMark(marks, receiveUid));
        return messageVO;
    }

    private static ChatMessageResp.MessageMark buildMsgMark(List<MessageMarkDO> marks, Long receiveUid) {
        Map<Integer, List<MessageMarkDO>> typeMap = marks.stream().collect(Collectors.groupingBy(MessageMarkDO::getType));
        List<MessageMarkDO> likeMarks = typeMap.getOrDefault(MessageMarkTypeEnum.LIKE.getCode(), new ArrayList<>());
        List<MessageMarkDO> dislikeMarks = typeMap.getOrDefault(MessageMarkTypeEnum.DISLIKE.getCode(), new ArrayList<>());
        ChatMessageResp.MessageMark mark = new ChatMessageResp.MessageMark();
        mark.setLikeCount(likeMarks.size());
        mark.setUserLike(Optional.ofNullable(receiveUid)
                .filter(uid -> likeMarks.stream().anyMatch(a -> Objects.equals(a.getUid(), uid)))
                .map(a -> FlagEnum.YES.getCode())
                .orElse(FlagEnum.NO.getCode()));
        mark.setDislikeCount(dislikeMarks.size());
        mark.setUserDislike(Optional.ofNullable(receiveUid)
                .filter(uid -> dislikeMarks.stream().anyMatch(a -> Objects.equals(a.getUid(), uid)))
                .map(a -> FlagEnum.YES.getCode())
                .orElse(FlagEnum.NO.getCode()));
        return mark;
    }

    private static ChatMessageResp.UserInfo buildFromUser(Long fromUid) {
        ChatMessageResp.UserInfo userInfo = new ChatMessageResp.UserInfo();
        userInfo.setUid(fromUid);
        return userInfo;
    }


    public static BaseFileDTO buildFileMsgReq(Integer msgType, MultipartFile file) {
        MessageTypeEnum messageType = MessageTypeEnum.of(msgType);
        try {
            switch (messageType) {
                case IMG:
                    BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                    return new ImgMsgDTO(bufferedImage.getWidth(), bufferedImage.getHeight());
                case VIDEO:
                    return new VideoMsgDTO();
                case SOUND:
                    return new SoundMsgDTO();
                case FILE:
                    return new FileMsgDTO();
            }
        } catch (Exception e) {
            log.error("生成对应消息体出错", e);
            throw new BusinessException("不支持该消息类型");
        }

        return null;
    }
}
