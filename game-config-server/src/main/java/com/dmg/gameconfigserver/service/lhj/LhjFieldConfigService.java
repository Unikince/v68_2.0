package com.dmg.gameconfigserver.service.lhj;


import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;

/**
 * 
 * 场次配置
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface LhjFieldConfigService {
	/**
	 * 增加游戏场次配置
	 * @param gameId
	 * @param number
	 * @return
	 * @throws Exception 
	 */
	int addSoltInfo(LhjFieldConfigDTO data);
	/**
	 * 删除游戏场次配置
	 * @param gameId
	 * @return 
	 */
	int delSoltInfo(int gameId);
	/**
	 * 更新游戏场次配置
	 * @param data
	 */
	int updateSoltInfo(LhjFieldConfigDTO data);
	/**
	 * 查询所有游戏场次配置
	 * @return
	 */
	List<LhjFieldConfigDTO> queryAllGameInfo();
	/**
	 * 查询单个游戏场次配置
	 * @return
	 */
	LhjFieldConfigDTO querySingleGameInfo(int gameId);

}

