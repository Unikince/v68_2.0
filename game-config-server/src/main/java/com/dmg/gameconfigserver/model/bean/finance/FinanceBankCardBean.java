package com.dmg.gameconfigserver.model.bean.finance;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 财务银行卡
 */
@Data
@TableName("finance_bank_card")
public class FinanceBankCardBean {
    /** ID */
    private Long id;
    /** 姓名 */
    private String name;
    /** 银行名称 */
    private String bankName;
    /** 银行卡号 */
    private String bankCode;
    /** 开户省份 */
    private String province;
    /** 开户城市 */
    private String city;
    /** 开户支行 */
    private String branchBank;
    /** 金额 */
    private BigDecimal money;
}
