package com.dmg.gameconfigserver.service.lhj.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameListDTO;
import com.dmg.gameconfigserver.service.lhj.LhjFieldConfigService;


@Service("lhjFieldConfigService")
public class LhjFieldConfigServiceImpl implements LhjFieldConfigService {

	@Autowired
	@Qualifier("lhjGameConfigDaoImp")
    private LhjGameConfigDao lhjGameConfigDao;
    @Autowired
    private StringRedisTemplate redisTemplate;
    
	@Override
	public int addSoltInfo(LhjFieldConfigDTO data) {
		List<LhjGameListDTO> soltList = lhjGameConfigDao.getSoltList();
		Map<Integer, String> map = new HashMap<>();
		soltList.forEach(v -> map.put(v.getId(), v.getName()));
		if (!map.containsKey(data.getGameId())) {
			return 0;
		}
		int result = lhjGameConfigDao.addSoltInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return result;
	}
	@Override
	public int delSoltInfo(int gameId) {
		int del = lhjGameConfigDao.delSoltInfo(gameId);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return del;
	}
	@Override
	public int updateSoltInfo(LhjFieldConfigDTO data) {
		int update = lhjGameConfigDao.updateSoltInfo(data);
		redisTemplate.convertAndSend(RedisRegionConfig.UPDATE_LHJ_GAME_CONFIG_CHANNEL,"更新游戏配置");
		return update;
	}
	@Override
	public List<LhjFieldConfigDTO> queryAllGameInfo() {
		List<LhjFieldConfigDTO> allGameInfo = lhjGameConfigDao.queryAllGameInfo();
		return allGameInfo;
	}
	@Override
	public LhjFieldConfigDTO querySingleGameInfo(int gameId) {
		LhjFieldConfigDTO singleGameInfo = lhjGameConfigDao.querySingleGameInfo(gameId);
		return singleGameInfo;
	}
    

}