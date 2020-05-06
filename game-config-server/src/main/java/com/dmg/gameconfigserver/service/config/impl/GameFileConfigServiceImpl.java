package com.dmg.gameconfigserver.service.config.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.web.BusinessException;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.dao.config.FileBaseConfigDao;
import com.dmg.gameconfigserver.dao.config.GameFileConfigDao;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.bean.config.GameFileBean;
import com.dmg.gameconfigserver.model.dto.config.GameFileDTO;
import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import com.dmg.gameconfigserver.service.config.GameFileConfigService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:17 2019/10/11
 */
@Service
public class GameFileConfigServiceImpl implements GameFileConfigService {

    @Autowired
    private GameFileConfigDao gameFileConfigDao;

    @Autowired
    private FileBaseConfigDao fileBaseConfigDao;

    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    @Value("${exchange-rate-code}")
    private String exchangeRateCode;

    @Override
    public List<GameFileDTO> getGameFileByGameId(Integer gameId) {
        List<GameFileDTO> gameFileDTOList = new ArrayList<>();
        List<FileBaseConfigBean> fileBaseConfigBeans = fileBaseConfigDao.selectList(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getGameId, gameId));
        if (CollectionUtil.isEmpty(fileBaseConfigBeans)) {
            return gameFileDTOList;
        }
        GameExchangeRateVO gameExchangeRateVO = gameExchangeRateService.getExchangeRateByCode(exchangeRateCode);
        for (FileBaseConfigBean fileBaseConfigBean : fileBaseConfigBeans) {
            GameFileDTO gameFileDTO = new GameFileDTO();
            GameFileBean gameFileBean = gameFileConfigDao.selectOne(new LambdaQueryWrapper<GameFileBean>()
                    .eq(GameFileBean::getFileBaseConfigId, fileBaseConfigBean.getId()));
            DmgBeanUtils.copyProperties(fileBaseConfigBean, gameFileDTO);
            DmgBeanUtils.copyProperties(gameFileBean, gameFileDTO);
            if (gameExchangeRateVO != null) {
                if (gameFileDTO.getBaseScore() != null) {
                    gameFileDTO.setBaseScore(gameFileDTO.getBaseScore().multiply(gameExchangeRateVO.getExchangeRate()));
                }
                if (gameFileDTO.getLowerLimit() != null) {
                    gameFileDTO.setLowerLimit(gameFileDTO.getLowerLimit().multiply(gameExchangeRateVO.getExchangeRate()));
                }
                if (gameFileDTO.getUpperLimit() != null) {
                    gameFileDTO.setUpperLimit(gameFileDTO.getUpperLimit().multiply(gameExchangeRateVO.getExchangeRate()));
                }
            }
            gameFileDTOList.add(gameFileDTO);
        }
        return gameFileDTOList;
    }

    @Override
    public GameFileDTO getGameFileByBaseId(Integer baseId) {
        FileBaseConfigBean fileBaseConfigBean = fileBaseConfigDao.selectById(baseId);
        if (fileBaseConfigBean == null) {
            return null;
        }
        GameFileBean gameFileBean = gameFileConfigDao.selectOne(new LambdaQueryWrapper<GameFileBean>()
                .eq(GameFileBean::getFileBaseConfigId, fileBaseConfigBean.getId()));
        GameFileDTO gameFileDTO = new GameFileDTO();
        DmgBeanUtils.copyProperties(fileBaseConfigBean, gameFileDTO);
        DmgBeanUtils.copyProperties(gameFileBean, gameFileDTO);
        return gameFileDTO;
    }

    @Override
    public void insert(FileBaseConfigBean fileBaseConfigBean, GameFileBean gameFileBean) {
        FileBaseConfigBean check = fileBaseConfigDao.selectOne(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getGameId, fileBaseConfigBean.getGameId())
                .eq(FileBaseConfigBean::getFileId, fileBaseConfigBean.getFileId()));
        if (check != null) {
            throw new BusinessException(String.valueOf(ResultEnum.HAS_EXIT.getCode()), ResultEnum.HAS_EXIT.getMsg());
        }
        fileBaseConfigDao.insert(fileBaseConfigBean);
        gameFileBean.setFileBaseConfigId(fileBaseConfigBean.getId());
        gameFileBean.setCreateDate(new Date());
        gameFileBean.setModifyDate(new Date());
        gameFileConfigDao.insert(gameFileBean);
        this.cacheRemove(fileBaseConfigBean.getGameId());
    }

    @Override
    public void update(FileBaseConfigBean fileBaseConfigBean, GameFileBean gameFileBean) {
        FileBaseConfigBean bean = fileBaseConfigDao.selectById(gameFileBean.getFileBaseConfigId());
        if (bean == null) {
            return;
        }
        DmgBeanUtils.copyProperties(fileBaseConfigBean, bean);
        fileBaseConfigDao.updateById(bean);
        GameFileBean gameFileBean1 = gameFileConfigDao.selectOne(new LambdaQueryWrapper<GameFileBean>()
                .eq(GameFileBean::getFileBaseConfigId, gameFileBean.getFileBaseConfigId()));
        if (gameFileBean1 == null) {
            return;
        }
        DmgBeanUtils.copyProperties(gameFileBean, gameFileBean1);
        gameFileBean1.setModifyDate(new Date());
        gameFileConfigDao.updateById(gameFileBean1);
        this.cacheRemove(bean.getGameId());
    }

    @Override
    public void deleteById(Integer baseId) {
        FileBaseConfigBean bean = fileBaseConfigDao.selectById(baseId);
        fileBaseConfigDao.deleteById(baseId);
        gameFileConfigDao.deleteById(gameFileConfigDao.selectOne(new LambdaQueryWrapper<GameFileBean>()
                .eq(GameFileBean::getFileBaseConfigId, baseId)));
        this.cacheRemove(bean.getGameId());

    }

    @Override
    public List<FileBaseConfigBean> getFileDetailByGameId(Integer gameId) {
        List<FileBaseConfigBean> fileBaseConfigBeans = fileBaseConfigDao.selectList(new LambdaQueryWrapper<FileBaseConfigBean>()
                .eq(FileBaseConfigBean::getGameId, gameId));
        return fileBaseConfigBeans;
    }

    @CacheEvict(cacheNames = Constant.ROOM_FILE_BASE, key = "#gameId")
    public void cacheRemove(Integer gameId) {
    }
}
