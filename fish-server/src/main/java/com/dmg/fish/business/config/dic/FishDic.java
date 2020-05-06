package com.dmg.fish.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.gameconfigserverapi.fish.dto.FishDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigFeign;

import lombok.extern.log4j.Log4j2;

/**
 * 捕鱼-鱼
 */
@Log4j2
@Component
public class FishDic {
    @Autowired
    private FishConfigFeign feign;
    private List<FishDTO> list = new ArrayList<>();
    private Map<Integer, FishDTO> map = new HashMap<>();

    @PostConstruct
    public void load() {
        log.info("加载鱼配置");
        this.list = this.feign.getFish();
        Map<Integer, FishDTO> map = new HashMap<>();
        this.list.forEach(val -> map.put(val.getId(), val));
        this.map = map;
        log.info("加载鱼配置完成");
    }

    public List<FishDTO> values() {
        return this.list;
    }

    public FishDTO get(int id) {
        return this.map.get(id);
    }
}
