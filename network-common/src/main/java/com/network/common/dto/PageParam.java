package com.network.common.dto;

import lombok.Data;

@Data
public class PageParam {

    private long page = 1;
    private long pageSize = 10;

}
