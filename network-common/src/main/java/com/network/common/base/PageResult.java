package com.network.common.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long page;
    private long pageSize;
    private long total;
    private long pages;
    private List<T> records;

    public static <T> PageResult<T> empty() {
        PageResult<T> result = new PageResult<>();
        result.page = 1;
        result.pageSize = 10;
        result.total = 0;
        result.pages = 0;
        result.records = Collections.emptyList();
        return result;
    }

    public static <T> PageResult<T> of(long page, long pageSize, long total, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.page = page;
        result.pageSize = pageSize;
        result.total = total;
        result.pages = (total + pageSize - 1) / pageSize;
        result.records = records;
        return result;
    }

}
