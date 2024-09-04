package com.sen.chat.chatserver.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sen.chat.chatserver.dto.msg.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 14:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageExtra implements Serializable {
    private static final long serialVersionUID = 1L;

    //url跳转链接
    private Map<String, UrlInfo> urlContentMap;

    //消息撤回详情
    private MsgRecall recall;

    //艾特的uid
    private List<Long> atUidList;

    //文件消息
    private FileMsgDTO fileMsg;

    //图片消息
    private ImgMsgDTO imgMsgDTO;

    //语音消息
    private SoundMsgDTO soundMsgDTO;

    //文件消息
    private VideoMsgDTO videoMsgDTO;

    //表情消息
    private EmojisMsgDTO emojisMsgDTO;
}
