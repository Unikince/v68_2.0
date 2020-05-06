package com.dmg.gameconfigserver.model.vo.finance;

import java.util.Date;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

/**
 * 充值审核订单列表
 */
public class PersionRechargeLogAuditListRecv extends PageReqDTO {
    /** 玩家id */
    private Long userId;
    /** 订单id */
    private String orderId;
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
}
