package com.dmg.gameconfigserver.model.bean.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO 账变记录表
 * @Date 13:49 2019/11/6
 */
@Data
@TableName("t_dmg_account_change_log")
public class AccountChangeLogBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 变更值
     */
    private BigDecimal account;
    /**
     * 变更前值
     */
    private BigDecimal beforeAccount;
    /**
     * 变更后值
     */
    private BigDecimal afterAccount;
    /**
     * 账单号
     */
    private Long accountNo;
    /**
     * userId
     */
    private Long userId;
}
