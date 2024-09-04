package com.sen.chat.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 20:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FixedWindowDTO extends FrequencyControlDTO {

    /**
     * 频控时间范围，默认单位秒
     */
    private Integer time;
}
