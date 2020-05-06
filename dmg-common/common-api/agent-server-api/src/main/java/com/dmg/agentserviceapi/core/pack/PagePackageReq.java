package com.dmg.agentserviceapi.core.pack;

import com.dmg.agentserviceapi.core.page.PageReq;

import lombok.Data;

/**
 * 分页请求封包
 */
@Data
public class PagePackageReq<T> {
    /** 分页数据 */
    private PageReq page = new PageReq();
    /** 参数 */
    private T params;
}
