package com.sen.chat.chatserver.component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 使用basemapper方法插入修改时填充时间
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/26 0:27
 */
@Slf4j
@Component
public class FillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        this.setFieldValByName("createTime", now, metaObject);
        this.setFieldValByName("updateTime", now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", Timestamp.valueOf(LocalDateTime.now()), metaObject);
    }
}
