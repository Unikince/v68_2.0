package com.dmg.agentserver.business.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.agentserver.business.dao.AgentSettleCalculateDao;
import com.dmg.agentserver.business.model.bo.AgentSettleCalculateBo;
import com.dmg.agentserver.business.model.bo.AgentSettleGameDetailBo;
import com.dmg.agentserver.business.model.bo.AgentSettleRecordBo;
import com.dmg.agentserver.core.work.WorkerGroup;
import com.dmg.agentserviceapi.business.agentconfig.model.pojo.AgentConfig;
import com.dmg.agentserviceapi.business.agentlevel.model.pojo.AgentLevel;
import com.dmg.agentserviceapi.business.agentrelation.model.pojo.AgentRelation;
import com.dmg.agentserviceapi.business.performanceconfig.model.pojo.PerformanceConfig;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.GoldPaySendDto;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * 代理结算-计算
 */
@Service
public class AgentSettleCalculateService {
    @Autowired
    private AgentSettleCalculateDao dao;
    @Autowired
    private AgentConfigService agentConfigService;
    @Autowired
    private AgentLevelService agentLevelService;
    @Autowired
    private AgentRelationService agentRelationService;
    @Autowired
    private PerformanceConfigService performanceConfigService;
    @Autowired
    private NettySend nettySend;
    @Autowired
    public WorkerGroup workerGroup;

    /** 下次结算时间 */
    private long nextSettleTime = 0;

    /**
     * 初始化结算
     */
    @PostConstruct
    private void init() {
        Date now = new Date();
        AgentConfig agentConfig = this.agentConfigService.get();
        String brokerageSettleTimeStr = agentConfig.getBrokerageSettleTime();
        brokerageSettleTimeStr = DateUtil.format(now, DatePattern.NORM_DATE_PATTERN) + " " + brokerageSettleTimeStr;
        DateTime brokerageSettleTime = DateUtil.parse(brokerageSettleTimeStr, DatePattern.NORM_DATETIME_PATTERN);
        if (brokerageSettleTime.isBeforeOrEquals(now)) {
            brokerageSettleTime = DateUtil.offsetDay(brokerageSettleTime, 1);
        }
        this.nextSettleTime = brokerageSettleTime.getTime();

        this.workerGroup.scheduleWithFixedDelay(() -> {
            if (System.currentTimeMillis() < this.nextSettleTime) {
                return;
            }
            this.nextSettleTime += 86400000;// 加一天
            this.doSettle();
        }, 1, 30, TimeUnit.MINUTES);
    }

    /**
     * 改变下次结算时间
     */
    public void changeNextSettleTime() {
        AgentConfig agentConfig = this.agentConfigService.get();
        String brokerageSettleTimeStr = agentConfig.getBrokerageSettleTime();
        brokerageSettleTimeStr = DateUtil.format(DateUtil.date(this.nextSettleTime), DatePattern.NORM_DATE_PATTERN) + " " + brokerageSettleTimeStr;
        DateTime brokerageSettleTime = DateUtil.parse(brokerageSettleTimeStr, DatePattern.NORM_DATETIME_PATTERN);
        this.nextSettleTime = brokerageSettleTime.getTime();
    }

    /**
     * 执行代理结算
     */
    private void doSettle() {
        Map<Integer, Map<Long, AgentSettleCalculateBo>> gameMap = new HashMap<>();// 游戏分割纪录
        this.getSettleRecord(gameMap);// 从数据库获取结算原始数据

        this.calculateInfiniteLevelWater(gameMap);// 计算无限代理流水计算

        this.calculatePerformance(gameMap);// 计算业绩
        this.calculateBrokerage(gameMap);// 计算佣金

        this.saveAgentSettleRecord(gameMap);// 保存纪录
        this.allocateBrokerage(gameMap);// 拨款
    }

    /**
     * 从数据库获取结算原始数据
     */
    private void getSettleRecord(Map<Integer, Map<Long, AgentSettleCalculateBo>> gameMap) {
        List<AgentSettleCalculateBo> gameInfos = this.dao.getAllGameInfo();
        List<AgentRelation> relations = this.agentRelationService.getAllRelation();
        for (AgentSettleCalculateBo gameInfo : gameInfos) {
            int gameId = gameInfo.getGameId();
            String gameName = gameInfo.getGameName();
            Map<Long, AgentSettleCalculateBo> map = new HashMap<>();
            gameMap.put(gameId, map);
            for (AgentRelation relation : relations) {
                AgentSettleCalculateBo po = new AgentSettleCalculateBo();
                map.put(relation.getId(), po);
                po.setUserId(relation.getId());
                po.setUserNick(relation.getUserNick());
                po.setGameId(gameId);
                po.setGameName(gameName);
                po.setMyBrokerage(BigDecimal.ZERO);
                po.setTeamBrokerage(BigDecimal.ZERO);
                po.setMyPerformance(BigDecimal.ZERO);
                po.setSubDirectlyPerformance(BigDecimal.ZERO);
                po.setSubTeamPerformance(BigDecimal.ZERO);
            }
        }

        List<AgentSettleCalculateBo> records = this.dao.getYesterdaySettleRecord();
        for (AgentSettleCalculateBo record : records) {
            int gameId = record.getGameId();
            long userId = record.getUserId();
            AgentSettleCalculateBo po = gameMap.get(gameId).get(userId);
            po.setMyPerformance(record.getMyPerformance());
        }
    }

