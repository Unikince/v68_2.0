package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.config.FileBaseConfigDao;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.service.config.FileBaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("fileBaseConfigService")
public class FileBaseConfigServiceImpl implements FileBaseConfigService {

    @Autowired
    private FileBaseConfigDao fileBaseConfigDao;

    @Override
    public List<FileBaseConfigBean> getBaseConfigByGameId(Integer gameId) {
        return fileBaseConfigDao.selectList(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getOpenStatus, 1)
                .eq(FileBaseConfigBean::getGameId, gameId));
    }
}
