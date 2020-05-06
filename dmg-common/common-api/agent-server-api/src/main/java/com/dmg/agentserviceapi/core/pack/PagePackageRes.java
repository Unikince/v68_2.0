package com.dmg.agentserviceapi.core.pack;

import com.dmg.agentserviceapi.core.page.PageRes;

import lombok.Data;

/**
 * 分页返回封包
 */
@Data
public class PagePackageRes<T> {
    /** 分页数据 */
    private PageRes page;
    /** 数据 */
    private T data;
}
