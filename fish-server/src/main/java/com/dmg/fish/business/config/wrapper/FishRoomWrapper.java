package com.dmg.fish.business.config.wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import com.alibaba.fastjson.JSON;
import com.dmg.gameconfigserverapi.fish.dto.FishRoomDTO;

import cn.hutool.core.bean.BeanUtil;

/**
 * 捕鱼房间配置(json解析)
 */
public class FishRoomWrapper extends FishRoomDTO {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 捕鱼炮分数 */
    private final TreeSet<Integer> batteryScoresTree;
    /** 捕鱼炮分数 */
    private final ArrayList<Integer> batteryScoresList;
    /** 场景数据 */
    private final List<Integer> scencesData;

    /** 集合转换 */
    public static List<FishRoomWrapper> convertList(List<FishRoomDTO> dtos) {
        List<FishRoomWrapper> result = new ArrayList<>();
        for (FishRoomDTO dto : dtos) {
            result.add(new FishRoomWrapper(dto));
        }
        return result;
    }

    /** 构建方法 */
    public FishRoomWrapper(FishRoomDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.batteryScoresTree = new TreeSet<>(JSON.parseArray(dto.getBatteryScores(), Integer.class));
        this.batteryScoresList = new ArrayList<>(this.batteryScoresTree);
        Collections.sort(this.batteryScoresList);

        this.scencesData = JSON.parseArray(dto.getScences(), Integer.class);
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
     * 获取：捕鱼炮分数
     *
     * @return 捕鱼炮分数
     */
    public TreeSet<Integer> getBatteryScoresTree() {
        return this.batteryScoresTree;
    }

    /**
     * 获取：捕鱼炮分数
     *
     * @return 捕鱼炮分数
     */
    public ArrayList<Integer> getBatteryScoresList() {
        return this.batteryScoresList;
    }

    /**
     * 获取：场景数据
     *
     * @return 场景数据
     */
    public List<Integer> getScencesData() {
        return this.scencesData;
    }

}
