package com.dmg.lobbyserver.model.constants;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
public enum GamePromotionType {
    XIMA_PROMOTION(1,"洗码优惠"),
    SIGN_PROMOTION(2,"签到奖金"),
    JIFEN_PROMOTION(3,"积分兑换"),
    CARU_PROMOTION(4,"首存优惠"),
    ;
    private int key;
    private String value;
    GamePromotionType(int key, String value) {
        this.key = key;
        this.value = value;
    }
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
