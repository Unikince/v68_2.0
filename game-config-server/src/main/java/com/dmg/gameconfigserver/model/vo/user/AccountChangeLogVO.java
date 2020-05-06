package com.dmg.gameconfigserver.model.vo.user;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:59 2019/12/30
 */
@Data
public class AccountChangeLogVO {
    private Long id;
    private Date createDate;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 类型名称
     */
    private String typeName;
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
