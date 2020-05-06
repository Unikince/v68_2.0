package com.dmg.agentserver.business.constant;

import com.dmg.agentserver.core.exception.IErrorEnum;

/**
 * 代理关系错误
 */
public enum AgentRelationErrorEnum implements IErrorEnum {
    /** 代理关系不存在 */
    NOAGENT(101, "代理关系不存在"),
    /** 不能绑定自己 */
    BINGSELF(111, "不能绑定自己"),
    /** 上级代理已经存在 */
    HAVEAGENT(112, "上级代理已经存在"),
    /** 已经有下级代理不能绑定 */
    HAVEDOWNAGENT(113, "已经有下级代理不能绑定"),
    /** 转移目标不是代理 */
    TONOAGENT(114, "转移目标不是代理"),
    /** 代理关系不能转移 */
    NOTRANSFERAGENT(121, "代理关系不能转移"),
    /** 代理关系已经绑定 */
    HAVEBINDINGAGENT(122, "代理关系已经绑定"),

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
    private AgentRelationErrorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
