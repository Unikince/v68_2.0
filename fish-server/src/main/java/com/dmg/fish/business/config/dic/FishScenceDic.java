package com.dmg.fish.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.config.wrapper.FishScenceWrapper;
import com.dmg.gameconfigserverapi.fish.dto.FishScenceDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigFeign;

import lombok.extern.log4j.Log4j2;

/**
 * 捕鱼场景
 */
@Log4j2
@Component
public class FishScenceDic {
    @Autowired
    private FishConfigFeign feign;
    private List<FishScenceWrapper> list = new ArrayList<>();
    private Map<Integer, FishScenceWrapper> map = new HashMap<>();

    @PostConstruct
    public void load() {
        log.info("加载场景配置");
        List<FishScenceDTO> dtos = this.feign.getFishScence();
        this.list = FishScenceWrapper.convertList(dtos);
        Map<Integer, FishScenceWrapper> map = new HashMap<>();
        this.list.forEach(val -> map.put(val.getId(), val));
        this.map = map;
        log.info("加载场景配置完成");
    }

    public List<FishScenceWrapper> values() {
        return this.list;
    }

    public FishScenceWrapper get(int id) {
        return this.map.get(id);
    }
}
