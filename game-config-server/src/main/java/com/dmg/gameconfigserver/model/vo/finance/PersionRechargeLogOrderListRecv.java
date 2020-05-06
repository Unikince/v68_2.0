package com.dmg.gameconfigserver.model.vo.finance;

import java.util.Date;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

/**
 * 人工充值订单列表
 */
public class PersionRechargeLogOrderListRecv extends PageReqDTO {
    /** 玩家id */
    private Long userId;
    /** 订单id */
    private String orderId;
    /** 类型 */
    private Integer type;
    /** 状态 */
    private Integer status;
    /** 处理人 */
    private String dealUser;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(不包含) */
    private Date endDate;

    /**
     * 获取：玩家id
     *
     * @return 玩家id
     */
    public Long getUserId() {
        return this.userId;
    }

    /**
     * 设置：玩家id
     *
     * @param userId 玩家id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取：订单id
     *
     * @return 订单id
     */
    public String getOrderId() {
        return this.orderId;
    }

    /**
     * 设置：订单id
     *
     * @param orderId 订单id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取：类型
     *
     * @return 类型
     */
    public Integer getType() {
        return this.type;
    }

    /**
     * 设置：类型
     *
     * @param type 类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取：状态
     *
     * @return 状态
     */
    public Integer getStatus() {
        return this.status;
    }

    /**
     * 设置：状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
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
     * 获取：查询结束时间(不包含)
     *
     * @return 查询结束时间(不包含)
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * 设置：查询结束时间(不包含)
     *
     * @param endDate 查询结束时间(不包含)
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取：处理人
     * 
     * @return 处理人
     */
    public String getDealUser() {
        return this.dealUser;
    }

    /**
     * 设置：处理人
     * 
     * @param dealUser 处理人
     */
    public void setDealUser(String dealUser) {
        this.dealUser = dealUser;
    }

}
