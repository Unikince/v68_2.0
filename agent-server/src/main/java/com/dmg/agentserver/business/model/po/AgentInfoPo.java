package com.dmg.agentserver.business.model.po;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 代理信息
 */
@Data
@TableName("a_agent_info")
public class AgentInfoPo {
    /** 逻辑ID */
    private long id;
    /** 玩家id */
    private long userId;
    /** 上级id */
    private long parentId;
    /** 节点等级 */
    private int treeLevel;
    /** 下级人数 */
    private int childNum;
}
