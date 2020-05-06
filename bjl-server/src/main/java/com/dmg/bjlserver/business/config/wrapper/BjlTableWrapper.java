package com.dmg.bjlserver.business.config.wrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dmg.gameconfigserverapi.bjl.dto.BjlTableDTO;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 百家乐场次配置(json解析)
 */
public class BjlTableWrapper extends BjlTableDTO {
    /** 筹码列表 */
    private List<Long> chipList;
    /** 机器人下注权重:权重->筹码序号 */
    private TreeRangeMap<Integer, Integer> robotBetChipWeightMap;
    /** 机器人下注次数:权重->下注次数 */
    private TreeRangeMap<Integer, Integer> robotBetCountMap;
    /** 机器人下注区域:权重->区域 */
    private TreeRangeMap<Integer, Integer> robotBetAreaWeightMap;

    /** 集合转换 */
    public static List<BjlTableWrapper> convertList(List<BjlTableDTO> dtos) {
        List<BjlTableWrapper> result = new ArrayList<>();
        for (BjlTableDTO dto : dtos) {
            result.add(new BjlTableWrapper(dto));
        }
        return result;
    }

    public BjlTableWrapper(BjlTableDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.chipList = JSON.parseArray(dto.getChipJson(), Long.class);
        this.robotBetChipWeightMap = this.parseRate(dto.getRobotBetChipWeightJson(), "chipIndex");
        this.robotBetCountMap = this.parseRate(dto.getRobotBetCountJson(), "times");
        this.robotBetAreaWeightMap = this.parseRate(dto.getRobotBetAreaWeightJson(), "area");
    }

    /** 权重配置转TreeRangeMap */
    private TreeRangeMap<Integer, Integer> parseRate(String jsonArray, String value) {
        TreeRangeMap<Integer, Integer> result = TreeRangeMap.create();
        List<String> list = JSON.parseArray(jsonArray, String.class);
        int minRate = 0;
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = JSON.parseObject(list.get(i));
            Integer rate = Integer.parseInt(map.get("rate").toString());
            Integer v = Integer.parseInt(map.get(value).toString());
            rate += minRate;
            result.put(Range.closed(minRate, rate), v);
            minRate = rate;
        }
        return result;
    }

    /**
     * 随机获取机器人下注筹码
     */
    public long getRandomRobotBetChipValue() {
        int maxPoint = this.robotBetChipWeightMap.span().upperEndpoint();
        int randomRate = RandomUtil.randomInt(0, maxPoint + 1);
        int value = this.robotBetChipWeightMap.get(randomRate);
        return this.chipList.get(value);
    }

    /**
     * 随机获取机器人下注次数
     */
    public int getRandomRobotBetCount() {
        int maxPoint = this.robotBetCountMap.span().upperEndpoint();
        int randomRate = RandomUtil.randomInt(0, maxPoint + 1);
        return this.robotBetCountMap.get(randomRate);
    }

    /**
     * 随机获取机器人下注位置
     */
    public int getRandomRobotBetArea() {
        int maxPoint = this.robotBetAreaWeightMap.span().upperEndpoint();
        int randomRate = RandomUtil.randomInt(0, maxPoint + 1);
        return this.robotBetAreaWeightMap.get(randomRate);
    }

    /**
     * 获取：筹码列表
     *
     * @return 筹码列表
     */
    public List<Long> getChipList() {
        return this.chipList;
    }

    /**
     * 设置：筹码列表
     *
     * @param chipList 筹码列表
     */
    public void setChipList(List<Long> chipList) {
        this.chipList = chipList;
    }

    /**
     * 获取：机器人下注权重:权重->筹码序号
     *
     * @return 机器人下注权重:权重->筹码序号
     */
    public TreeRangeMap<Integer, Integer> getRobotBetChipWeightMap() {
        return this.robotBetChipWeightMap;
    }

    /**
     * 设置：机器人下注权重:权重->筹码序号
     *
     * @param robotBetChipWeightMap 机器人下注权重:权重->筹码序号
     */
    public void setRobotBetChipWeightMap(TreeRangeMap<Integer, Integer> robotBetChipWeightMap) {
        this.robotBetChipWeightMap = robotBetChipWeightMap;
    }

    /**
     * 获取：机器人下注次数:权重->下注次数
     *
     * @return 机器人下注次数:权重->下注次数
     */
    public TreeRangeMap<Integer, Integer> getRobotBetCountMap() {
        return this.robotBetCountMap;
    }

    /**
     * 设置：机器人下注次数:权重->下注次数
     *
     * @param robotBetCountMap 机器人下注次数:权重->下注次数
     */
    public void setRobotBetCountMap(TreeRangeMap<Integer, Integer> robotBetCountMap) {
        this.robotBetCountMap = robotBetCountMap;
    }

    /**
     * 获取：机器人下注区域:权重->区域
     *
     * @return 机器人下注区域:权重->区域
     */
    public TreeRangeMap<Integer, Integer> getRobotBetAreaWeightMap() {
        return this.robotBetAreaWeightMap;
    }

    /**
     * 设置：机器人下注区域:权重->区域
     *
     * @param robotBetAreaWeightMap 机器人下注区域:权重->区域
     */
    public void setRobotBetAreaWeightMap(TreeRangeMap<Integer, Integer> robotBetAreaWeightMap) {
        this.robotBetAreaWeightMap = robotBetAreaWeightMap;
    }

}
