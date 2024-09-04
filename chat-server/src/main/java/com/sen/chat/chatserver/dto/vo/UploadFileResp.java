package com.sen.chat.chatserver.dto.vo;

import lombok.Data;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/2 11:38
 */
@Data
public class UploadFileResp {

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 上传状态
     */
    private String uploadingStatus;

}
