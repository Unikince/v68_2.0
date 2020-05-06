package com.dmg.gameconfigserver.service.config.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dmg.gameconfigserver.dao.config.rate.GameExchangeRateDao;
import com.dmg.gameconfigserver.model.bean.config.rate.GameExchangeRateBean;
import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import com.dmg.server.common.util.DmgBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:25 2019/12/5
 */
@Service
public class GameExchangeRateServiceImpl implements GameExchangeRateService {

    @Autowired
    private GameExchangeRateDao gameExchangeRateDao;

    @Override
    public GameExchangeRateVO getExchangeRateByCode(String code) {
        //GameExchangeRateBean gameExchangeRateBean = gameExchangeRateDao.selectOne(new LambdaQueryWrapper<GameExchangeRateBean>()
                //.eq(GameExchangeRateBean::getExchangeRateCode, code));
        GameExchangeRateBean gameExchangeRateBean = gameExchangeRateDao.selectOne(new QueryWrapper<GameExchangeRateBean>()
        		.eq("exchange_rate_code", code));
        if (gameExchangeRateBean == null) {
            return null;
        }
        GameExchangeRateVO gameExchangeRateVO = new GameExchangeRateVO();
        DmgBeanUtils.copyProperties(gameExchangeRateBean, gameExchangeRateVO);
        return gameExchangeRateVO;
    }
}
