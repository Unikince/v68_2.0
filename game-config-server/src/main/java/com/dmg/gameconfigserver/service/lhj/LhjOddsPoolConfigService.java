package com.dmg.gameconfigserver.service.lhj;


import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjOddsPoolConfigDTO;

/**
 * 
 * 赔率奖池配置
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface LhjOddsPoolConfigService {
	/**
	 * 增加赔率奖池信息
	 * @param data
	 * @return
	 */
	int addOddsPoolInfo(LhjOddsPoolConfigDTO data);
	/**
	 * 更新赔率奖池信息
	 */
	int updateOddsPoolInfo(LhjOddsPoolConfigDTO data);
	/**
	 * 查询赔率奖池信息
	 * @return 
	 */
	LhjOddsPoolConfigDTO queryOddsPoolInfo();

}

