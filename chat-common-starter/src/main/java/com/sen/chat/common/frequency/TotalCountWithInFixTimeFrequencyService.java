package com.sen.chat.common.frequency;

import com.sen.chat.common.domain.FrequencyControlDTO;
import com.sen.chat.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.sen.chat.common.frequency.FrequencyControlStrategyFactory.TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/30 17:23
 */
@Slf4j
@Service
public class TotalCountWithInFixTimeFrequencyService extends AbstractFrequencyControlService<FrequencyControlDTO> {

    /**
     * 是否达到限流阈值 子类实现 每个子类都可以自定义自己的限流逻辑判断
     *
     * @param frequencyControlMap 定义的注解频控 Map中的Key-对应redis的单个频控的Key Map中的Value-对应redis的单个频控的Key限制的Value
     * @return true-方法被限流 false-方法没有被限流
     */
    @Override
    protected boolean reachRateLimit(Map<String, FrequencyControlDTO> frequencyControlMap) {
        //批量获取redis统计的值
        List<String> frequencyKeys = new ArrayList<>(frequencyControlMap.keySet());
        List<Integer> countList = RedisUtils.mget(frequencyKeys, Integer.class);
        for (int i = 0; i < frequencyKeys.size(); i++) {
            String key = frequencyKeys.get(i);
            Integer count = countList.get(i);
            int frequencyControlCount = frequencyControlMap.get(key).getCount();
            if (Objects.nonNull(count) && count >= frequencyControlCount) {
                //频率超过了
                log.warn("frequencyControl limit key:{},count:{}", key, count);
                return true;
            }
        }
        return false;
    }

    /**
     * 增加限流统计次数 子类实现 每个子类都可以自定义自己的限流统计信息增加的逻辑
     *
     * @param frequencyControlMap 定义的注解频控 Map中的Key-对应redis的单个频控的Key Map中的Value-对应redis的单个频控的Key限制的Value
     */
    @Override
    protected void addFrequencyControlStatisticsCount(Map<String, FrequencyControlDTO> frequencyControlMap) {
        frequencyControlMap.forEach((k, v) -> RedisUtils.inc(k, v.getTime(), v.getUnit()));
    }

    @Override
    protected String getStrategyName() {
        return TOTAL_COUNT_WITH_IN_FIX_TIME_FREQUENCY_CONTROLLER;
    }
}
