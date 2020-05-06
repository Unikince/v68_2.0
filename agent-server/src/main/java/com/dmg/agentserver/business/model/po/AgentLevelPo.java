package com.dmg.agentserver.business.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.agentserviceapi.business.agentlevel.model.pojo.AgentLevel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代理等级
 */
@Data
@TableName("a_agent_level")
@EqualsAndHashCode(callSuper = false)
public class AgentLevelPo extends AgentLevel {
}
