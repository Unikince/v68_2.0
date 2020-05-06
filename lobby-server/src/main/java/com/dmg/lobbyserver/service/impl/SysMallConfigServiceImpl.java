package com.dmg.lobbyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SysMallConfigDao;
import com.dmg.lobbyserver.dao.bean.SysMallConfigBean;
import com.dmg.lobbyserver.model.dto.SysMallConfigDTO;
import com.dmg.lobbyserver.service.SysMallConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 15:59
 * @Version V1.0
 **/
@Service
public class SysMallConfigServiceImpl implements SysMallConfigService {

    @Autowired
    private SysMallConfigDao sysMallConfigDao;

    @Override
    public List<SysMallConfigDTO> getSysMallConfig() {
        return sysMallConfigDao.selectSysMallConfig();
    }

    @Override
    public SysMallConfigBean getSysMallConfigById(Long id) {
        return sysMallConfigDao.selectById(id);
    }

    @Override
    public SysMallConfigBean getSysMallConfigByThirdId(String thirdId) {
        return sysMallConfigDao.selectOne(new LambdaQueryWrapper<SysMallConfigBean>().eq(SysMallConfigBean::getThirdId,thirdId));
    }
}