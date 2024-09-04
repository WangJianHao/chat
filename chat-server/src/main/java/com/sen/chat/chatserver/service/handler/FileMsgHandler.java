package com.sen.chat.chatserver.service.handler;

import com.sen.chat.chatserver.dao.MessageDAO;
import com.sen.chat.chatserver.dto.msg.FileMsgDTO;
import com.sen.chat.chatserver.entity.MessageDO;
import com.sen.chat.chatserver.entity.message.MessageExtra;
import com.sen.chat.common.constant.dict.MessageTypeEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/2 21:32
 */
@Component
@AllArgsConstructor
public class FileMsgHandler extends AbstractMsgHandler<FileMsgDTO> {

    private MessageDAO messageDAO;

    @Override
    MessageTypeEnum getMsgTypeEnum() {
        return MessageTypeEnum.FILE;
    }

    @Override
    public void saveMsg(MessageDO msg, FileMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(msg.getExtra()).orElse(new MessageExtra());
        MessageDO update = new MessageDO();
        update.setId(msg.getId());
        update.setExtra(extra);
        extra.setFileMsg(body);
        messageDAO.updateById(update);
    }

    @Override
    public Object showMsg(MessageDO msg) {
        return msg.getExtra().getFileMsg();
    }

    @Override
    public Object showReplyMsg(MessageDO msg) {
        return "文件:" + msg.getExtra().getFileMsg().getFileName();
    }

    @Override
    public String showContactMsg(MessageDO msg) {
        return "[文件]" + msg.getExtra().getFileMsg().getFileName();
    }
}
