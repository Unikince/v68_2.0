package com.dmg.agentserver.business.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dmg.agentserviceapi.business.agentrelation.model.pojo.AgentRelation;

/**
 * 代理关系
 */
@Mapper
public interface AgentRelationDao {
    /**
     * 查询所有代理关系
     */
    List<AgentRelation> allRelation();

    /**
     * 调用存储过程增加每日代理的直属下级人数
     */
    void addNewChildNum(long userId);

    /**
     * 绑定代理
     */
    void bindAgent(long playerId, long parentId, Date bindTime);

    /**
     * 获取新增用户
     */
    int getNewChildNum(long userId);
}
