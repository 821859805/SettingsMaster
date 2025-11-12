package com.k8smaster.common;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PagedResult<T> {

    private List<T> items = Collections.emptyList();

    private long total;

    private int page;

    private int pageSize;

    public PagedResult() {
    }

    public PagedResult(List<T> items, long total, int page, int pageSize) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }
}
