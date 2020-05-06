package com.dmg.agentserviceapi.business.transferaccountrecord.model.dto;

import java.util.Date;

/**
 * 转账纪录--获取所有对象--接收
 */
public class TransferAccountRecordGetListRecv {
    /** 订单号 */
    private String orderId;
    /** 转账玩家id */
    private long sourceId;
    /** 接收玩家id */
    private long targetId;
    /** 状态(0,所有,1开始,2完成,3撤回) */
    private int status;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(包含) */
    private Date endDate;

    /**
     * 获取：订单号
     * 
     * @return 订单号
     */
    public String getOrderId() {
        return this.orderId;
    }

    /**
     * 设置：订单号
     * 
     * @param orderId 订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取：转账玩家id
     * 
     * @return 转账玩家id
     */
    public long getSourceId() {
        return this.sourceId;
    }

    /**
     * 设置：转账玩家id
     * 
     * @param sourceId 转账玩家id
     */
    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 获取：接收玩家id
     * 
     * @return 接收玩家id
     */
    public long getTargetId() {
        return this.targetId;
    }

    /**
     * 设置：接收玩家id
     * 
     * @param targetId 接收玩家id
     */
    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    /**
     * 获取：状态(0,所有,1开始,2完成,3撤回)
     * 
     * @return 状态(0,所有,1开始,2完成,3撤回)
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * 设置：状态(0,所有,1开始,2完成,3撤回)
     * 
     * @param status 状态(0,所有,1开始,2完成,3撤回)
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * 获取：查询起始时间(包含)
     * 
     * @return 查询起始时间(包含)
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * 设置：查询起始时间(包含)
     * 
     * @param startDate 查询起始时间(包含)
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取：查询结束时间(包含)
     * 
     * @return 查询结束时间(包含)
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * 设置：查询结束时间(包含)
     * 
     * @param endDate 查询结束时间(包含)
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
