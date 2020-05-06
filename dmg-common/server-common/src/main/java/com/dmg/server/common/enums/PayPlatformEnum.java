package com.dmg.server.common.enums;

/**
 * @description:
 * @return
 * @author mice
 * @date 2019/12/24
*/
public enum PayPlatformEnum {
    VNNN(1, "VNNN","VNNN平台"),
    JQ(2, "googlepay","JQ平台"),
    JQ_THIRD_ZALO(3, "zalopay","JQ第三方平台-zalo"),
    JQ_THIRD_MOMO(4, "momopay","JQ第三方平台-momo"),

   ;


    private Integer code;
    private String thirdCode;

    private String msg;

    PayPlatformEnum(Integer code,String thirdCode, String msg) {
        this.code = code;
        this.thirdCode = thirdCode;
        this.msg = msg;
    }

    public static Integer getCode(String thirdCode){
        for (PayPlatformEnum e : values()) {
            if (e.getThirdCode().equals(thirdCode)) {
                return e.getCode();
            }
        }
        return null;
    }
    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getThirdCode() {
        return thirdCode;
    }
}
