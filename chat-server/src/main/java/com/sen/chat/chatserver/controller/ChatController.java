package com.sen.chat.chatserver.controller;

import com.sen.chat.chatserver.dto.req.ChatMessagePageReq;
import com.sen.chat.chatserver.dto.req.ChatMessageReq;
import com.sen.chat.chatserver.dto.resp.ChatFileMessageResp;
import com.sen.chat.chatserver.dto.resp.ChatMessageResp;
import com.sen.chat.chatserver.dto.resp.CursorPageBaseResp;
import com.sen.chat.chatserver.service.ChatService;
import com.sen.chat.common.api.SenCommonResponse;
import com.sen.chat.common.interceptor.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 15:26
 */
@RestController
@RequestMapping("/chat")
@Api(tags = "ChatController", description = "聊天接口")
@Slf4j
public class ChatController {

    @Resource
    private ChatService chatService;


    @GetMapping("/msg/page")
    @ApiOperation("消息列表")
//    @FrequencyControl(time = 120, count = 20, target = FrequencyControl.Target.IP)
    public SenCommonResponse<CursorPageBaseResp<ChatMessageResp>> getMsgPage(@Valid @RequestBody ChatMessagePageReq request) {
        CursorPageBaseResp<ChatMessageResp> msgPage = chatService.getMsgPage(request, RequestHolder.get().getUid());
//        filterBlackMsg(msgPage);
        return SenCommonResponse.success(msgPage);
    }


    @PostMapping("/msg")
    @ApiOperation("发送消息")
//    @FrequencyControl(time = 5, count = 3, target = FrequencyControl.Target.UID)
//    @FrequencyControl(time = 30, count = 5, target = FrequencyControl.Target.UID)
//    @FrequencyControl(time = 60, count = 10, target = FrequencyControl.Target.UID)
    public SenCommonResponse<ChatMessageResp> sendMsg(@Valid @RequestBody ChatMessageReq request) {
        Long msgId = chatService.sendMsg(request, RequestHolder.get().getUid());
        //返回完整消息格式，方便前端展示
        return SenCommonResponse.success(chatService.getMsgResp(msgId, RequestHolder.get().getUid()));
    }

    @PostMapping("/sendFile")
    @ApiOperation("发送小文件(不需要分片上传)")
    public SenCommonResponse<ChatMessageResp> sendFile(@RequestParam(value = "roomId", required = false) Long roomId,
                                                       @RequestParam(value = "msgType", required = false) Integer msgType,
                                                       MultipartFile file,
                                                       @RequestParam(value = "fileName") String fileName,
                                                       @RequestParam(value = "md5") String md5) {
        Long msgId = chatService.sendFile(roomId, msgType, file, fileName, md5);
        //返回完整消息格式，方便前端展示
        return SenCommonResponse.success(chatService.getMsgResp(msgId, RequestHolder.get().getUid()));
    }


    @PostMapping("/sendBigFile")
    @ApiOperation("发送大文件(需要分片上传)")
    public SenCommonResponse<ChatFileMessageResp> sendFile(@RequestParam(value = "fileId", required = false) String fileId,
                                                           @RequestParam(value = "roomId", required = false) Long roomId,
                                                           @RequestParam(value = "msgType", required = false) Integer msgType,
                                                           MultipartFile file,
                                                           @RequestParam(value = "fileName") String fileName,
                                                           @RequestParam(value = "md5") String md5,
                                                           @RequestParam(value = "chunkIndex") Integer chunkIndex,
                                                           @RequestParam(value = "chunks") Integer chunks) {

        return SenCommonResponse.success(chatService.sendBigFile(fileId, roomId, msgType, file, fileName, md5, chunkIndex, chunks));
    }

    @PostMapping("/test")
    @ApiOperation("测试")
    public SenCommonResponse<ChatMessageResp> test(@RequestParam(value = "msgId") Long msgId) {

//        Long msgId = chatService.sendMsg();
//        //返回完整消息格式，方便前端展示
        return SenCommonResponse.success(chatService.getMsgResp(msgId, RequestHolder.get().getUid()));
    }

}
