package com.dmg.gameconfigserver.service.lhj.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;


@Service("lhjBonusConfigService")
public class LhjBonusConfigServiceImpl implements LhjBonusConfigService {

	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    
	@Override
	public int addPoolConfig(LhjBonusConfigDTO data) {
		int add = lhjGameConfigDao.addPoolConfig(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return add;
	}
	@Override
	public int updatePoolConfig(LhjBonusConfigDTO data) {
		int update = lhjGameConfigDao.updatePoolConfig(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return update;
	}
	@Override
	public List<LhjBonusConfigDTO> queryGamePoolConfig(int gameId) {
		List<LhjBonusConfigDTO> gamePoolConfigList = lhjGameConfigDao.queryGamePoolConfig(gameId);
		return gamePoolConfigList;
	}

}