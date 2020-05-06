package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.dao.config.GameWaterPoolConfigDao;
import com.dmg.gameconfigserver.model.bean.config.GameWaterPoolConfigBean;
import com.dmg.gameconfigserver.model.dto.config.GameWaterPoolByWaterDTO;
import com.dmg.gameconfigserver.model.vo.config.GameWaterPoolConfigVO;
import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import com.dmg.gameconfigserver.service.config.GameWaterPoolConfigService;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import com.dmg.server.common.util.DmgBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:47 2019/9/27
 */
@Slf4j
@Service
public class GameWaterPoolConfigServiceImpl implements GameWaterPoolConfigService {

    @Autowired
    private GameWaterPoolConfigDao gameWaterPoolConfigDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    @Value("${exchange-rate-code}")
    private String exchangeRateCode;

    @Override
    public List<GameWaterPoolConfigBean> getAllList() {
        return gameWaterPoolConfigDao.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public GameWaterPoolConfigBean getGameWaterPoolById(Integer id) {
        return gameWaterPoolConfigDao.selectById(id);
    }

    @Override
    public List<GameWaterPoolConfigBean> getGameWaterPoolByGame(Integer gameId) {
        List<GameWaterPoolConfigBean> gameWaterPoolConfigBeanList = gameWaterPoolConfigDao.selectList(new LambdaQueryWrapper<GameWaterPoolConfigBean>().eq(GameWaterPoolConfigBean::getGameId, gameId));
        if (gameWaterPoolConfigBeanList == null) {
            return null;
        }
        GameExchangeRateVO gameExchangeRateVO = gameExchangeRateService.getExchangeRateByCode(exchangeRateCode);
        if (gameExchangeRateVO != null) {
            List<GameWaterPoolConfigBean> gameWaterPoolConfigBeans = new ArrayList<>();
            for (GameWaterPoolConfigBean gameWaterPoolConfigBean : gameWaterPoolConfigBeanList) {
                if (gameWaterPoolConfigBean.getWaterHigh() != null) {
                    gameWaterPoolConfigBean.setWaterHigh(gameExchangeRateVO.getExchangeRate().multiply(gameWaterPoolConfigBean.getWaterHigh()));
                }
                if (gameWaterPoolConfigBean.getWaterLow() != null) {
                    gameWaterPoolConfigBean.setWaterLow(gameExchangeRateVO.getExchangeRate().multiply(gameWaterPoolConfigBean.getWaterLow()));
                }
                gameWaterPoolConfigBeans.add(gameWaterPoolConfigBean);
            }
            return gameWaterPoolConfigBeans;
        }
        return gameWaterPoolConfigBeanList;
    }

    @Override
    public List<GameWaterPoolConfigBean> getGameWaterPoolByGame(Integer gameId, Integer fileId) {
        return gameWaterPoolConfigDao.selectList(new LambdaQueryWrapper<GameWaterPoolConfigBean>()
                .eq(GameWaterPoolConfigBean::getGameId, gameId)
                .eq(GameWaterPoolConfigBean::getRoomLevel, fileId)
                .orderByDesc(GameWaterPoolConfigBean::getWaterLow, GameWaterPoolConfigBean::getWaterHigh));
    }

    @Override
    public void insert(GameWaterPoolConfigBean gameWaterPoolConfigBean) {
        gameWaterPoolConfigBean.setCreateDate(new Date());
        gameWaterPoolConfigBean.setModifyDate(new Date());
        gameWaterPoolConfigDao.insert(gameWaterPoolConfigBean);
        this.redisData(gameWaterPoolConfigBean.getGameId());
    }

    @Override
    public void update(GameWaterPoolConfigVO vo) {
        GameWaterPoolConfigBean gameWaterPoolConfigBean = gameWaterPoolConfigDao.selectById(vo.getId());
        if (gameWaterPoolConfigBean == null) {
            return;
        }
        DmgBeanUtils.copyProperties(vo, gameWaterPoolConfigBean);
        gameWaterPoolConfigBean.setModifyDate(new Date());
        gameWaterPoolConfigDao.updateById(gameWaterPoolConfigBean);
        this.cacheRemove(gameWaterPoolConfigBean.getGameId(), gameWaterPoolConfigBean.getRoomLevel());
        this.redisData(gameWaterPoolConfigBean.getGameId());
    }

    @Override
    public void deleteById(Integer id) {
        GameWaterPoolConfigBean gameWaterPoolConfigBean = gameWaterPoolConfigDao.selectById(id);
        gameWaterPoolConfigDao.deleteById(id);
        this.cacheRemove(gameWaterPoolConfigBean.getGameId(), gameWaterPoolConfigBean.getRoomLevel());
        this.redisData(gameWaterPoolConfigBean.getGameId());
    }

    @Override
    public GameWaterPoolConfigBean getInfoByWater(GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO) {
        GameExchangeRateVO gameExchangeRateVO = gameExchangeRateService.getExchangeRateByCode(exchangeRateCode);
        BigDecimal water = gameExchangeRateVO != null ? gameWaterPoolByWaterDTO.getWater().multiply(gameExchangeRateVO.getExchangeRate()) : gameWaterPoolByWaterDTO.getWater();
        return gameWaterPoolConfigDao.selectList(new LambdaQueryWrapper<GameWaterPoolConfigBean>()
                .eq(GameWaterPoolConfigBean::getGameId, gameWaterPoolByWaterDTO.getGameId())
                .eq(GameWaterPoolConfigBean::getRoomLevel, gameWaterPoolByWaterDTO.getRoomLevel())
                .and(i -> i.isNotNull(GameWaterPoolConfigBean::getWaterLow)
                        .le(GameWaterPoolConfigBean::getWaterLow, water)
                        .or().isNull(GameWaterPoolConfigBean::getWaterLow))
                .and(i -> i.isNotNull(GameWaterPoolConfigBean::getWaterHigh)
                        .gt(GameWaterPoolConfigBean::getWaterHigh, water)
                        .or().isNull(GameWaterPoolConfigBean::getWaterHigh))).get(0);
    }

    @CacheEvict(cacheNames = Constant.GAME_WATER_POOL, key = "#gameId+'_'+#roomLevel+'_*'")
    public void cacheRemove(Integer gameId, Integer roomLevel) {
    }

    private void redisData(Integer gameId) {
        redisUtil.lSet(Constant.GAME_WATER_POOL + ":" + gameId, DmgBeanUtils.copyList(this.getGameWaterPoolByGame(gameId), GameWaterPoolDTO.class));
    }
}
