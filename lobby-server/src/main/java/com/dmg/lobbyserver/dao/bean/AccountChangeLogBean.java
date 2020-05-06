package com.dmg.lobbyserver.dao.bean;

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
 * @Author liubo
 * @Description //TODO 账变记录表
 * @Date 13:49 2019/11/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_dmg_account_change_log")
public class AccountChangeLogBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 修改人
     */
    private Long modifyUser;
    /**
     * 顺序
     */
    private Long sort;
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
