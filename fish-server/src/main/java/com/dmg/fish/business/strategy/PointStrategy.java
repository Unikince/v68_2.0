package com.dmg.fish.business.strategy;

import java.util.List;

import com.dmg.fish.business.model.route.Position;

/**
 * 点刷鱼
 */
public class PointStrategy implements Strategy {
    /** 初始延迟(毫秒) */
    private int initDelay;
    /** 刷鱼间隔(毫秒) */
    private int delay;
    /** 次数 */
    private int times;
    /** 点位置 */
    private Position pos;
    /** 鱼的种类 */
    private List<Integer> fishs;
    /** 鱼的权重 */
    private List<Integer> weights;
    /** 鱼的权重和 */
    private int totalWeight;
    /** 路线 */
    private List<Integer> routes;

    @Override
    public StrategyType type() {
        return StrategyType.POINT;
    }

    /**
     * 获取：初始延迟(毫秒)
     *
     * @return 初始延迟(毫秒)
     */
    public int getInitDelay() {
        return this.initDelay;
    }

    /**
     * 设置：初始延迟(毫秒)
     *
     * @param initDelay 初始延迟(毫秒)
     */
    public void setInitDelay(int initDelay) {
        this.initDelay = initDelay;
    }

    /**
     * 获取：刷鱼间隔(毫秒)
     *
     * @return 刷鱼间隔(毫秒)
     */
    public int getDelay() {
        return this.delay;
    }

    /**
     * 设置：刷鱼间隔(毫秒)
     *
     * @param delay 刷鱼间隔(毫秒)
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }

    /**
     * 获取：次数
     *
     * @return 次数
     */
    public int getTimes() {
        return this.times;
    }

    /**
     * 设置：次数
     *
     * @param times 次数
     */
    public void setTimes(int times) {
        this.times = times;
    }

    /**
     * 获取：点位置
     *
     * @return 点位置
     */
    public Position getPos() {
        return this.pos;
    }

    /**
     * 设置：点位置
     *
     * @param pos 点位置
     */
    public void setPos(Position pos) {
        this.pos = pos;
    }

    /**
     * 获取：鱼的种类
     *
     * @return 鱼的种类
     */
    public List<Integer> getFishs() {
        return this.fishs;
    }

    /**
     * 设置：鱼的种类
     *
     * @param fishs 鱼的种类
     */
    public void setFishs(List<Integer> fishs) {
        this.fishs = fishs;
    }

    /**
     * 获取：鱼的权重和
     *
     * @return 鱼的权重和
     */
    public int getTotalWeight() {
        return this.totalWeight;
    }

    /**
     * 设置：鱼的权重和
     *
     * @param totalWeight 鱼的权重和
     */
    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    /**
     * 获取：路线
     *
     * @return 路线
     */
    public List<Integer> getRoutes() {
        return this.routes;
    }

    /**
     * 设置：路线
     *
     * @param routes 路线
     */
    public void setRoutes(List<Integer> routes) {
        this.routes = routes;
    }

    /**
     * 获取：鱼的权重
     *
     * @return 鱼的权重
     */
    public List<Integer> getWeights() {
        return this.weights;
    }

    /**
     * 设置：鱼的权重
     *
     * @param weights 鱼的权重
     */
    public void setWeights(List<Integer> weights) {
        this.weights = weights;
        int totalWeight = 0;
        for (Integer weight : weights) {
            totalWeight += weight;
        }
        this.totalWeight = totalWeight;
    }

}
