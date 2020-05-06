package com.dmg.bairenlonghu.common.enums;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:22 2019/10/24
 */
public enum TableEnum {

    TABLE_LONG(1, "龙", 1, true),
    TABLE_HE(2, "和", 8, false),
    TABLE_HU(3, "虎", 1, true);

    //下标
    private int tableIndex;
    //名称
    private String tableName;
    //倍率
    private int tableMang;
    //是否发牌
    private Boolean isLicensing;

    TableEnum(int tableIndex, String tableName, int tableMang, Boolean isLicensing) {
        this.tableIndex = tableIndex;
        this.tableName = tableName;
        this.tableMang = tableMang;
        this.isLicensing = isLicensing;
    }

    public static TableEnum getTableEnum(int tableIndex){
        for (TableEnum tableEnum: TableEnum.values()) {
            if(tableIndex == tableEnum.getTableIndex()){
                return tableEnum;
            }
        }
        return null;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTableMang() {
        return tableMang;
    }

    public void setTableMang(int tableMang) {
        this.tableMang = tableMang;
    }

    public Boolean getLicensing() {
        return isLicensing;
    }

    public void setLicensing(Boolean licensing) {
        isLicensing = licensing;
    }
}
