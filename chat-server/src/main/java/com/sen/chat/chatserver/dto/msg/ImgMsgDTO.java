package com.sen.chat.chatserver.dto.msg;

import com.sen.chat.chatserver.dto.msg.BaseFileDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/2 21:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ImgMsgDTO extends BaseFileDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("宽度（像素）")
    @NotNull
    private Integer width;

    @ApiModelProperty("高度（像素）")
    @NotNull
    private Integer height;

}
