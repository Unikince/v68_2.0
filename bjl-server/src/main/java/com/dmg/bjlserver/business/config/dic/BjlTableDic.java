package com.dmg.bjlserver.business.config.dic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.config.wrapper.BjlTableWrapper;
import com.dmg.gameconfigserverapi.bjl.dto.BjlTableDTO;
import com.dmg.gameconfigserverapi.bjl.feign.BjlConfigFeign;

import lombok.extern.log4j.Log4j2;

/**
 * 百家乐场次配置
 */
@Log4j2
@Component
public class BjlTableDic {
    @Autowired
    private BjlConfigFeign feign;
    private List<BjlTableWrapper> list = new ArrayList<>();
    private Map<Integer, BjlTableWrapper> map = new HashMap<>();

    @PostConstruct
    public void load() {
        log.info("加载场次配置");
        List<BjlTableDTO> dtos = this.feign.getBjlTable();
        this.list = BjlTableWrapper.convertList(dtos);
        this.list.forEach(val -> this.map.put(val.getId(), val));
        log.info("加载场次配置完成");
    }

    public List<BjlTableWrapper> values() {
        return this.list;
    }

    public BjlTableWrapper get(int id) {
        return this.map.get(id);
    }
}
