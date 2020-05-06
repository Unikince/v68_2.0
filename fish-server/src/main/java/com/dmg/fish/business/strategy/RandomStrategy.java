package com.dmg.fish.business.strategy;

import java.util.List;

/**
 * 随机策略组刷鱼
 */
public class RandomStrategy implements Strategy {
    /** 策略组 */
    private List<Integer[]> strategyGroups;

    @Override
    public StrategyType type() {
        return StrategyType.RANDOM;
    }

    public List<Integer[]> getStrategyGroups() {
        return this.strategyGroups;
    }

    public void setStrategyGroups(List<Integer[]> strategyGroups) {
        this.strategyGroups = strategyGroups;
    }

}
