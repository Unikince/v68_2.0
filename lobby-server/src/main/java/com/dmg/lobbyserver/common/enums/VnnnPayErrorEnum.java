package com.dmg.lobbyserver.common.enums;

import com.dmg.lobbyserver.result.ResultEnum;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/27 9:56
 * @Version V1.0
 **/
public enum VnnnPayErrorEnum {

    SIGN_ERROR(1000, null, "签名验证失败"),
    APPID_ERROR(1001, null, "APP_ID验证失败"),
    USER_NO_EXIT(1002, null, "用户验证失败"),
    ITEM_NO_EXIT(1003, null, "商品不存在"),
    PRICE_NO_MATCH(1004, null, "价格不匹配"),
    SYS_SEND_GOLD_ERROR(1005, null, "系统发送金币失败"),
    SYS_ERROR(ResultEnum.SYS_ERROR.getCode(), 5005, "服务异常"),
    DEC_ERROR(ResultEnum.DEC_ERROR.getCode(), 5001, "解密数据失败"),
    VND_ERROE(ResultEnum.VND_ERROE.getCode(), 5003, "vnd参数错误"),
    GOLD_NOT_ENOUGH(ResultEnum.GOLD_NOT_ENOUGH.getCode(), 5004, "额度不足"),
    VNNN_PAY_APPID_ERROR(ResultEnum.APPID_ERROR.getCode(), 5006, "APP_ID验证失败"),
    VNNN_PAY_USER_NO_EXIT(ResultEnum.USER_NO_EXIT.getCode(), 5002, "用户验证失败"),
    VNNN_PAY_SIGN_ERROR(ResultEnum.SIGN_ERROR.getCode(), null, "签名验证失败"),
    SUCCESS(1, 0, "成功");


    private Integer code;

    //vnnnPay code
    private Integer vnnnPayCode;

    private String msg;

    public static Integer getCodeByVnnnPayCode(Integer vnnnPayCode) {
        for (VnnnPayErrorEnum vnnnPayErrorEnum : VnnnPayErrorEnum.values()) {
            if (vnnnPayErrorEnum.getVnnnPayCode() != null
                    && vnnnPayErrorEnum.getVnnnPayCode().intValue() == vnnnPayCode.intValue()) {
                return vnnnPayErrorEnum.code;
            }
        }
        return null;
    }

    VnnnPayErrorEnum(Integer code, Integer vnnnPayCode, String msg) {
        this.code = code;
        this.vnnnPayCode = vnnnPayCode;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getVnnnPayCode() {
        return vnnnPayCode;
    }
}