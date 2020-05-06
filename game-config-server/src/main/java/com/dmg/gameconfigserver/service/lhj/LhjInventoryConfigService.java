package com.dmg.gameconfigserver.service.lhj;


import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryConfigDTO;

/**
 * 
 * 库存配置
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface LhjInventoryConfigService {
	/**
	 * 增加库存配置
	 * @param data
	 * @return 
	 */
	int addInventoryInfo(LhjInventoryConfigDTO data);
	/**
	 * 删除库存配置
	 * @param data
	 * @return 
	 */
	int delInventoryInfo(LhjInventoryConfigDTO data);
	/**
	 * 查询库存配置
	 * @param data
	 * @return 
	 */
	List<LhjInventoryConfigDTO> queryInventoryInfo(int gameId);

}

