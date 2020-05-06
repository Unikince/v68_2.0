package com.dmg.fish.business.strategy;

import java.util.List;

import com.dmg.fish.business.model.route.Position;

/**
 * 圆刷鱼策略
 */
public class CircleStrategy implements Strategy {
    /** 初始延迟(毫秒) */
    private int initDelay;
    /** 圆中心 */
    private Position center;
    /** 半径 */
    private int r;
    /** 最小数数量 */
    private int minNum;
    /** 最大数数量 */
    private int maxNum;
    /** 鱼的种类 */
    private List<Integer> fishs;
    /** 鱼的权重 */
    private List<Integer> weights;
    /** 鱼的权重和 */
    private int totalWeight;
    /** 每次刷出的鱼是否一样 */
    private boolean same;
    /** 路线 */
    private List<Integer> routes;

    @Override
    public StrategyType type() {
        return StrategyType.CIRCLE;
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
     * 获取：圆中心
     *
     * @return 圆中心
     */
    public Position getCenter() {
        return this.center;
    }

    /**
     * 设置：圆中心
     *
     * @param center 圆中心
     */
    public void setCenter(Position center) {
        this.center = center;
    }

    /**
     * 获取：半径
     *
     * @return 半径
     */
    public int getR() {
        return this.r;
    }

    /**
     * 设置：半径
     *
     * @param r 半径
     */
    public void setR(int r) {
        this.r = r;
    }

    /**
     * 获取：最小数数量
     *
     * @return 最小数数量
     */
    public int getMinNum() {
        return this.minNum;
    }

    /**
     * 设置：最小数数量
     *
     * @param minNum 最小数数量
     */
    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    /**
     * 获取：最大数数量
     *
     * @return 最大数数量
     */
    public int getMaxNum() {
        return this.maxNum;
    }

    /**
     * 设置：最大数数量
     *
     * @param maxNum 最大数数量
     */
    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
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
     * 获取：每次刷出的鱼是否一样
     *
     * @return 每次刷出的鱼是否一样
     */
    public boolean isSame() {
        return this.same;
    }

    /**
     * 设置：每次刷出的鱼是否一样
     *
     * @param same 每次刷出的鱼是否一样
     */
    public void setSame(boolean same) {
        this.same = same;
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
