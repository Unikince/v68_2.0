package com.dmg.gameconfigserverapi.fish.dto;

import java.io.Serializable;

/**
 * 捕鱼刷鱼策略
 */
public class FishStrategyDTO implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 策略id */
    private Integer id;
    /** 类型(1:矩形范围刷鱼,2:点刷鱼,3:圆刷鱼,4:随机策略组刷鱼 */
    private Integer type;
    /** 初始延迟(毫秒) */
    private Integer initDelay;
    /** 刷鱼间隔(毫秒) */
    private Integer delay;
    /** 刷鱼次数 */
    private Integer times;
    /** 每次刷鱼最小数数量 */
    private Integer minNum;
    /** 每次刷鱼最大数数量 */
    private Integer maxNum;
    /** 最小速度 */
    private Integer minSpeed;
    /** 最大速度 */
    private Integer maxSpeed;
    /** 鱼的种类 */
    private String fishs;
    /** 鱼之间的间隔(毫秒) */
    private Integer fishDelay;
    /** 鱼的权重 */
    private String weights;
    /** 每次刷出的鱼是否一样 */
    private boolean sameFish;
    /** 每次刷出的鱼的路径是否一样 */
    private boolean sameRoute;
    /** 路径 */
    private String routes;
    /** 刷鱼起点 */
    private String start;
    /** 矩形范围刷鱼刷鱼起点x方向偏差 */
    private Integer deviateX;
    /** 矩形范围刷鱼刷鱼起点y方向偏差 */
    private Integer deviateY;
    /** 圆刷鱼半径 */
    private Integer radius;
    /** 随机策略组刷鱼配置 */
    private String groupData;
    /** 描述 */
    private String description;

    /**
     * 获取：策略id
     *
     * @return 策略id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：策略id
     *
     * @param id 策略id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：类型(1:矩形范围刷鱼,2:点刷鱼,3:圆刷鱼,4:随机策略组刷鱼
     *
     * @return 类型(1:矩形范围刷鱼,2:点刷鱼,3:圆刷鱼,4:随机策略组刷鱼
     */
    public Integer getType() {
        return this.type;
    }

    /**
     * 设置：类型(1:矩形范围刷鱼,2:点刷鱼,3:圆刷鱼,4:随机策略组刷鱼
     *
     * @param type 类型(1:矩形范围刷鱼,2:点刷鱼,3:圆刷鱼,4:随机策略组刷鱼
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取：初始延迟(毫秒)
     *
     * @return 初始延迟(毫秒)
     */
    public Integer getInitDelay() {
        return this.initDelay;
    }

    /**
     * 设置：初始延迟(毫秒)
     *
     * @param initDelay 初始延迟(毫秒)
     */
    public void setInitDelay(Integer initDelay) {
        this.initDelay = initDelay;
    }

    /**
     * 获取：刷鱼间隔(毫秒)
     *
     * @return 刷鱼间隔(毫秒)
     */
    public Integer getDelay() {
        return this.delay;
    }

    /**
     * 设置：刷鱼间隔(毫秒)
     *
     * @param delay 刷鱼间隔(毫秒)
     */
    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    /**
     * 获取：刷鱼次数
     *
     * @return 刷鱼次数
     */
    public Integer getTimes() {
        return this.times;
    }

    /**
     * 设置：刷鱼次数
     *
     * @param times 刷鱼次数
     */
    public void setTimes(Integer times) {
        this.times = times;
    }

    /**
     * 获取：每次刷鱼最小数数量
     *
     * @return 每次刷鱼最小数数量
     */
    public Integer getMinNum() {
        return this.minNum;
    }

    /**
     * 设置：每次刷鱼最小数数量
     *
     * @param minNum 每次刷鱼最小数数量
     */
    public void setMinNum(Integer minNum) {
        this.minNum = minNum;
    }

    /**
     * 获取：每次刷鱼最大数数量
     *
     * @return 每次刷鱼最大数数量
     */
    public Integer getMaxNum() {
        return this.maxNum;
    }

    /**
     * 设置：每次刷鱼最大数数量
     *
     * @param maxNum 每次刷鱼最大数数量
     */
    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    /**
     * 获取：最小速度
     *
     * @return 最小速度
     */
    public Integer getMinSpeed() {
        return this.minSpeed;
    }

    /**
     * 设置：最小速度
     *
     * @param minSpeed 最小速度
     */
    public void setMinSpeed(Integer minSpeed) {
        this.minSpeed = minSpeed;
    }

    /**
     * 获取：最大速度
     *
     * @return 最大速度
     */
    public Integer getMaxSpeed() {
        return this.maxSpeed;
    }

    /**
     * 设置：最大速度
     *
     * @param maxSpeed 最大速度
     */
    public void setMaxSpeed(Integer maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * 获取：鱼的种类
     *
     * @return 鱼的种类
     */
    public String getFishs() {
        return this.fishs;
    }

    /**
     * 设置：鱼的种类
     *
     * @param fishs 鱼的种类
     */
    public void setFishs(String fishs) {
        this.fishs = fishs;
    }

    /**
     * 获取：鱼之间的间隔(毫秒)
     *
     * @return 鱼之间的间隔(毫秒)
     */
    public Integer getFishDelay() {
        return this.fishDelay;
    }

    /**
     * 设置：鱼之间的间隔(毫秒)
     *
     * @param fishDelay 鱼之间的间隔(毫秒)
     */
    public void setFishDelay(Integer fishDelay) {
        this.fishDelay = fishDelay;
    }

    /**
     * 获取：鱼的权重
     *
     * @return 鱼的权重
     */
    public String getWeights() {
        return this.weights;
    }

    /**
     * 设置：鱼的权重
     *
     * @param weights 鱼的权重
     */
    public void setWeights(String weights) {
        this.weights = weights;
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
     * 获取：路径
     *
     * @return 路径
     */
    public String getRoutes() {
        return this.routes;
    }

    /**
     * 设置：路径
     *
     * @param routes 路径
     */
    public void setRoutes(String routes) {
        this.routes = routes;
    }

    /**
     * 获取：刷鱼起点
     *
     * @return 刷鱼起点
     */
    public String getStart() {
        return this.start;
    }

    /**
     * 设置：刷鱼起点
     *
     * @param start 刷鱼起点
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * 获取：矩形范围刷鱼刷鱼起点x方向偏差
     *
     * @return 矩形范围刷鱼刷鱼起点x方向偏差
     */
    public Integer getDeviateX() {
        return this.deviateX;
    }

    /**
     * 设置：矩形范围刷鱼刷鱼起点x方向偏差
     *
     * @param deviateX 矩形范围刷鱼刷鱼起点x方向偏差
     */
    public void setDeviateX(Integer deviateX) {
        this.deviateX = deviateX;
    }

    /**
     * 获取：矩形范围刷鱼刷鱼起点y方向偏差
     *
     * @return 矩形范围刷鱼刷鱼起点y方向偏差
     */
    public Integer getDeviateY() {
        return this.deviateY;
    }

    /**
     * 设置：矩形范围刷鱼刷鱼起点y方向偏差
     *
     * @param deviateY 矩形范围刷鱼刷鱼起点y方向偏差
     */
    public void setDeviateY(Integer deviateY) {
        this.deviateY = deviateY;
    }

    /**
     * 获取：圆刷鱼半径
     *
     * @return 圆刷鱼半径
     */
    public Integer getRadius() {
        return this.radius;
    }

    /**
     * 设置：圆刷鱼半径
     *
     * @param radius 圆刷鱼半径
     */
    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    /**
     * 获取：随机策略组刷鱼配置
     *
     * @return 随机策略组刷鱼配置
     */
    public String getGroupData() {
        return this.groupData;
    }

    /**
     * 设置：随机策略组刷鱼配置
     *
     * @param groupData 随机策略组刷鱼配置
     */
    public void setGroupData(String groupData) {
        this.groupData = groupData;
    }

    /**
     * 获取：描述
     *
     * @return 描述
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置：描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
