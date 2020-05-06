package com.dmg.agentserver.business.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.agentserver.business.constant.AgentRelationErrorEnum;
import com.dmg.agentserver.business.dao.AgentRelationDao;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationGifaudRes;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationGifciRes;
import com.dmg.agentserviceapi.business.agentrelation.model.pojo.AgentRelation;

/**
 * 代理关系
 */
@Service
public class AgentRelationService {
    @Autowired
    private AgentRelationDao dao;

    /** ID->代理关系 */
    private static Map<Long, AgentRelation> map = new ConcurrentHashMap<>();

    /**
     * 构建代理关系
     */
    public void buildAgentRelation() {
        List<AgentRelation> list = this.dao.allRelation();
        // begin:保存到map
        for (AgentRelation obj : list) {
            map.put(obj.getId(), obj);
        }
        // end:保存到map

        // begin:构建下级列表
        for (AgentRelation obj : list) {
            if (obj.getParentId() == null) {
                continue;
            }
            AgentRelation parent = map.get(obj.getParentId());
            parent.getChildren().add(obj);
            obj.setParent(parent);
        }
        // begin:构建下级列表

        this.infiniteLevelTeamPeopleNum();// 无限代理下级团队人数
    }

    /**
     * 无限代理下级团队人数
     */
    private void infiniteLevelTeamPeopleNum() {
        for (AgentRelation obj : map.values()) {
            while (obj.getParentId() != null) {
                obj = map.get(obj.getParentId());
                obj.setTeamPeopleNum(obj.getTeamPeopleNum() + 1);
            }
        }
    }

    /**
     * 创建代理关系对象
     */
    public void create(long playerId, String nickName) {
        AgentRelation relation = new AgentRelation();
        relation.setId(playerId);
        relation.setUserNick(nickName);
        map.put(playerId, relation);
    }

    /**
     * 绑定代理
     */
    public void bind(long playerId, long parentId) {
        if (playerId == parentId) {// 不能绑定自己
            throw BusinessException.create(AgentRelationErrorEnum.BINGSELF, "" + playerId);
        }
        AgentRelation agentRelation = map.get(playerId);
        if (agentRelation == null) {// 代理关系不存在
            throw BusinessException.create(AgentRelationErrorEnum.NOAGENT, "" + playerId);
        }
        if (agentRelation.getParentId() != 0) {// 已经有代理不能绑定
            throw BusinessException.create(AgentRelationErrorEnum.HAVEAGENT, "" + playerId);
        }
        if (agentRelation.getTeamPeopleNum() > 0) {// 已经有下级不能绑定
            throw BusinessException.create(AgentRelationErrorEnum.HAVEDOWNAGENT, "" + playerId);
        }
        AgentRelation parentAgent = map.get(parentId);
        if (parentAgent == null) {// 代理关系不存在
            throw BusinessException.create(AgentRelationErrorEnum.NOAGENT, "" + parentId);
        }
        Date now = new Date();
        this.dao.bindAgent(playerId, parentId, now);
        this.dao.addNewChildNum(parentId);
        parentAgent.getChildren().add(agentRelation);
        agentRelation.setBindTime(now);
        agentRelation.setParentId(parentId);
        agentRelation.setParent(parentAgent);
        // 更新代理团队人数
        this.updateTeamPeopleNum(parentAgent, 1);
    }

    /**
     * 代理转移
     */
    public void transfer(long transferId, long toAgentId, boolean includeSelf) {
        AgentRelation agentRelation = map.get(transferId);
        if (agentRelation == null) {
            throw BusinessException.create(AgentRelationErrorEnum.NOAGENT, "" + transferId);
        }
        if (agentRelation.getParentId() == toAgentId) {
            throw BusinessException.create(AgentRelationErrorEnum.HAVEBINDINGAGENT, "" + transferId);
        }

        AgentRelation toAgent = map.get(toAgentId);
        if (toAgent == null) {
            throw BusinessException.create(AgentRelationErrorEnum.TONOAGENT, "" + toAgentId);
        }
        // 转移代理关系ids
        List<Long> myUpTeam = new ArrayList<>();
        this.getUpTeamId(myUpTeam, agentRelation.getParentId());
        List<Long> myDownTeam = new ArrayList<>();
        List<Long> agentIds = new ArrayList<>();
        this.getDownTeamId(myDownTeam, agentIds);
        myUpTeam.addAll(myDownTeam);

        // 目标代理关系ids
        List<Long> toUpTeam = new ArrayList<>();
        this.getUpTeamId(toUpTeam, toAgent.getParentId());
        List<Long> toDownTeam = new ArrayList<>();
        List<Long> toAgentIds = new ArrayList<>();
        this.getDownTeamId(toDownTeam, toAgentIds);
        toUpTeam.addAll(toDownTeam);

        // 检查有没有交集
        myUpTeam.retainAll(toUpTeam);
        if (myUpTeam.size() > 0) {
            // 不能转移
            throw BusinessException.create(AgentRelationErrorEnum.NOTRANSFERAGENT, "" + transferId);
        }

        // 修改转移关系
        if (includeSelf) {
            agentRelation.setParentId(toAgentId);
            agentRelation.setParent(toAgent);
            this.updateTeamPeopleNum(toAgent, agentRelation.getTeamPeopleNum() + 1);
        } else {
            // 不包括自己
            agentRelation.setParentId(0l);
            agentRelation.setParent(null);
            List<AgentRelation> agentChildren = agentRelation.getChildren();
            for (AgentRelation ar : agentChildren) {
                ar.setParentId(toAgentId);
                ar.setParent(toAgent);
            }
            this.updateTeamPeopleNum(toAgent, agentRelation.getTeamPeopleNum());
        }

    }

