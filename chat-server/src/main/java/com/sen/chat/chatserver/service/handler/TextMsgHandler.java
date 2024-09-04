package com.sen.chat.chatserver.service.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.sen.chat.chatserver.cache.UserInfoCache;
import com.sen.chat.chatserver.dao.MessageDAO;
import com.sen.chat.chatserver.dto.msg.TextMsgReq;
import com.sen.chat.chatserver.dto.msg.TextMsgResp;
import com.sen.chat.chatserver.entity.MessageDO;
import com.sen.chat.chatserver.entity.UserInfoDO;
import com.sen.chat.chatserver.entity.message.MessageExtra;
import com.sen.chat.chatserver.entity.message.UrlInfo;
import com.sen.chat.chatserver.utils.CommonUrlDiscover;
import com.sen.chat.common.constant.dict.FlagEnum;
import com.sen.chat.common.constant.dict.MessageStatusEnum;
import com.sen.chat.common.constant.dict.MessageTypeEnum;
import com.sen.chat.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description: 普通文本消息
 * @author: sensen
 * @date: 2023/6/31 0:34
 */
@Component
@AllArgsConstructor
public class TextMsgHandler extends AbstractMsgHandler<TextMsgReq> {

    private MessageDAO messageDao;

    private UserInfoCache userInfoCache;

    private static final CommonUrlDiscover URL_TITLE_DISCOVER = new CommonUrlDiscover();

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    protected void checkMsg(TextMsgReq body, Long roomId, Long uid) {
        //校验下回复消息
        if (Objects.nonNull(body.getReplyMsgId())) {
            MessageDO replyMsg = messageDao.selectById(body.getReplyMsgId());
            if (Objects.isNull(replyMsg)) {
                throw new BusinessException("回复消息不存在");
            }
            if (!Objects.equals(replyMsg.getRoomId(), roomId)) {
                throw new BusinessException("只能回复相同会话内的消息");
            }
        }
        //艾特的人
        if (CollectionUtils.isNotEmpty(body.getAtUidList())) {
            //前端传入的@用户列表可能会重复，需要去重
            List<Long> atUidList = body.getAtUidList().stream().distinct().collect(Collectors.toList());
            Map<Long, UserInfoDO> batch = userInfoCache.getUserInfoBatch(atUidList);
            //如果@用户不存在，userInfoCache 返回的map中依然存在该key，但是value为null，需要过滤掉再校验
            long batchCount = batch.values().stream().filter(Objects::nonNull).count();
            if (!Objects.equals(atUidList.size(), batchCount)) {
                throw new BusinessException("@用户不存在");
            }
            boolean atAll = body.getAtUidList().contains(0L);
            //todo 检查管理员权限
        }
    }

    @Override
    public void saveMsg(MessageDO msg, TextMsgReq body) {//插入文本内容
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        MessageDO update = new MessageDO();
        update.setId(msg.getId());
        update.setContent(body.getContent());
        update.setExtra(extra);
        //如果有回复消息
        if (Objects.nonNull(body.getReplyMsgId())) {
            Integer gapCount = messageDao.getGapCount(msg.getRoomId(), body.getReplyMsgId(), msg.getId());
            update.setGapCount(gapCount);
            update.setReplyMsgId(body.getReplyMsgId());
        }
        //判断消息url跳转
        Map<String, UrlInfo> urlContentMap = URL_TITLE_DISCOVER.getUrlContentMap(body.getContent());
        extra.setUrlContentMap(urlContentMap);
        //艾特功能
        if (CollectionUtil.isNotEmpty(body.getAtUidList())) {
            extra.setAtUidList(body.getAtUidList());
        }

        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(MessageDO msg) {
        TextMsgResp resp = new TextMsgResp();
        resp.setContent(msg.getContent());
        resp.setUrlContentMap(Optional.ofNullable(msg.getExtra()).map(MessageExtra::getUrlContentMap).orElse(null));
        resp.setAtUidList(Optional.ofNullable(msg.getExtra()).map(MessageExtra::getAtUidList).orElse(null));
        //回复消息
        Optional<MessageDO> reply = Optional.ofNullable(msg.getReplyMsgId())
                .map(messageDao::selectById)
                .filter(a -> Objects.equals(a.getStatus(), MessageStatusEnum.NORMAL.getCode()));
        if (reply.isPresent()) {
            MessageDO replyMessage = reply.get();
            TextMsgResp.ReplyMsg replyMsgVO = new TextMsgResp.ReplyMsg();
            replyMsgVO.setId(replyMessage.getId());
            replyMsgVO.setUid(replyMessage.getFromUid());
            replyMsgVO.setType(replyMessage.getType());
            replyMsgVO.setBody(MsgHandlerFactory.getStrategyNoNull(replyMessage.getType()).showReplyMsg(replyMessage));
            UserInfoDO replyUser = userInfoCache.getUserInfo(replyMessage.getFromUid());
            replyMsgVO.setUsername(replyUser.getUserName());
            replyMsgVO.setCanCallback(FlagEnum.toStatus(Objects.nonNull(msg.getGapCount()) && msg.getGapCount() <= MessageAdapter.CAN_CALLBACK_GAP_COUNT));
            replyMsgVO.setGapCount(msg.getGapCount());
            resp.setReply(replyMsgVO);
        }
        return resp;
    }

    @Override
    public Object showReplyMsg(MessageDO msg) {
        return msg.getContent();
    }

    @Override
    public String showContactMsg(MessageDO msg) {
        return msg.getContent();
    }
}
