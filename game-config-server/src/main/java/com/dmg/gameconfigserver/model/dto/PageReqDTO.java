package com.dmg.gameconfigserver.model.dto;

import lombok.Data;

@Data
public class PageReqDTO {
    /** 页数 */
    private Integer current = 1;
    /** 条数 */
    private Integer size = 30;
}
