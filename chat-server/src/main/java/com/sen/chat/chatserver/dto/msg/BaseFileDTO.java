package com.sen.chat.chatserver.dto.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/2 21:20
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("大小（字节）")
    @NotNull
    private Long size;

    @ApiModelProperty("下载地址")
    @NotBlank
    private String url;

    @ApiModelProperty("文件实际名称")
    @NotBlank
    private String fileName;

    @ApiModelProperty("存储在minio中的id")
    @NotBlank
    private String fileId;

    @ApiModelProperty("文件MD5值")
    @NotBlank
    private String md5;
}
