package com.dmg.data.common.message;

import com.dmg.data.common.constant.NettyErrorEnum;

/**
 * 消息异常
 */
public class NettyMsgException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /** 状态码 */
    private int code;

    public NettyMsgException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public NettyMsgException(int code, String msg, Throwable cause) {
        this(code, msg);
        this.initCause(cause);
    }

    public NettyMsgException(int code) {
        this(code, "");
    }

    public NettyMsgException(int code, Throwable cause) {
        this(code, "", cause);
    }

    public NettyMsgException(NettyErrorEnum errorEnum) {
        this(errorEnum.getCode(), errorEnum.getDesc());
    }

    public NettyMsgException(NettyErrorEnum errorEnum, Throwable cause) {
        this(errorEnum.getCode(), errorEnum.getDesc(), cause);
    }

    public NettyMsgException(NettyErrorEnum errorEnum, String msg) {
        this(errorEnum.getCode(), errorEnum.getDesc() + ":" + msg);
    }

    public NettyMsgException(NettyErrorEnum errorEnum, String msg, Throwable cause) {
        this(errorEnum.getCode(), errorEnum.getDesc() + ":" + msg, cause);
    }

    public int getCode() {
        return this.code;
    }
}
