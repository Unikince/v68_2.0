package com.dmg.server.common.enums;

/**
 * @author ASUS
 * 点控自控模型
 */
public enum GameOddsEnum {

    a(1, 0.70, false),
    b(2, 0.80, false),
    c(3, 0.90, false),
    d(4, 0.95, true),
    e(5, 1.00, true),
    f(6, 1.10, true),
    g(7, 1.20, true);

    //图标id
    private int iconType;
    //当前胜率
    private double odds;
    //模型控制输赢
    private Boolean isWin;

    GameOddsEnum(int iconType, double odds, Boolean isWin) {
        this.iconType = iconType;
        this.odds = odds;
        this.isWin = isWin;
    }

    public static Boolean getIsWinByIcon(int iconType) {
        for (GameOddsEnum gameOddsEnum : GameOddsEnum.values()) {
            if (gameOddsEnum.getIconType() == iconType) {
                return gameOddsEnum.isWin;
            }
        }
        return false;
    }

    public double getOdds() {
        return odds;
    }

    public void setOdds(double odds) {
        this.odds = odds;
    }

    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    public static int getIdByOdds(double odds) {
        for (GameOddsEnum e : values()) {
            if (e.getOdds() == odds) {
                return e.getIconType();
            }
        }
        return 0;
    }

    public Boolean getWin() {
        return isWin;
    }

    public void setWin(Boolean win) {
        isWin = win;
    }
}
