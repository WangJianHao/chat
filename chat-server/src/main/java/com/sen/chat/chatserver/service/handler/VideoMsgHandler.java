package com.sen.chat.chatserver.service.handler;

import com.sen.chat.chatserver.dao.MessageDAO;
import com.sen.chat.chatserver.dto.msg.VideoMsgDTO;
import com.sen.chat.chatserver.entity.MessageDO;
import com.sen.chat.chatserver.entity.message.MessageExtra;
import com.sen.chat.common.constant.dict.MessageTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/2 21:51
 */
@Component
@AllArgsConstructor
public class VideoMsgHandler extends AbstractMsgHandler<VideoMsgDTO>{

    private MessageDAO messageDAO;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.VIDEO;
    }

    @Override
    public void saveMsg(MessageDO msg, VideoMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        MessageDO update = new MessageDO();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setVideoMsgDTO(body);
        messageDAO.updateById(update);
    }

    @Override
    public Object showMsg(MessageDO msg) {
        return msg.getExtra().getVideoMsgDTO();
    }

    @Override
    public Object showReplyMsg(MessageDO msg) {
        return "视频";
    }

    @Override
    public String showContactMsg(MessageDO msg) {
        return "[视频]";
    }
}
