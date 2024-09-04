package com.sen.chat.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/26 0:46
 */
@Retention(RetentionPolicy.RUNTIME)//运行时生效
@Target(ElementType.METHOD)//作用在方法上
public @interface RedissonLock {

    /**
     * key的前缀,默认取方法全限定名，除非我们在不同方法上对同一个资源做分布式锁，就自己指定
     *
     * @return key的前缀
     */
    String prefixKey() default "";

    /**
     * springEl 表达式
     *
     * @return 表达式
     * eg : @RedissonLock(key = "#idempotent", waitTime = 5000)//相同幂等如果同时发奖，需要排队等上一个执行完，取出之前数据返回
     * public void doAcquireItem(Long uid, Long itemId, String idempotent)
     * <p>
     * eg:  @RedissonLock(key = "#request.roomId")
     * public void addMember(Long uid, MemberAddReq request)
     */
    String key();

    /**
     * 等待锁的时间，默认-1，不等待直接失败,redisson默认也是-1
     *
     * @return 单位秒
     */
    int waitTime() default -1;

    /**
     * 等待锁的时间单位，默认毫秒
     *
     * @return 单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

}
