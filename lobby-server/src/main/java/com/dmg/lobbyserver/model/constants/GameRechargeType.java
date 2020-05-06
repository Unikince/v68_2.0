package com.dmg.lobbyserver.model.constants;

/**
 * @Description 
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
public enum  GameRechargeType {
    ONLINE_RECHARGE(1,"在线充值"),
    ALIPAY_RECHARGE(2,"支付宝充值"),
    WECHAT_RECHARGE(1,"微信充值"),
    MANUALLY_RECHARGE(1,"手动充值"),
                  ;
    private int key;
    private String value;
    GameRechargeType(int key, String value) {
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
