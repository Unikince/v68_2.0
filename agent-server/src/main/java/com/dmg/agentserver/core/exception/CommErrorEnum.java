package com.dmg.agentserver.core.exception;

/**
 * 公共错误枚举
 */
public enum CommErrorEnum implements IErrorEnum {
    /** 未知错误 */
    UNKNOWN_ERROR(11, "未知错误"),
    /** 其他错误 */
    OTHER_ERROR(12, "其他错误"),
    /** 参数为空 */
    PARAM_EMPTY(31, "参数为空"),
    /** 参数错误 */
    PARAM_ERROR(32, "参数错误"),
    /** 对象不存在 */
    OBJ_NOT_EXIST(33, "对象不存在"),
    /** 分页错误 */
    PAGE_ERROR(34, "分页错误"),
    /** 长度错误 */
    LENGTH_ERROR(35, "长度错误"),

    /** 小于最小值 */
    LT_MIN(51, "小于最小值"),
    /** 小于等于最小值 */
    LE_MIN(52, "小于等于最小值"),
    /** 大于最大值 */
    GT_MAX(53, "大于最大值"),
    /** 大于等于最大值 */
    GE_MAX(54, "大于等于最大值"),
    /** 等于指定值 */
    EQ_VALUE(55, "等于指定值"),
    /** 不等于指定值 */
    NE_VALUE(56, "不等于指定值"),
    /** 小于0 */
    LT_ZERO(61, "小于0"),
    /** 小于等于0 */
    LE_ZERO(62, "小于等于0"),
    /** 大于0 */
    GT_ZERO(63, "大于0"),
    /** 大于等于0 */
    GE_ZERO(64, "大于等于0"),
    /** 等于0 */
    EQ_ZERO(65, "等于0"),
    /** 不等于0 */
    NE_ZERO(66, "不等于0"),
    /** 开始时间大于结束时间 */
    START_TIME_IS_GREATER_THAN_END_TIME(67, "开始时间大于结束时间");

    ;

    /** 错误码 */
    private int code;
    /** 错误描述 */
    private String desc;

    /**
     * 构建方法
     *
     * @param code 错误码
     * @param desc 错误描述
     */
    private CommErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取：错误码
     *
     * @return 错误码
     */
    @Override
    public int getCode() {
        return this.code;
    }

    /**
     * 获取：错误描述
     *
     * @return 错误描述
     */
    @Override
    public String getDesc() {
        return this.desc;
    }
}
