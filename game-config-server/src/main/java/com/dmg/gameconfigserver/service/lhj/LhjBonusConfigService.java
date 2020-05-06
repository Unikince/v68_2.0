package com.dmg.gameconfigserver.service.lhj;


import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;

/**
 * 
 * 游戏奖池配置
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface LhjBonusConfigService {
	/**
	 * 增加游戏奖池配置
	 * @param data
	 * @return
	 */
	int addPoolConfig(LhjBonusConfigDTO data);
	/**
	 * 更新游戏奖池配置
	 * @param data
	 * @return
	 */
	int updatePoolConfig(LhjBonusConfigDTO data);
	/**
	 * 查询游戏奖池配置
	 * @return
	 */
	List<LhjBonusConfigDTO> queryGamePoolConfig(int gameId);

}

