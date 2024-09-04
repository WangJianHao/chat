package com.sen.chat.chatserver.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sen.chat.common.api.SenCommonPage;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 14:15
 */
public class PageUtils {


    public static <T> SenCommonPage<T> restPage(IPage<T> page) {
        SenCommonPage<T> result = new SenCommonPage<>();
        result.setTotalPage(page.getPages());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords());
        return result;
    }
}
