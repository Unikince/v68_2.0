package com.dmg.gameconfigserver.model.bean.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mice
 * @email .com
 * @date 2019-12-24 11:23:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_platform_recharge_log")
public class PlatformRechargeLogBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 我方订单id
     */
    private String orderId;
    /**
     * 第三方订单id
     */
    private String thirdOrderId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 系统商品配置id
     */
    private Long sysMallConfigId;
    /**
     * 请求次数
     */
    private Integer requestCount;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 支付时间
     */
    private Date payDate;
    /**
     * 到账时间
     */
    private Date arriveDate;
    /**
     * 处理人id
     */
    private Long dealUserId;
    /**
     * 处理时间
     */
    private Date dealDate;
    /**
     * 订单状态 1: 等待支付 5:支付失败 10:未到账 15:已完成 20:补单成功
     */
    private Integer orderStatus;
    /**
     * 支付平台id
     */
    private Integer platformId;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 充值金额
     */
    private BigDecimal rechargeAmount;
    /**
     * 请求源数据
     */
    private String requestBody;
    /**
     * 请求返回数据
     */
    private String responseBody;
    /**
     * 失败错误码
     */
    private Integer errorCode;
}
