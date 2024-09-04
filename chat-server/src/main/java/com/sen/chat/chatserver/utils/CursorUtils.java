package com.sen.chat.chatserver.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sen.chat.chatserver.dto.req.CursorPageBaseReq;
import com.sen.chat.chatserver.dto.resp.CursorPageBaseResp;
import com.sen.chat.common.service.RedisService;
import lombok.SneakyThrows;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.data.redis.core.ZSetOperations;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 游标分页工具类
 * @author: sensen
 * @date: 2024/9/3 14:15
 */
public class CursorUtils {

    public static <T> CursorPageBaseResp<Pair<T, Double>> getCursorPageByRedis(RedisService redisService, CursorPageBaseReq cursorPageBaseReq, String redisKey, Function<String, T> typeConvert) {
        Set<ZSetOperations.TypedTuple<Object>> typedTuples;
        if (StrUtil.isBlank(cursorPageBaseReq.getCursor())) {//第一次
            typedTuples = redisService.zReverseRangeByScoreWithScores(redisKey, Double.MAX_VALUE, cursorPageBaseReq.getPageSize());
        } else {
            typedTuples = redisService.zReverseRangeByScoreWithScores(redisKey, Double.parseDouble(cursorPageBaseReq.getCursor()), cursorPageBaseReq.getPageSize());
        }
        List<Pair<T, Double>> result = typedTuples
                .stream()
                .map(t -> Pair.of(typeConvert.apply((String) t.getValue()), t.getScore()))
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .collect(Collectors.toList());
        String cursor = Optional.ofNullable(CollectionUtil.getLast(result))
                .map(Pair::getValue)
                .map(String::valueOf)
                .orElse(null);
        Boolean isLast = result.size() != cursorPageBaseReq.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, result);
    }

    public static <T> CursorPageBaseResp<T> getCursorPageByMysql(IService<T> mapper, CursorPageBaseReq request, Consumer<LambdaQueryWrapper<T>> initWrapper, SFunction<T, ?> cursorColumn) {
        //游标字段类型
        Class<?> cursorType = getReturnType(cursorColumn);//字段类型 DO:getId就是long类型
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>();
        //额外条件
        initWrapper.accept(wrapper);
        //游标条件
        if (StrUtil.isNotBlank(request.getCursor())) {
            wrapper.lt(cursorColumn, parseCursor(request.getCursor(), cursorType));
        }
        //游标方向
        wrapper.orderByDesc(cursorColumn);

        Page<T> page = mapper.page(request.plusPage(), wrapper);
        //取出游标
        String cursor = Optional.ofNullable(CollectionUtil.getLast(page.getRecords()))
                .map(cursorColumn)
                .map(CursorUtils::toCursor)
                .orElse(null);
        //判断是否最后一页
        Boolean isLast = page.getRecords().size() != request.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
    }

    @SneakyThrows
    public static <T> Class<?> getReturnType(SFunction<T, ?> func) {
        LambdaMeta lambda = LambdaUtils.extract(func);
        Class<?> aClass = lambda.getInstantiatedClass();
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        Field field = aClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getType();
    }

    private static String toCursor(Object o) {
        if (o instanceof Date) {
            return String.valueOf(((Date) o).getTime());
        } else {
            return o.toString();
        }
    }

    private static Object parseCursor(String cursor, Class<?> cursorClass) {
        if (Date.class.isAssignableFrom(cursorClass)) {
            return new Date(Long.parseLong(cursor));
        } else {
            return cursor;
        }
    }
}
