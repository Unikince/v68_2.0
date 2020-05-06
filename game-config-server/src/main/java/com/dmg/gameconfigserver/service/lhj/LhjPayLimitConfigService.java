package com.dmg.gameconfigserver.service.lhj;


import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPayLimitConfigDTO;

/**
 * 
 * 派奖条件配置
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface LhjPayLimitConfigService {
	/**
	 * 增加派奖条件
	 * @return 
	 */
	int addPayLimitInfo(LhjPayLimitConfigDTO data);
	/**
	 * 删除派奖条件
	 */
	int delPayLimitInfo(int number);
	/**
	 * 更新派奖条件
	 * @return 
	 */
	int updatePayLimitInfo(LhjPayLimitConfigDTO data);
	/**
	 * 查询派奖条件
	 * @return 
	 */
	List<LhjPayLimitConfigDTO> queryPayLimitInfo();

}

