package com.dmg.agentserver.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.agentserver.business.dao.PerformanceConfigDao;
import com.dmg.agentserver.business.model.po.PerformanceConfigPo;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.performanceconfig.model.pojo.PerformanceConfig;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.agentserviceapi.core.page.PageReq;
import com.dmg.agentserviceapi.core.page.PageRes;

import cn.hutool.core.bean.BeanUtil;

/**
 * 业绩配置
 */
@Service
public class PerformanceConfigService {
    @Autowired
    private PerformanceConfigDao dao;

    /**
     * 修改
     */
    public void update(PerformanceConfig obj) {
        PerformanceConfigPo po = this.dao.selectById(obj.getId());
        if (po == null) {
            throw BusinessException.create(CommErrorEnum.OBJ_NOT_EXIST, "" + obj.getId());
        }
        po.setRatio(obj.getRatio());
        this.dao.updateById(po);
    }

    /**
     * 获取所有对象
     */
    public PagePackageRes<List<PerformanceConfig>> getList(PageReq pageReq) {
        IPage<PerformanceConfigPo> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        LambdaQueryWrapper<PerformanceConfigPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PerformanceConfigPo::getId);
        IPage<PerformanceConfigPo> pagePos = this.dao.selectPage(page, wrapper);

        List<PerformanceConfig> data = new ArrayList<>();
        for (PerformanceConfigPo po : pagePos.getRecords()) {
            data.add(po);
        }

        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        PagePackageRes<List<PerformanceConfig>> result = new PagePackageRes<>();
        result.setData(data);
        result.setPage(pageRes);
        return result;
    }

    /**
     * 获取所有对象
     */
    public List<PerformanceConfig> getList() {
        LambdaQueryWrapper<PerformanceConfigPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(PerformanceConfigPo::getId);
        List<PerformanceConfigPo> pagePos = this.dao.selectList(wrapper);
        List<PerformanceConfig> data = new ArrayList<>();
        for (PerformanceConfigPo po : pagePos) {
            data.add(po);
        }
        return data;
    }

}