package com.dmg.agentserver.business.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.agentserver.business.dao.AgentConfigDao;
import com.dmg.agentserver.business.model.po.AgentConfigPo;
import com.dmg.agentserviceapi.business.agentconfig.model.pojo.AgentConfig;

import cn.hutool.core.bean.BeanUtil;

/**
 * 代理配置
 */
@Service
public class AgentConfigService {
    @Autowired
    private AgentConfigDao dao;
    @Autowired
    private AgentSettleCalculateService agentSettleRecordCalculateService;

    private volatile AgentConfig cache = null;

    /**
     * 修改
     */
    public void update(AgentConfig obj) {
        this.cache = obj;
        Map<String, Object> map = BeanUtil.beanToMap(obj);
        List<AgentConfigPo> pos = new ArrayList<>();
        for (Entry<String, Object> entry : map.entrySet()) {
            AgentConfigPo po = new AgentConfigPo();
            String key = entry.getKey();
            String value = "";
            if (entry.getValue() != null) {
                value = entry.getValue().toString();
            }
            po.setConfigKey(key);
            po.setConfigValue(value);
            pos.add(po);
        }
        for (AgentConfigPo po : pos) {
            this.dao.update(po, new LambdaQueryWrapper<AgentConfigPo>().eq(AgentConfigPo::getConfigKey, po.getConfigKey()));
        }
        this.agentSettleRecordCalculateService.changeNextSettleTime();
    }

    /**
     * 获取
     */
    public AgentConfig get() {
        if (this.cache == null) {
            List<AgentConfigPo> pos = this.dao.selectList(new LambdaQueryWrapper<>());
            Map<String, Object> map = new HashMap<>();
            for (AgentConfigPo po : pos) {
                map.put(po.getConfigKey(), po.getConfigValue());
            }
            this.cache = BeanUtil.mapToBean(map, AgentConfig.class, false);
        }
        return this.cache;
    }
}