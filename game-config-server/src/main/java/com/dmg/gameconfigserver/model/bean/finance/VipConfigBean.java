package com.dmg.gameconfigserver.model.bean.finance;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * VIP配置
 */
@Data
@TableName("vip_config")
public class VipConfigBean {
    /** ID(等级) */
    private Long id;
    /** 日充值限额 */
    private BigDecimal rechargeMax;
    /** 日提现次数 */
    private Integer withdrawalTimes;
    /** 修改人 */
    private String modifyUser;
    /** 修改时间 */
    private Date modifyDate;
}
