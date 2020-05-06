package com.dmg.gameconfigserver.service.lhj.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryConfigDTO;
import com.dmg.gameconfigserver.service.lhj.LhjInventoryConfigService;


@Service("lhjInventoryConfigService")
public class LhjInventoryConfigServiceImpl implements LhjInventoryConfigService {

	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    
	@Override
	public int addInventoryInfo(LhjInventoryConfigDTO data) {
		List<LhjInventoryConfigDTO> inventoryInfo = queryInventoryInfo(data.getGameId());
		if (inventoryInfo.size() > 0) {
			for (LhjInventoryConfigDTO dto : inventoryInfo) {
				if (dto.getNumber() == data.getNumber()) {
					return 0;
				}
			}
		}
		int add = lhjGameConfigDao.addInventoryInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return add;
	}
	@Override
	public int delInventoryInfo(LhjInventoryConfigDTO data) {
		int del = lhjGameConfigDao.delInventoryInfo(data.getGameId(), data.getNumber());
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return del;
	}
	@Override
	public List<LhjInventoryConfigDTO> queryInventoryInfo(int gameId) {
		List<LhjInventoryConfigDTO> inventoryInfoList = lhjGameConfigDao.queryInventoryInfo(gameId);
		return inventoryInfoList;
	}
}