    /**
     * 计算无限代理流水计算
     */
    private void calculateInfiniteLevelWater(Map<Integer, Map<Long, AgentSettleCalculateBo>> gameMap) {
        for (Map<Long, AgentSettleCalculateBo> map : gameMap.values()) {
            for (AgentSettleCalculateBo po : map.values()) {
                AgentRelation relation = this.agentRelationService.get(po.getUserId());
                BigDecimal myPerformance = po.getMyPerformance();

                if (relation.getParentId() != null) {
                    AgentSettleCalculateBo parentPo = map.get(relation.getParentId());
                    parentPo.setSubDirectlyPerformance(parentPo.getSubDirectlyPerformance().add(myPerformance));
                }

                while (relation.getParent() != null) {
                    relation = relation.getParent();
                    po = map.get(po.getUserId());
                    po.setSubTeamPerformance(po.getSubTeamPerformance().add(myPerformance));
                }
            }
        }
    }

    /**
     * 计算业绩
     */
    private void calculatePerformance(Map<Integer, Map<Long, AgentSettleCalculateBo>> gameMap) {
        List<PerformanceConfig> pcs = this.performanceConfigService.getList();
        for (PerformanceConfig pc : pcs) {
            BigDecimal ratio = pc.getRatio().divide(new BigDecimal(100));// 百分比转小数
            Map<Long, AgentSettleCalculateBo> map = gameMap.get(pc.getGameId());
            for (AgentSettleCalculateBo po : map.values()) {
                po.setMyPerformance(po.getMyPerformance().multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP));
                po.setSubDirectlyPerformance(po.getSubDirectlyPerformance().multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP));
                po.setSubTeamPerformance(po.getSubTeamPerformance().multiply(ratio).setScale(2, BigDecimal.ROUND_HALF_UP));
            }
        }
    }

    /**
     * 计算佣金
     */
    private void calculateBrokerage(Map<Integer, Map<Long, AgentSettleCalculateBo>> gameMap) {
        PagePackageRes<List<AgentLevel>> levelsPack = this.agentLevelService.getList(null);
        List<AgentLevel> levels = levelsPack.getData();
        AgentLevel lastLevel = levels.get(levels.size() - 1);
        lastLevel.setPerformanceEnd(new BigDecimal(Long.MAX_VALUE));

        TreeRangeMap<BigDecimal, AgentLevel> rangeMap = TreeRangeMap.create();
        for (AgentLevel level : levels) {
            rangeMap.put(Range.closed(level.getPerformanceBegin(), level.getPerformanceEnd()), level);
        }

        // begin:计算每个玩家总业绩
        Map<Long, BigDecimal> performanceMap = new HashMap<>();
        for (Map<Long, AgentSettleCalculateBo> map : gameMap.values()) {
            for (AgentSettleCalculateBo po : map.values()) {
                BigDecimal performance = performanceMap.get(po.getUserId());
                if (performance == null) {
                    performance = BigDecimal.ZERO;
                    performanceMap.put(po.getUserId(), performance);
                }
                performance.add(po.getSubTeamPerformance());
            }
        }
        // end:计算每个玩家总业绩

        // begin:计算佣金
        List<AgentRelation> relations = this.agentRelationService.getAllDownToUpRelation();
        for (AgentRelation relation : relations) {
            List<AgentRelation> children = relation.getChildren();
            if (children.isEmpty()) {
                continue;
            }
            long userId = relation.getId();
            BigDecimal allPerformance = performanceMap.get(userId);// 玩家每日业绩总和

            AgentLevel level = rangeMap.get(allPerformance);
            if (level == null) {
                level = lastLevel;
            }
            BigDecimal brokerageRatio = level.getBrokerageRatio();
            brokerageRatio = brokerageRatio.divide(new BigDecimal(100));// 百分比转小数
            for (Map<Long, AgentSettleCalculateBo> map : gameMap.values()) {
                AgentSettleCalculateBo po = map.get(userId);
                po.setAgentLevelId(level.getId());
                BigDecimal teamBrokerage = po.getSubTeamPerformance().multiply(brokerageRatio).setScale(2, BigDecimal.ROUND_HALF_UP);
                po.setTeamBrokerage(teamBrokerage);
                for (AgentRelation child : children) {
                    AgentSettleCalculateBo childPo = map.get(child.getId());
                    teamBrokerage = teamBrokerage.subtract(childPo.getMyBrokerage());
                }
                po.setMyBrokerage(teamBrokerage);
            }
        }
        // end:计算佣金
    }

    /**
     * 保存纪录
     */
    private void saveAgentSettleRecord(Map<Integer, Map<Long, AgentSettleCalculateBo>> gameMap) {
        Date yestory = DateUtil.beginOfDay(DateUtil.yesterday());
        Map<Long, AgentSettleRecordBo> recordMap = new HashMap<>();
        Map<Long, List<AgentSettleGameDetailBo>> detailMap = new HashMap<>();

        for (Map<Long, AgentSettleCalculateBo> map : gameMap.values()) {
            for (AgentSettleCalculateBo po : map.values()) {
                AgentSettleRecordBo record = recordMap.get(po.getUserId());
                if (record == null) {
                    record = new AgentSettleRecordBo();
                    record.setDayStr(yestory);
                    record.setUserId(po.getUserId());
                    record.setUserNick(po.getUserNick());
                    record.setMyPerformance(BigDecimal.ZERO);
                    record.setSubDirectlyPerformance(BigDecimal.ZERO);
                    record.setSubTeamPerformance(BigDecimal.ZERO);
                    record.setAgentLevelId(po.getAgentLevelId());
                    record.setMyBrokerage(BigDecimal.ZERO);
                    record.setTeamBrokerage(BigDecimal.ZERO);
                    record.setHistoryBrokerage(record.getMyBrokerage());
                    recordMap.put(po.getUserId(), record);
                }
                List<AgentSettleGameDetailBo> details = detailMap.get(po.getUserId());
                if (details == null) {
                    details = new ArrayList<>();
                    detailMap.put(po.getUserId(), details);
                }

                AgentSettleGameDetailBo detail = new AgentSettleGameDetailBo();
                detail.setGameId(po.getGameId());
                detail.setGameName(po.getGameName());
                detail.setMyPerformance(po.getMyBrokerage());
                detail.setSubDirectlyPerformance(po.getSubDirectlyPerformance());
                detail.setSubTeamPerformance(po.getSubTeamPerformance());
                details.add(detail);

                record.setMyPerformance(record.getMyPerformance().add(po.getMyPerformance()));
                record.setSubDirectlyPerformance(record.getSubDirectlyPerformance().add(po.getSubDirectlyPerformance()));
                record.setSubTeamPerformance(record.getMyPerformance().add(po.getSubTeamPerformance()));
                record.setMyBrokerage(record.getMyBrokerage().add(po.getMyBrokerage()));
                record.setTeamBrokerage(record.getTeamBrokerage().add(po.getTeamBrokerage()));
            }
        }

        List<AgentSettleRecordBo> lastRecords = this.dao.getRecordByDate(DateUtil.offsetDay(yestory, -1));
        for (AgentSettleRecordBo lastRecord : lastRecords) {
            AgentSettleRecordBo record = recordMap.get(lastRecord.getUserId());
            record.setHistoryBrokerage(record.getMyBrokerage().add(lastRecord.getHistoryBrokerage()));
        }
        this.dao.insertBatchRecord(new ArrayList<>(recordMap.values()));
        List<AgentSettleRecordBo> records = this.dao.getRecordByDate(yestory);
        List<AgentSettleGameDetailBo> details = new ArrayList<>();
        for (AgentSettleRecordBo record : records) {
            List<AgentSettleGameDetailBo> tdetails = detailMap.get(record.getUserId());
            for (AgentSettleGameDetailBo detail : tdetails) {
                detail.setRecordId(record.getId());
            }
            details.addAll(tdetails);
            if (details.size() >= 5000) {
                this.dao.insertBatchDetail(details);
                tdetails.clear();
            }
        }
        if (!details.isEmpty()) {
            this.dao.insertBatchDetail(details);
        }
    }

    /**
     * 拨款
     */
    private void allocateBrokerage(Map<Integer, Map<Long, AgentSettleCalculateBo>> gameMap) {
        Map<Long, BigDecimal> moneyMap = new HashMap<>();
        for (Map<Long, AgentSettleCalculateBo> map : gameMap.values()) {
            for (AgentSettleCalculateBo po : map.values()) {
                BigDecimal money = moneyMap.get(po.getUserId());
                if (money == null) {
                    money = BigDecimal.ZERO;
                    moneyMap.put(po.getUserId(), money);
                }
                money.add(po.getMyBrokerage());
            }
        }
        for (Entry<Long, BigDecimal> entry : moneyMap.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            long userId = entry.getKey();
            BigDecimal money = entry.getValue();

            GoldPaySendDto dto = new GoldPaySendDto();
            dto.setUserId(userId);
            dto.setPayGold(money);
            dto.setType(AccountChangeTypeEnum.AGENT_SETTLE_BROKERAGE.getCode());
            this.nettySend.goldPayAsync(dto);
        }
    }
}