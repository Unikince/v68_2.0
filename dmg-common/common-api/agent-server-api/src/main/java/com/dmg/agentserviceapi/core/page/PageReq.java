package com.dmg.agentserviceapi.core.page;

import lombok.Data;

/**
 * 分页请求
 */
@Data
public class PageReq {
    /** 页数 */
    private Long current = 1L;
    /** 条数 */
    private Long size = 30L;

    /**
     * 是否错误
     */
    public static boolean isError(PageReq page) {
        if (page.current <= 0 || page.size <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 构建最大分页对象
     */
    public static PageReq maxPage() {
        PageReq result = new PageReq();
        result.setCurrent(1L);
        result.setSize(1000000000000000L);
        return result;
    }
}
