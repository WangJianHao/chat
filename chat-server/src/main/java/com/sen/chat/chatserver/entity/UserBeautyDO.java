package com.sen.chat.chatserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author sensen
 * @Description
 * @date 2024-08-27
 */

@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "fieldHandler"}, ignoreUnknown = true)
@TableName(value = "tusr_userbeauty")
public class UserBeautyDO implements Serializable {
    private static final long serialVersionUID = 5114248101497773985L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 靓号UID
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 0-未使用 1-已使用
     */
    @TableField(value = "status")
    private Integer status;

}
