package com.dmg.dataserver.business.model;

import java.util.List;

import lombok.Data;

@Data
public class AllGameInfoDTO {
    /**
     * 开放游戏列表
     */
    private List<String> openGameList;
    /**
     * 所有游戏列表
     */
    private List<String> allGameList;
}
