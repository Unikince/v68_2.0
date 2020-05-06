package com.dmg.agentserviceapi.core.pack;

import com.dmg.agentserviceapi.core.page.PageRes;

import lombok.Data;

/**
 * 分页和和额外数据返回封包
 */
@Data
public class PageAndExtraPackageRes<T, E> {
    /** 分页数据 */
    private PageRes page;
    /** 数据 */
    private T data;
    /** 额外数据 */
    private E extra;
}
