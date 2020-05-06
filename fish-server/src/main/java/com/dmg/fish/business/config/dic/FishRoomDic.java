package com.dmg.fish.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.fish.business.config.wrapper.FishRoomWrapper;
import com.dmg.gameconfigserverapi.fish.dto.FishRoomDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigFeign;

import lombok.extern.log4j.Log4j2;

/**
 * 捕鱼房间配置
 */
@Log4j2
@Component
public class FishRoomDic {
    @Autowired
    private FishConfigFeign feign;
    private List<FishRoomWrapper> list = new ArrayList<>();
    private Map<Integer, FishRoomWrapper> map = new HashMap<>();

    @PostConstruct
    public void load() {
        log.info("加载房间配置");
        List<FishRoomDTO> dtos = this.feign.getFishRoom();
        this.list = FishRoomWrapper.convertList(dtos);
        Map<Integer, FishRoomWrapper> map = new HashMap<>();
        this.list.forEach(val -> map.put(val.getId(), val));
        this.map = map;
        log.info("加载房间配置完成");
    }

    public List<FishRoomWrapper> values() {
        return this.list;
    }

    public FishRoomWrapper get(int id) {
        return this.map.get(id);
    }
}
