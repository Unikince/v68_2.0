package com.dmg.server.common.enums;

/**
 * @Author liubo
 * @Description //TODO 物品类型
 * @Date 18:09 2019/11/26
 */
public enum ItemTypeEnum {

    CODE_PATRONAGE(0, "谢谢惠顾"),
    CODE_GOLD(1, "金币"),
    CODE_INTEGRAL(2, "积分"),
    PROMO_CODE(4,"优惠码"),
    CODE_ACTIVITY_LEVEL(6, "活跃度");

    private Integer code;

    private String msg;

    ItemTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
