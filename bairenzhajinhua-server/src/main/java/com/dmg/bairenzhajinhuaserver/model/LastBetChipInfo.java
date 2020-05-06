package com.dmg.bairenzhajinhuaserver.model;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/5 17:45
 * @Version V1.0
 **/

public class LastBetChipInfo {
    /**
     * 上次下注桌子序号
    */
    private String tableIndex;
    /**
     * 上次下注额
     */
    private BigDecimal betChip;

    public String getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(String tableIndex) {
        this.tableIndex = tableIndex;
    }

    public BigDecimal getBetChip() {
        return betChip;
    }

    public void setBetChip(BigDecimal betChip) {
        this.betChip = betChip;
    }
}