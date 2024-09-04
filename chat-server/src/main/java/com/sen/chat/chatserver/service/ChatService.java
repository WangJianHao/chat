package com.sen.chat.chatserver.service;

import com.sen.chat.chatserver.dto.req.ChatMessagePageReq;
import com.sen.chat.chatserver.dto.req.ChatMessageReq;
import com.sen.chat.chatserver.dto.resp.ChatFileMessageResp;
import com.sen.chat.chatserver.dto.resp.ChatMemberStatisticResp;
import com.sen.chat.chatserver.dto.resp.ChatMessageResp;
import com.sen.chat.chatserver.dto.resp.CursorPageBaseResp;
import com.sen.chat.chatserver.entity.MessageDO;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 15:26
 */
public interface ChatService {

    /**
     * 获取消息列表
     *
     * @param request    获取消息列表请求
     * @param receiveUid 接收人UID
     * @return 当前页的消息
     */
    CursorPageBaseResp<ChatMessageResp> getMsgPage(ChatMessagePageReq request, @Nullable Long receiveUid);

    /**
     * 发送消息
     *
     * @param request 通用消息请求体
     * @param uid     发送人UID
     * @return 保存到数据库中的消息id
     */
    Long sendMsg(ChatMessageReq request, Long uid);

    /**
     * 获取消息发送成功返回体
     *
     * @param msgId 消息ID
     * @param receiveUid   接收消息
     * @return 消息发送成功后的返回体
     */
    ChatMessageResp getMsgResp(Long msgId, Long receiveUid);

    /**
     * 根据消息获取消息前端展示的物料
     *
     * @param message
     * @param receiveUid 接受消息的uid，可null
     * @return
     */
    ChatMessageResp getMsgResp(MessageDO message, Long receiveUid);

    /**
     * 查询在线人数
     *
     * @return
     */
    ChatMemberStatisticResp getMemberStatistic();

    /**
     * 发送小文件
     *
     * @param roomId
     * @param msgType
     * @param file
     * @param fileName
     * @param md5
     * @return
     */
    Long sendFile(Long roomId, Integer msgType, MultipartFile file, String fileName, String md5);

    /**
     * 发送大文件
     *
     * @param fileId
     * @param roomId
     * @param msgType
     * @param file
     * @param fileName
     * @param md5
     * @param chunkIndex
     * @param chunks
     * @return
     */
    ChatFileMessageResp sendBigFile(String fileId, Long roomId, Integer msgType, MultipartFile file,
                                    String fileName, String md5, Integer chunkIndex, Integer chunks);

}
