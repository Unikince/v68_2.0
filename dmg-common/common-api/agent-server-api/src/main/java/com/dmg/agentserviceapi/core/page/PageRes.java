package com.dmg.agentserviceapi.core.page;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PageRes extends PageReq {
    /** 总行数 */
    private int total;
    /** 总页数 */
    private Integer pages = 1;
}
