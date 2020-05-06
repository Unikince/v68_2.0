package com.dmg.gameconfigserver.service.lhj;


import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryControlDTO;

/**
 * 
 * 库存控制
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface LhjInventoryControlService {
	/**
	 * 查询库存控制
	 * @param data
	 * @return 
	 */
	LhjInventoryControlDTO queryInventoryControl(int gameId);
	
	/**
	 * 更新库存控制
	 * @param data
	 * @return 
	 */
	int updateInventoryControl(LhjInventoryControlDTO data);

}

