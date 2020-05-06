/**
 *
 */
package com.dmg.doudizhuserver.core.msg;

/**
 * 消息结果
 */
public class MessageResult {
    /** 协议号 */
    private String m;
    /** 消息结果 */
    private int res;
    /** 消息数据 */
    private Object msg;
    /** 系统时间 */
    private long time;

    /** 构造方法 */
    public MessageResult(String m, int res, Object msg) {
        super();
        this.m = m;
        this.res = res;
        this.msg = msg;
        this.time = System.currentTimeMillis();
    }

    /**
     * 获取：协议号
     *
     * @return 协议号
     */
    public String getM() {
        return this.m;
    }

    /**
     * 设置：协议号
     *
     * @param m 协议号
     */
    public void setM(String m) {
        this.m = m;
    }

    /**
     * 获取：消息结果
     *
     * @return 消息结果
     */
    public int getRes() {
        return this.res;
    }

    /**
     * 设置：消息结果
     *
     * @param res 消息结果
     */
    public void setRes(int res) {
        this.res = res;
    }

    /**
     * 获取：消息数据
     *
     * @return 消息数据
     */
    public Object getMsg() {
        return this.msg;
    }

    /**
     * 设置：消息数据
     *
     * @param msg 消息数据
     */
    public void setMsg(Object msg) {
        this.msg = msg;
    }

    /**
     * 获取：系统时间
     *
     * @return 系统时间
     */
    public long getTime() {
        return this.time;
    }

    /**
     * 设置：系统时间
     *
     * @param time 系统时间
     */
    public void setTime(long time) {
        this.time = time;
    }

}