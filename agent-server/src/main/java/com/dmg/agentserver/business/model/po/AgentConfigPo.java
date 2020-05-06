package com.dmg.agentserver.business.model.po;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 代理配置
 */
@Data
@TableName("a_agent_config")
public class AgentConfigPo {
    /** 配置名 */
    private String configKey;
    /** 配置值 */
    private String configValue;
}
