package com.dmg.agentserviceapi.business.agentrelation.model.pojo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Data;

/**
 * 代理关系
 */
@Data
public class AgentRelation {
    /** 玩家id */
    private Long id;
    /** 玩家昵称 */
    private String userNick;
    /** 上级id */
    private Long parentId;
    /** 上级对象 */
    private AgentRelation parent;
    /** 绑定时间 */
    private Date bindTime;
    /** 直接下级列表 */
    private List<AgentRelation> children;
    /** 下级团队人数 */
    private int teamPeopleNum;

    /**
     * 构造方法
     */
    public AgentRelation() {
        this.children = new CopyOnWriteArrayList<>();
    }
}
