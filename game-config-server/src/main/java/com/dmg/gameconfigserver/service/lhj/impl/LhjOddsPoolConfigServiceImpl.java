package com.dmg.gameconfigserver.service.lhj.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjOddsPoolConfigDTO;
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjOddsPoolConfigService;


@Service("lhjOddsPoolConfigService")
public class LhjOddsPoolConfigServiceImpl implements LhjOddsPoolConfigService {

	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
	@Override
	public int addOddsPoolInfo(LhjOddsPoolConfigDTO data) {
		int add = lhjGameConfigDao.addOddsPoolInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return add;
	}
	@Override
	public int updateOddsPoolInfo(LhjOddsPoolConfigDTO data) {
		int update = lhjGameConfigDao.updateOddsPoolInfo(data.getState(), data.getPoolTotalRatio());
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return update;
	}
	@Override
	public LhjOddsPoolConfigDTO queryOddsPoolInfo() {
		LhjOddsPoolConfigDTO lhjOddsPoolConfigDTO = lhjGameConfigDao.queryOddsPoolInfo();
		return lhjOddsPoolConfigDTO;
	}
    

}