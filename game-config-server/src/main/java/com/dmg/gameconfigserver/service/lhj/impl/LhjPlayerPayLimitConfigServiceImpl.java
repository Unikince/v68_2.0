package com.dmg.gameconfigserver.service.lhj.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPlayerPayLimitConfigDTO;
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjPayLimitConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjPlayerPayLimitConfigService;


@Service("lhjPlayerPayLimitConfigService")
public class LhjPlayerPayLimitConfigServiceImpl implements LhjPlayerPayLimitConfigService {

	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    
	@Override
	public int addPayPlayerLimitInfo(LhjPlayerPayLimitConfigDTO data) {
		int add = lhjGameConfigDao.addPayPlayerLimitInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return add;
	}
	@Override
	public int delPayPlayerLimitInfo(int number) {
		int del = lhjGameConfigDao.delPayPlayerLimitInfo(number);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return del;
	}
	@Override
	public int updatePlayerPayLimitInfo(LhjPlayerPayLimitConfigDTO data) {
		int update = lhjGameConfigDao.updatePlayerPayLimitInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return update;
	}
	@Override
	public List<LhjPlayerPayLimitConfigDTO> queryPayPlayerLimitInfo() {
		List<LhjPlayerPayLimitConfigDTO> lhjPlayerPayLimitConfigDTOList = lhjGameConfigDao.queryPayPlayerLimitInfo();
		return lhjPlayerPayLimitConfigDTOList;
	}
    
}