package com.dmg.fish.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.config.wrapper.FishRouteWrapper;
import com.dmg.gameconfigserverapi.fish.dto.FishRouteDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigFeign;

import lombok.extern.log4j.Log4j2;

/**
 * 捕鱼-鱼路线图
 */
@Log4j2
@Component
public class FishRouteDic {
    @Autowired
    private FishConfigFeign feign;
    private List<FishRouteWrapper> list = new ArrayList<>();
    private Map<Integer, FishRouteWrapper> map = new HashMap<>();

    @PostConstruct
    public void load() {
        log.info("加载鱼路线图配置");
        List<FishRouteDTO> dtos = this.feign.getFishRoute();
        this.list = FishRouteWrapper.convertList(dtos);
        Map<Integer, FishRouteWrapper> map = new HashMap<>();
        this.list.forEach(val -> map.put(val.getId(), val));
        this.map = map;
        log.info("加载鱼路线图配置完成");
    }

    public List<FishRouteWrapper> values() {
        return this.list;
    }

    public FishRouteWrapper get(int id) {
        return this.map.get(id);
    }
}
