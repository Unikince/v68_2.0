package com.dmg.gameconfigserver.model.bean.finance;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 人工充值
 */
@Data
@TableName("t_persion_recharge_log")
public class PersionRechargeLogBean {
    /** id */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单id */
    private String orderId;
    /** 用户id */
    private Long userId;
    /** 昵称 */
    private String nickname;
    /** 充值类型 */
    private Integer type;
    /** 充值金额 */
    private BigDecimal rechargeAmount = BigDecimal.ZERO;
    /** 赠送金额 */
    private BigDecimal giveAmount = BigDecimal.ZERO;
    /** 到账金额 */
    private BigDecimal accountAmount = BigDecimal.ZERO;
    /** 状态 1:等待审核,2:审核通过,3:审核拒绝 */
    private Integer status = 1;
    /** 创建人 */
    private String createUser;
    /** 创建时间 */
    private Date createDate;
    /** 充值备注 */
    private String createRemark;
    /** 处理人 */
    private String dealUser;
    /** 处理时间 */
    private Date dealDate;
    /** 处理备注 */
    private String dealRemark;
}
