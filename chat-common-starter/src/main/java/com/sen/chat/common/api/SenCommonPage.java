package com.sen.chat.common.api;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 统一分页处理
 *
 * @author sensen
 * @date 2021-01-01
 */
public class SenCommonPage<T> {

    /**
     * 页号
     */
    private Long current;

    /**
     * 每页数据的数量
     */
    private Long size;

    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 总数
     */
    private Long total;

    /**
     * 当前页的数据
     */
    private List<T> records;

    public static <T> SenCommonPage<T> restPage(Page<T> page) {
        SenCommonPage<T> result = new SenCommonPage<>();
        result.setTotalPage((long) page.getTotalPages());
        result.setCurrent(page.getNumber());
        result.setSize(page.getSize());
        result.setTotal(page.getTotalElements());
        result.setRecords(page.getContent());
        return result;
    }

    public List<T> getRecords() {
        return this.records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrent() {
        return this.current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }


}
