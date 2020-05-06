package com.dmg.agentserver.business.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.agentserver.business.dao.AgentLevelDao;
import com.dmg.agentserver.business.model.po.AgentLevelPo;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.agentlevel.model.pojo.AgentLevel;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.agentserviceapi.core.page.PageReq;
import com.dmg.agentserviceapi.core.page.PageRes;

import cn.hutool.core.bean.BeanUtil;

/**
 * 代理等级
 */
@Service
public class AgentLevelService {
    @Autowired
    private AgentLevelDao dao;

    /**
     * 添加
     */
    public void add(AgentLevel obj) {
        AgentLevelPo po = new AgentLevelPo();
        BeanUtil.copyProperties(obj, po);
        this.dao.insert(po);
        this.afreshSort();
    }

    /**
     * 修改
     */
    public void update(AgentLevel obj) {
        AgentLevelPo po = this.dao.selectById(obj.getId());
        if (po == null) {
            throw BusinessException.create(CommErrorEnum.OBJ_NOT_EXIST, "" + obj.getId());
        }
        BeanUtil.copyProperties(obj, po);
        this.dao.updateById(po);
        this.afreshSort();
    }

    /**
     * 删除
     */
    public void delete(long id) {
        this.dao.deleteById(id);
        this.afreshSort();
    }

    /**
     * 获取所有对象
     */
    public PagePackageRes<List<AgentLevel>> getList(PageReq pageReq) {
        if (pageReq == null) {
            pageReq = PageReq.maxPage();
        }

        IPage<AgentLevelPo> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        LambdaQueryWrapper<AgentLevelPo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(AgentLevelPo::getSort);
        IPage<AgentLevelPo> pagePos = this.dao.selectPage(page, wrapper);

        List<AgentLevel> data = new ArrayList<>();
        for (AgentLevelPo po : pagePos.getRecords()) {
            data.add(po);
        }

        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        PagePackageRes<List<AgentLevel>> result = new PagePackageRes<>();
        result.setData(data);
        result.setPage(pageRes);
        return result;
    }

    /**
     * 重新排序
     */
    private void afreshSort() {
        List<AgentLevelPo> pos = this.dao.selectList(new LambdaQueryWrapper<AgentLevelPo>().orderByAsc(AgentLevelPo::getPerformanceBegin));
        for (int i = 1; i <= pos.size(); i++) {
            AgentLevelPo po = pos.get(i - 1);
            if (po.getSort() == i) {
                continue;
            }
            po.setSort(i);
            this.dao.updateById(po);
        }
    }
}