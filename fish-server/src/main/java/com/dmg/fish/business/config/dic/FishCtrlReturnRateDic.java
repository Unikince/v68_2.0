package com.dmg.fish.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigFeign;
import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;

import lombok.extern.log4j.Log4j2;

/**
 * 捕鱼返奖率控制
 */
@Log4j2
@Component
public class FishCtrlReturnRateDic {
    @Autowired
    private FishConfigFeign feign;
    private List<FishCtrlReturnRateDTO> list = new ArrayList<>();
    private Map<Integer, FishCtrlReturnRateDTO> map = new HashMap<>();
    /** 返奖率区间map,闭区间做key */
    private TreeRangeMap<Long, FishCtrlReturnRateDTO> rangeMap = TreeRangeMap.create();

    @PostConstruct
    public void load() {
        log.info("加载捕鱼返奖率控制");
        this.list = this.feign.getFishCtrlReturnRate();
        Map<Integer, FishCtrlReturnRateDTO> map = new HashMap<>();
        this.list.forEach(val -> map.put(val.getId(), val));
        this.map = map;
        TreeRangeMap<Long, FishCtrlReturnRateDTO> rangeMap = TreeRangeMap.create();
        this.list.forEach(val -> rangeMap.put(Range.closed(val.getMinReturnRate(), val.getMaxReturnRate()), val));
        this.rangeMap = rangeMap;
        log.info("加载捕鱼返奖率控制完成");
    }

    public List<FishCtrlReturnRateDTO> values() {
        return this.list;
    }

    public FishCtrlReturnRateDTO get(int id) {
        return this.map.get(id);
    }

    /**
     * 通过返奖率获取模型
     */
    public FishCtrlReturnRateDTO getByReturnRate(long returnRate) {
        FishCtrlReturnRateDTO dto = this.rangeMap.get(returnRate);
        if (dto == null) {
            dto = this.map.get(1);
        }
        return dto;
    }

}
