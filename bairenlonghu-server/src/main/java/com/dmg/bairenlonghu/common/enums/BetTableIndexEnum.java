package com.dmg.bairenlonghu.common.enums;


/**
 * 下注类型映射
 */
public enum BetTableIndexEnum {

    TABLE_LONG(TableEnum.TABLE_LONG.getTableIndex()),
    TABLE_HE(TableEnum.TABLE_HE.getTableIndex()),
    TABLE_HU(TableEnum.TABLE_HU.getTableIndex());

    //台座id
    private int tableIndex;

    BetTableIndexEnum(int tableIndex) {
        this.tableIndex = tableIndex;
    }


    public int getTableIndex() {
        return tableIndex;
    }

}
