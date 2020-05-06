package com.dmg.fish.business.config.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dmg.gameconfigserverapi.fish.dto.FishStrategyDTO;

import cn.hutool.core.bean.BeanUtil;

/**
 * 捕鱼刷鱼策略(json解析)
 */
public class FishStrategyWrapper extends FishStrategyDTO {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 鱼数据 */
    private List<Integer> fishsData;
    /** 权重数据 */
    private List<Integer> weightsData;
    /** 合计权重 */
    private Integer totalWeight;
    /** 路径 */
    private List<Integer> routesData;
    /** 刷鱼起点 */
    private Integer[] startData;
    /** 随机策略组刷鱼配置 */
    private List<Integer[]> groupsData;

    /** 集合转换 */
    public static List<FishStrategyWrapper> convertList(List<FishStrategyDTO> dtos) {
        List<FishStrategyWrapper> result = new ArrayList<>();
        for (FishStrategyDTO dto : dtos) {
            result.add(new FishStrategyWrapper(dto));
        }
        return result;
    }

    /** 构建方法 */
    public FishStrategyWrapper(FishStrategyDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.fishsData = JSON.parseArray(dto.getFishs(), Integer.class);
        this.weightsData = JSON.parseArray(dto.getWeights(), Integer.class);
        this.routesData = JSON.parseArray(dto.getRoutes(), Integer.class);
        this.startData = JSON.parseObject(dto.getStart(), Integer[].class);
        this.groupsData = JSON.parseArray(dto.getGroupData(), Integer[].class);

        this.totalWeight = 0;
        for (Integer weight : this.weightsData) {
            this.totalWeight += weight;
        }
    }

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * 获取：鱼数据
     *
     * @return 鱼数据
     */
    public List<Integer> getFishsData() {
        return this.fishsData;
    }

    /**
     * 获取：权重数据
     *
     * @return 权重数据
     */
    public List<Integer> getWeightsData() {
        return this.weightsData;
    }

    /**
     * 获取：合计权重
     *
     * @return 合计权重
     */
    public Integer getTotalWeight() {
        return this.totalWeight;
    }

    /**
     * 获取：路径
     *
     * @return 路径
     */
    public List<Integer> getRoutesData() {
        return this.routesData;
    }

    /**
     * 获取：刷鱼起点
     *
     * @return 刷鱼起点
     */
    public Integer[] getStartData() {
        return this.startData;
    }

    /**
     * 获取：随机策略组刷鱼配置
     *
     * @return 随机策略组刷鱼配置
     */
    public List<Integer[]> getGroupsData() {
        return this.groupsData;
    }

}
