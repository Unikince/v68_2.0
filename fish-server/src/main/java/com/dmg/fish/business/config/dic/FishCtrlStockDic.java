package com.dmg.fish.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.gameconfigserverapi.fish.dto.FishCtrlStockDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigFeign;

import cn.hutool.json.JSONUtil;
import lombok.extern.log4j.Log4j2;

/**
 * 捕鱼库存控制
 */
@Log4j2
@Component
public class FishCtrlStockDic {
    @Autowired
    private FishConfigFeign feign;
    private List<FishCtrlStockDTO> list = new ArrayList<>();
    private Map<Integer, FishCtrlStockDTO> map = new HashMap<>();

    @PostConstruct
    public void load() {
        log.info("加载捕鱼库存控制");
        this.list = this.feign.getFishCtrlStock();
        Map<Integer, FishCtrlStockDTO> map = new HashMap<>();
        this.list.forEach(val -> map.put(val.getId(), val));
        this.map = map;
        log.info("加载捕鱼库存控制完成");
    }

    /**
     * 更新库存值
     *
     * @param params key为房间id，value为修改的分数
     */
    public void update(Map<String, String> params) {
        this.feign.updateFishCtrlStock(JSONUtil.toJsonStr(params));
    }

    public List<FishCtrlStockDTO> values() {
        return this.list;
    }

    public FishCtrlStockDTO get(int id) {
        return this.map.get(id);
    }
}
