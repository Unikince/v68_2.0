package com.dmg.gameconfigserver.model.vo.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:54 2020/3/16
 */
@Data
public class GameOnlineDetailVO {
    /**
     * 总在线人数
     */
    private Integer countOnlineNum = 0;
    /**
     * 明细
     */
    private List<Detail> details = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        /**
         * 游戏id
         */
        private Integer gameId;
        /**
         * 游戏名称
         */
        private String gameName;
        /**
         * 在线人数
         */
        private Integer onlineNum;
    }
}
