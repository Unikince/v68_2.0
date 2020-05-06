package com.dmg.gameconfigserver.service.config;

import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:22 2019/12/5
 */
public interface GameExchangeRateService {

    /**
     * @Author liubo
     * @Description //TODO 根据code查询汇率配置
     * @Date 10:24 2019/12/5
     **/
    GameExchangeRateVO getExchangeRateByCode(String code);
}
