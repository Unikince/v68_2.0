package com.dmg.fish.business.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.config.dic.FishDic;
import com.dmg.fish.business.config.dic.FishRouteDic;
import com.dmg.fish.business.config.dic.FishScenceDic;
import com.dmg.fish.business.config.dic.FishStrategyDic;
import com.dmg.fish.business.config.wrapper.FishRouteWrapper;
import com.dmg.fish.business.config.wrapper.FishScenceWrapper;
import com.dmg.fish.business.config.wrapper.FishStrategyWrapper;
import com.dmg.fish.business.model.FishScence;
import com.dmg.fish.business.model.fish.Fish;
import com.dmg.fish.business.model.route.Position;
import com.dmg.fish.business.model.route.Route;
import com.dmg.gameconfigserverapi.fish.dto.FishDTO;

import cn.hutool.core.util.RandomUtil;

/**
 * 捕鱼刷鱼策略管理
 */
@Component
public class FishStrategyMgr {
    // 鱼的路径缓存
    private final Map<Integer, Route> routes = new HashMap<>();

    @Autowired
    private FishDic fishDic;
    @Autowired
    private FishScenceDic fishScenceDic;
    @Autowired
    private FishRouteDic fishRouteDic;
    @Autowired
    private FishStrategyDic strategyDic;

    /**
     * 初始化刷鱼策略和鱼的路径
     */
    @PostConstruct
    private void init() {
        // 初始化路径
        for (FishRouteWrapper r : this.fishRouteDic.values()) {
            this.routes.put(r.getId(), new Route(r.getId(), r.getData()));
        }
    }

    /**
     * 随机鱼
     */
    private static int randomFish(List<Integer> fishs, List<Integer> weights, int totalWeight) {
        int fishId = 0;
        // 随机数
        int randWeight = RandomUtil.randomInt(totalWeight);
        int sum = 0;

        for (int i = 0; i < weights.size(); i++) {
            sum += weights.get(i);
            if (randWeight < sum) {
                fishId = fishs.get(i);
                break;
            }
        }

        return fishId;
    }

    /**
     * 产生该场景的鱼
     *
     * @param scence
     * @param startTime 开始刷鱼的时间戳
     */
    public void produceFishs(FishScence scence, long startTime) {
        FishScenceWrapper scenceBean = this.fishScenceDic.get(scence.id);

        for (Integer strategyId : scenceBean.getStrategysData()) {
            this.doProduceFishs(scence, this.strategyDic.get(strategyId), startTime);
        }
    }

    /**
     * 矩形范围刷鱼
     *
     * @param scence
     * @param strategy
     * @param startTime
     */
    private void doProduceFishs(FishScence scence, FishStrategyWrapper strategy, long startTime) {
        if (strategy.getType() == 4) {// 随机策略组刷鱼,递归刷鱼
            Integer[] strategyGroup = RandomUtil.randomEle(strategy.getGroupsData());
            for (int stg : strategyGroup) {
                this.doProduceFishs(scence, this.strategyDic.get(stg), startTime);
            }

            return;
        }

        int initDelay = strategy.getInitDelay();
        int delay = strategy.getDelay();
        int strategyType = strategy.getType();
        // 鱼之间的时差
        int fishDelay = strategy.getFishDelay();
        int times = strategy.getTimes();
        Position start = new Position(strategy.getStartData()[0], strategy.getStartData()[1]);
        // 场景时间(毫秒)
        int scenceTime = this.fishScenceDic.get(scence.id).getTime() * 1000;
        int speed = RandomUtil.randomInt(strategy.getMinSpeed(), strategy.getMaxSpeed() + 1);

        for (int i = 0; i < times; i++) {
            // 刷鱼总间隔时间
            int intervalDelay = initDelay + (delay * i);
            if (intervalDelay > scenceTime) {// 刷鱼总间隔时间超过场景时间
                break;
            }

            int fishType = randomFish(strategy.getFishsData(), strategy.getWeightsData(), strategy.getTotalWeight());
            int routeId = RandomUtil.randomEle(strategy.getRoutesData());
            int fishNum = RandomUtil.randomInt(strategy.getMinNum(), strategy.getMaxNum() + 1);

            for (int j = 0; j < fishNum; j++) {
                if (!strategy.isSameFish()) {
                    fishType = randomFish(strategy.getFishsData(), strategy.getWeightsData(), strategy.getTotalWeight());
                }
                if (!strategy.isSameRoute()) {
                    routeId = RandomUtil.randomEle(strategy.getRoutesData());
                }

                int totalFishDelay = intervalDelay + (fishDelay * j);
                if (totalFishDelay > scenceTime) {// 鱼总间隔时间超过场景时间
                    break;
                }

                // 鱼的出生时间
                long birthTime = startTime + totalFishDelay;
                Route route = this.routes.get(routeId);
                FishDTO fishDto = this.fishDic.get(fishType);
                int multiple = RandomUtil.randomInt(fishDto.getMinMultiple(), fishDto.getMaxMultiple() + 1);

                Position birthPos = null;

                if (strategyType == 2) {// 点刷鱼
                    birthPos = start;
                } else if (strategyType == 1) {// 矩形
                    double deviateX = RandomUtil.randomInt(-strategy.getDeviateX(), strategy.getDeviateX() + 1);
                    double deviateY = RandomUtil.randomInt(-strategy.getDeviateY(), strategy.getDeviateY() + 1);
                    birthPos = start.add(deviateX, deviateY);
                } else if (strategyType == 3) {// 圆形
                    // 半径
                    int r = strategy.getRadius();
                    double avgAngle = 360D / fishNum;
                    double angle = avgAngle * j;
                    double deviateX = Math.cos(Math.toRadians(angle)) * r;
                    double deviateY = Math.sin(Math.toRadians(angle)) * r;

                    birthPos = start.add(deviateX, deviateY);
                } else {
                    throw new IllegalStateException();
                }

                Fish fish = new Fish(fishType, multiple, birthTime, birthPos, route, speed, scence);
                scence.fishs.put(fish.id, fish);
            }
        }
    }

}
