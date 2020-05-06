package com.dmg.lobbyserver.model.constants;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
public enum GameWithdrawalType {
    AUDIT_WITHDRAWAL(1, "审核中"),
    DURING_PAYMENT_WITHDRAWAL(2, "支付中"),
    HAVE_PAID_WITHDRAWAL(3, "已支付"),
    AUTOMATIC_CANCELLATION_WITHDRAWAL(4, "自动取消"),
    CANCELED_WITHDRAWAL(5, "已取消"),
    SYSTEM_EXCEPTION_WITHDRAWAL(6, "系统异常"),
    ;
    private int key;
    private String value;

    GameWithdrawalType(int key, String value) {
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
