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
import com.dmg.gameconfigserver.service.lhj.LhjBonusConfigService;
import com.dmg.gameconfigserver.service.lhj.LhjPayLimitConfigService;


@Service("lhjPayLimitConfigService")
public class LhjPayLimitConfigServiceImpl implements LhjPayLimitConfigService {

	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    
	@Override
	public int addPayLimitInfo(LhjPayLimitConfigDTO data) {
		int add = lhjGameConfigDao.addPayLimitInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return add;
	}
	@Override
	public int delPayLimitInfo(int number) {
		int del = lhjGameConfigDao.delPayLimitInfo(number);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return del;
	}
	@Override
	public int updatePayLimitInfo(LhjPayLimitConfigDTO data) {
		int update = lhjGameConfigDao.updatePayLimitInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return update;
	}
	@Override
	public List<LhjPayLimitConfigDTO> queryPayLimitInfo() {
		List<LhjPayLimitConfigDTO> lhjPayLimitConfigDTOList = lhjGameConfigDao.queryPayLimitInfo();
		return lhjPayLimitConfigDTOList;
	}
}