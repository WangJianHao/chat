package com.sen.chat.common.service;

import com.sen.chat.common.constant.IDHashKeyEnum;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import static com.sen.chat.common.utils.DateUtil.parsePatterns;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/27 12:47
 */
@Slf4j
@Component
public class CommonIDGenerator implements InitializingBean {

    /**
     * 开始时间戳
     */
    private static final long BEGIN_TIMESTAMP = 927993600L;

    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS = 3;

    /**
     * Redis Key
     */
    private static final String ID_KEY = "sen:common:id";


    @Autowired
    private RedisService redisService;

    public Long nextId(String hashKey) {
//        return getRandomNumber(Constant.LENGTH_11);
        return nextId(hashKey, 1L);
    }

    private String getRandomNumber(Integer count) {
        return RandomStringUtils.random(count, false, true);
    }


    // offset表示的是id的递增梯度值
    public Long nextId(String hashKey, Long offset) {
        try {
            if (null == offset) {
                offset = 1L;
            }
            // 生成唯一id
            return redisService.increment(ID_KEY, hashKey, offset);
        } catch (Exception e) {
            log.error("redis服务异常", e);
            throw new BusinessException("redis服务连接异常");
        }
    }

    public Long nextIdForTime(String hashKey, Long offset) {
        try {
            if (null == offset) {
                offset = 1L;
            }
            // 1.生成时间戳
            LocalDateTime now = LocalDateTime.now();
            //	得到当前的秒数
            long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
            long timestamp = nowSecond - BEGIN_TIMESTAMP;

            //2.自增长
            long count = redisService.increment(ID_KEY, hashKey, offset);

            // 3.拼接并返回
            return timestamp << COUNT_BITS | count;
        } catch (Exception e) {
            log.error("redis服务异常", e);
            throw new BusinessException("redis服务连接异常");
        }
    }

    public Long nextIdForTimeWithDate(String hashKey, Long offset) {
        try {
            if (null == offset) {
                offset = 1L;
            }
            // 1.生成时间戳
            LocalDateTime now = LocalDateTime.now();
            //	得到当前的秒数
            long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
            long timestamp = nowSecond - BEGIN_TIMESTAMP;

            // 2.生成序列号
            // 2.1.获取当前日期，精确到天
            String date = DateUtil.formatTime(now, parsePatterns[12]);
            // 2.2.自增长 （每天一个key）
            long count = redisService.increment(ID_KEY + date, hashKey, offset);

            // 3.拼接并返回
            return timestamp << COUNT_BITS | count;
        } catch (Exception e) {
            log.error("redis服务异常", e);
            throw new BusinessException("redis服务连接异常");
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化
        Arrays.stream(IDHashKeyEnum.values()).forEach(idHashKeyEnum -> {
            //如果不存在就初始化ID序列
            if (!redisService.hHasKey(ID_KEY, idHashKeyEnum.getHashKey())) {
                redisService.hSet(ID_KEY, idHashKeyEnum.getHashKey(), idHashKeyEnum.getBound());
            }
        });
    }
}