    /**
     * 更新所有上级的团队人数
     */
    private void updateTeamPeopleNum(AgentRelation agentRelation, int teamPeopleNum) {
        do {
            agentRelation.setTeamPeopleNum(agentRelation.getTeamPeopleNum() + teamPeopleNum);
            agentRelation = agentRelation.getParent();
        } while (agentRelation != null);
    }

    /**
     * 获取所有上级代理id列表
     */
    private void getUpTeamId(List<Long> agentIds, Long upAgentId) {
        if (upAgentId == 0) {
            return;
        }
        if (agentIds.contains(upAgentId)) {
            return;
        }
        AgentRelation upAgent = map.get(upAgentId);
        if (upAgent == null) {
            return;
        }
        agentIds.add(upAgentId);
        if (upAgent.getParentId() == 0) {
            return;
        }
        this.getUpTeamId(agentIds, upAgent.getParentId());
    }

    /**
     * 获取直接下级id
     */
    private void getDownTeamId(List<Long> myDownTeam, List<Long> agentIds) {
        List<Long> newDownTeamId = new ArrayList<>();
        for (AgentRelation ar : map.values()) {
            if (ar.getParentId() != 0 && agentIds.contains(ar.getParentId())) {
                newDownTeamId.add(ar.getParentId());
            }
        }
        agentIds.clear();
        if (newDownTeamId.isEmpty()) {
            return;
        }
        myDownTeam.addAll(newDownTeamId);
        this.getDownTeamId(myDownTeam, newDownTeamId);
    }

    /**
     * 后台玩家详情获取代理信息
     */
    public AgentRelationGifaudRes getInfoForAdminUserDetails(long playerId) {
        AgentRelationGifaudRes result = new AgentRelationGifaudRes();
        // 上级玩家id
        AgentRelation relation = map.get(playerId);
        if (relation.getParentId() != null) {
            result.setParentId(playerId);
        } // 直属下级数量
        result.setSubPeopleNum(relation.getChildren().size());
        // 下级团队人数
        result.setTeamPeopleNum(relation.getTeamPeopleNum());
        // 直属下级信息列表
        List<AgentRelationGifaudRes> subInfoList = new ArrayList<>();
        result.setSubInfoList(subInfoList);
        for (AgentRelation child : relation.getChildren()) {
            AgentRelationGifaudRes subInfo = new AgentRelationGifaudRes();
            subInfo.setPlayerId(child.getId());
            subInfo.setParentId(child.getParentId());
            subInfo.setSubPeopleNum(child.getChildren().size());
            subInfo.setTeamPeopleNum(child.getTeamPeopleNum());
        }
        return result;
    }

    /**
     * 客户端首页获取代理信息
     */
    public AgentRelationGifciRes getInfoForClientIndex(long playerId) {
        AgentRelationGifciRes result = new AgentRelationGifciRes();
        // 上级玩家id
        AgentRelation relation = map.get(playerId);
        if (relation.getParentId() != null) {
            result.setParentId(playerId);
        } // 直属下级数量
        result.setSubPeopleNum(relation.getChildren().size());
        // 下级团队人数
        result.setTeamPeopleNum(relation.getTeamPeopleNum());
        // 今日新增用户
        int newPeopleNum = this.dao.getNewChildNum(playerId);
        result.setNewPeopleNum(newPeopleNum);
        return result;
    }

    /**
     * 查询自己代理关系
     */
    public AgentRelation get(long id) {
        return map.get(id);
    }

    /**
     * 检查是否是上下级关系(多层)
     */
    public boolean isChild(long parentId, long childId) {
        AgentRelation childRelation = map.get(childId);
        List<AgentRelation> parentRelations = this.getAllParentRelation(childRelation);
        for (AgentRelation parentRelation : parentRelations) {
            if (parentRelation.getId() == parentId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有上级
     */
    private List<AgentRelation> getAllParentRelation(AgentRelation agentRelation) {
        List<AgentRelation> result = new ArrayList<>();
        while (agentRelation.getParent() != null) {
            agentRelation = agentRelation.getParent();
            result.add(agentRelation);
        }
        return result;
    }

    /**
     * 返回所有关系
     */
    public List<AgentRelation> getAllRelation() {
        return new ArrayList<>(map.values());
    }

    /**
     * 获取所有从下到上排序的代理关系
     */
    public List<AgentRelation> getAllDownToUpRelation() {
        Queue<AgentRelation> queue = new LinkedList<>();
        for (AgentRelation relation : map.values()) {
            if (relation.getParent() == null) {
                queue.offer(relation);
            }
        }
        return this.genDownToUpRelation(queue);
    }

    /**
     * 获取指定代理的团队(包括自己)从下到上排序的代理关系
     */
    public List<AgentRelation> getDownToUpRelation(long playerId) {
        AgentRelation relation = map.get(playerId);
        if (relation == null) {
            throw BusinessException.create(AgentRelationErrorEnum.NOAGENT, "" + playerId);
        }
        Queue<AgentRelation> queue = new LinkedList<>();

        queue.offer(relation);
        return this.genDownToUpRelation(queue);
    }

    /**
     * 生成指定队列(队列元素同级)从下到上排序的代理关系
     */
    private List<AgentRelation> genDownToUpRelation(Queue<AgentRelation> queue) {
        List<AgentRelation> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            AgentRelation relation = queue.poll();
            result.add(relation);
            queue.addAll(relation.getChildren());
        }
        Collections.reverse(result);
        return result;
    }

}