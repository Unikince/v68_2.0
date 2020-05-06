package com.dmg.gameconfigserver.service.financing.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.finance.FinancePropertiesDao;
import com.dmg.gameconfigserver.model.bean.finance.FinancePropertiesBean;
import com.dmg.gameconfigserver.model.vo.finance.FinancePropertiesVo;
import com.dmg.gameconfigserver.service.financing.FinancePropertiesService;

import cn.hutool.core.bean.BeanUtil;

/**
 * 财务属性
 */
@Service
public class FinancePropertiesServiceImpl implements FinancePropertiesService {
    @Autowired
    private FinancePropertiesDao dao;

    @Override
    public void updateOne(FinancePropertiesVo vo) {
        Map<String, Object> map = BeanUtil.beanToMap(vo);
        List<FinancePropertiesBean> beans = new ArrayList<>();
        for (Entry<String, Object> entry : map.entrySet()) {
            FinancePropertiesBean bean = new FinancePropertiesBean();
            String key = entry.getKey();
            String value = "";
            if (entry.getValue() != null) {
                value = entry.getValue().toString();
            }
            bean.setConfigKey(key);
            bean.setConfigValue(value);
            beans.add(bean);
        }
        for (FinancePropertiesBean bean : beans) {
            this.dao.update(bean, new LambdaQueryWrapper<FinancePropertiesBean>().eq(FinancePropertiesBean::getConfigKey, bean.getConfigKey()));
        }
    }

    @Override
    public FinancePropertiesVo get() {
        List<FinancePropertiesBean> beans = this.dao.selectList(null);
        Map<String, Object> map = new HashMap<>();
        for (FinancePropertiesBean bean : beans) {
            map.put(bean.getConfigKey(), bean.getConfigValue());
        }
        return BeanUtil.mapToBean(map, FinancePropertiesVo.class, false);
    }
}
