package com.sen.chat.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 20:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SlidingWindowDTO extends FrequencyControlDTO {
    /**
     * 窗口大小，默认 10 s
     */
    private int windowSize;

    /**
     * 窗口最小周期 1s (窗口大小是 10s， 1s一个小格子，-共10个格子)
     */
    private int period;
}
