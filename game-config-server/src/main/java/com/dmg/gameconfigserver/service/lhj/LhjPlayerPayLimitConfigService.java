package com.dmg.gameconfigserver.service.lhj;


import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPlayerPayLimitConfigDTO;

/**
 * 
 * 派奖条件配置
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface LhjPlayerPayLimitConfigService {
	/**
	 * 添加玩家派奖条件
	 * @return 
	 */
	int addPayPlayerLimitInfo(LhjPlayerPayLimitConfigDTO data);
	/**
	 * 删除玩家派奖条件
	 */
	int delPayPlayerLimitInfo(int number);
	/**
	 * 更新玩家派奖条件
	 * @return 
	 */
	int updatePlayerPayLimitInfo(LhjPlayerPayLimitConfigDTO data);
	/**
	 * 查询玩家派奖条件
	 * @return 
	 */
	List<LhjPlayerPayLimitConfigDTO> queryPayPlayerLimitInfo();

}

