package com.sen.chat.chatserver.service.handler;

import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;
import com.sen.chat.common.exception.BusinessException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/30 21:51
 */
public class MsgHandlerFactory {

    /**
     * 对不同的消息类型采用不同的处理类
     */
    private static final Map<Integer, AbstractMsgHandler<?>> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler<?> strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    /**
     * 根据消息类型获取对应的handler类
     *
     * @param type 消息类型
     * @return 消息类型对应的处理类
     */
    public static AbstractMsgHandler<?> getStrategyNoNull(Integer type) {
        AbstractMsgHandler<?> strategy = STRATEGY_MAP.get(type);
        if (Objects.isNull(strategy)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }
        return strategy;
    }
}
