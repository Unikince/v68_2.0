package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description  充值金额配置
 * @Author jock
 * @Date 2019/7/3 0003
 * @Version V1.0
 **/
@Data
@TableName("sys_recharge_log")
public class SysRechargeLogBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 充值类型 1:金币
     */
    private Integer rechargeType;
    /**
     * 平台类型 1:vnnn 2:JQ 3:JQ_THIRD
     */
    private Integer platformType;
    /**
     * 充值金额
     */
    private Long rechargeNumber;
    /**
     *创建时间
     */
    private Date creatDate;
    /**
     * 更新时间
     */
    private Date updateDate;
}
