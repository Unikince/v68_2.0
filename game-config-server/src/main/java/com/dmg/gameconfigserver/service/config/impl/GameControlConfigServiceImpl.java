package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.config.GameControlConfigDao;
import com.dmg.gameconfigserver.model.bean.config.GameControlBean;
import com.dmg.gameconfigserver.model.dto.config.GameControlConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.GameControlVO;
import com.dmg.gameconfigserver.service.config.GameControlConfigService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:17 2019/10/11
 */
@Service
public class GameControlConfigServiceImpl implements GameControlConfigService {

    @Autowired
    private GameControlConfigDao gameControlConfigDao;

    @Override
    public GameControlConfigDTO getGameControlByBaseId(Integer baseId) {
        GameControlBean gameControlBean = gameControlConfigDao.selectOne(new LambdaQueryWrapper<GameControlBean>()
                .eq(GameControlBean::getFileBaseConfigId, baseId));
        if (gameControlBean == null) {
            return null;
        }
        GameControlConfigDTO gameControlConfigDTO = new GameControlConfigDTO();
        DmgBeanUtils.copyProperties(gameControlBean, gameControlConfigDTO);
        return gameControlConfigDTO;
    }

    @Override
    public void insert(GameControlVO vo) {
        GameControlBean gameControlBean = gameControlConfigDao.selectOne(new LambdaQueryWrapper<GameControlBean>()
                .eq(GameControlBean::getFileBaseConfigId, vo.getFileBaseConfigId()));
        if (gameControlBean == null) {
            gameControlBean = new GameControlBean();
            DmgBeanUtils.copyProperties(vo, gameControlBean);
            gameControlBean.setCreateDate(new Date());
            gameControlBean.setModifyDate(new Date());
            gameControlConfigDao.insert(gameControlBean);
        }
    }

    @Override
    public void update(GameControlVO vo) {
        GameControlBean gameControlBean = gameControlConfigDao.selectOne(new LambdaQueryWrapper<GameControlBean>()
                .eq(GameControlBean::getFileBaseConfigId, vo.getFileBaseConfigId()));
        if (gameControlBean == null) {
            return;
        }
        DmgBeanUtils.copyProperties(vo, gameControlBean);
        gameControlBean.setModifyDate(new Date());
        gameControlConfigDao.updateById(gameControlBean);
    }

    @Override
    public void deleteById(Integer baseId) {
        gameControlConfigDao.deleteById(gameControlConfigDao.selectOne(new LambdaQueryWrapper<GameControlBean>()
                .eq(GameControlBean::getFileBaseConfigId, baseId)));
    }
}
