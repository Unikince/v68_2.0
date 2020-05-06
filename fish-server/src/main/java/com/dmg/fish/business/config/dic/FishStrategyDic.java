package com.dmg.fish.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.config.wrapper.FishStrategyWrapper;
import com.dmg.gameconfigserverapi.fish.dto.FishStrategyDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigFeign;

import lombok.extern.log4j.Log4j2;

/**
 * 捕鱼刷鱼策略
 */
@Log4j2
@Component
public class FishStrategyDic {
    @Autowired
    private FishConfigFeign feign;
    private List<FishStrategyWrapper> list = new ArrayList<>();
    private Map<Integer, FishStrategyWrapper> map = new HashMap<>();

    @PostConstruct
    public void load() {
        log.info("加载刷鱼策略配置");
        List<FishStrategyDTO> dtos = this.feign.getFishStrategy();
        this.list = FishStrategyWrapper.convertList(dtos);
        Map<Integer, FishStrategyWrapper> map = new HashMap<>();
        this.list.forEach(val -> map.put(val.getId(), val));
        this.map = map;
        log.info("加载刷鱼策略配置完成");
    }

    public List<FishStrategyWrapper> values() {
        return this.list;
    }

    public FishStrategyWrapper get(int id) {
        return this.map.get(id);
    }
}
