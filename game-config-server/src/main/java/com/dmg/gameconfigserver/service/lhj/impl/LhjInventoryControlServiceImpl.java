package com.dmg.gameconfigserver.service.lhj.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryControlDTO;
import com.dmg.gameconfigserver.service.lhj.LhjInventoryControlService;


@Service("LhjInventoryControlService")
public class LhjInventoryControlServiceImpl implements LhjInventoryControlService {

	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    
	@Override
	public LhjInventoryControlDTO queryInventoryControl(int gameId) {
		LhjInventoryControlDTO query = lhjGameConfigDao.queryInventoryControl(gameId);
		return query;
	}
	
	@Override
	public int updateInventoryControl(LhjInventoryControlDTO data) {
		int update = lhjGameConfigDao.updateInventoryControl(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return update;
	}
}