package com.dmg.gameconfigserver.dao.lhj;

import java.util.List;

import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameListDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryControlDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjOddsPoolConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPlayerPayLimitConfigDTO;

public interface LhjGameConfigDao {
	/**
	 * 获取所有游戏列表
	 * @return
	 */
	List<LhjGameListDTO> getSoltList();
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
	int delInventoryInfo(int gameId, int number);
	/**
	 * 查询库存配置
	 * @param data
	 * @return 
	 */
	List<LhjInventoryConfigDTO> queryInventoryInfo(int gameId);
	
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
	
	
	/**
	 * 增加赔率奖池信息
	 * @param data
	 * @return
	 */
	int addOddsPoolInfo(LhjOddsPoolConfigDTO data);
	/**
	 * 更新赔率奖池信息
	 */
	int updateOddsPoolInfo(int state, double Ratio);
	/**
	 * 查询赔率奖池信息
	 * @return 
	 */
	LhjOddsPoolConfigDTO queryOddsPoolInfo();
	
	
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
