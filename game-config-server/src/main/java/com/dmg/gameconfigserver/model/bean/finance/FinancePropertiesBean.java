package com.dmg.gameconfigserver.model.bean.finance;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 财务属性
 */
@Data
@TableName("finance_properties")
public class FinancePropertiesBean {
    private String configKey;
    private String configValue;
}
