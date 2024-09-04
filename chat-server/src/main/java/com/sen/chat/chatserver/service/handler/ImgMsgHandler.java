package com.sen.chat.chatserver.service.handler;

import com.sen.chat.chatserver.dao.MessageDAO;
import com.sen.chat.chatserver.dto.msg.ImgMsgDTO;
import com.sen.chat.chatserver.entity.MessageDO;
import com.sen.chat.chatserver.entity.message.MessageExtra;
import com.sen.chat.common.constant.dict.MessageTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 图片消息
 *
 * @description: 图片消息
 * @author: sensen
 * @date: 2024/9/2 21:19
 */
@Component
@AllArgsConstructor
public class ImgMsgHandler extends AbstractMsgHandler<ImgMsgDTO> {

    private MessageDAO messageDAO;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.IMG;
    }

    @Override
    public void saveMsg(MessageDO msg, ImgMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        MessageDO update = new MessageDO();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setImgMsgDTO(body);
        messageDAO.updateById(update);
    }

    @Override
    public Object showMsg(MessageDO msg) {
        return msg.getExtra().getImgMsgDTO();
    }

    @Override
    public Object showReplyMsg(MessageDO msg) {
        return "图片";
    }

    @Override
    public String showContactMsg(MessageDO msg) {
        return "[图片]";
    }
}
