package com.dmg.fish.business.strategy;

import java.util.List;

import com.dmg.fish.business.model.route.Position;

/**
 * 矩形范围刷鱼策略
 */
public class RectangleStrategy implements Strategy {
    /** 初始延迟(毫秒) */
    private int initDelay;
    /** 刷鱼间隔(毫秒) */
    private int delay;
    /** 次数 */
    private int times;
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
    private boolean sameFish;
    /** 每次刷出的鱼的路径是否一样 */
    private boolean sameRoute;
    /** 路线 */
    private List<Integer> routes;
    /** 刷鱼起点 */
    private Position start;
    /** 刷鱼起点x方向偏差 */
    private int[] deviateX = new int[0];
    /** 刷鱼起点y方向偏差 */
    private int[] deviateY = new int[0];

    @Override
    public StrategyType type() {
        return StrategyType.RECTANGLE;
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
    public boolean isSameFish() {
        return this.sameFish;
    }

    /**
     * 设置：每次刷出的鱼是否一样
     * 
     * @param sameFish 每次刷出的鱼是否一样
     */
    public void setSameFish(boolean sameFish) {
        this.sameFish = sameFish;
    }

    /**
     * 获取：每次刷出的鱼的路径是否一样
     * 
     * @return 每次刷出的鱼的路径是否一样
     */
    public boolean isSameRoute() {
        return this.sameRoute;
    }

    /**
     * 设置：每次刷出的鱼的路径是否一样
     * 
     * @param sameRoute 每次刷出的鱼的路径是否一样
     */
    public void setSameRoute(boolean sameRoute) {
        this.sameRoute = sameRoute;
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
     * 获取：刷鱼起点
     * 
     * @return 刷鱼起点
     */
    public Position getStart() {
        return this.start;
    }

    /**
     * 设置：刷鱼起点
     * 
     * @param start 刷鱼起点
     */
    public void setStart(Position start) {
        this.start = start;
    }

    /**
     * 获取：刷鱼起点x方向偏差
     * 
     * @return 刷鱼起点x方向偏差
     */
    public int[] getDeviateX() {
        int[] deviateX = new int[this.deviateX.length];
        for (int i = 0; i < deviateX.length; i++) {
            deviateX[i] = this.deviateX[i];
        }
        return deviateX;
    }

    /**
     * 设置：刷鱼起点x方向偏差
     * 
     * @param deviateX 刷鱼起点x方向偏差
     */
    public void setDeviateX(int[] deviateX) {
        this.deviateX = new int[deviateX.length];
        for (int i = 0; i < this.deviateX.length; i++) {
            this.deviateX[i] = deviateX[i];
        }
    }

    /**
     * 获取：刷鱼起点y方向偏差
     * 
     * @return 刷鱼起点y方向偏差
     */
    public int[] getDeviateY() {
        int[] deviateY = new int[this.deviateY.length];
        for (int i = 0; i < deviateY.length; i++) {
            deviateY[i] = this.deviateY[i];
        }
        return deviateY;
    }

    /**
     * 设置：刷鱼起点y方向偏差
     * 
     * @param deviateY 刷鱼起点y方向偏差
     */
    public void setDeviateY(int[] deviateY) {
        this.deviateY = new int[deviateY.length];
        for (int i = 0; i < this.deviateY.length; i++) {
            this.deviateY[i] = deviateY[i];
        }
    }
}
