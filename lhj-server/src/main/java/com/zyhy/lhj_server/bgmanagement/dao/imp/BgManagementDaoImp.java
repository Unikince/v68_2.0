package com.zyhy.lhj_server.bgmanagement.dao.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.lhj_server.bgmanagement.config.GamePoolConfigEnum;
import com.zyhy.lhj_server.bgmanagement.dao.BgManagementDao;
import com.zyhy.lhj_server.bgmanagement.entity.GamePoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.OddsPoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutLimit;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutPlayerLimit;
import com.zyhy.lhj_server.bgmanagement.entity.PayoutRecord;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInventoryConfig;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameList;
import com.zyhy.lhj_server.bgmanagement.entity.palyerWaterRecord;
import com.zyhy.lhj_server.bgmanagement.entity.totalWaterRecord;
import com.zyhy.lhj_server.bgmanagement.feign.dto.LhjInventoryControlDTO;

@Service
public class BgManagementDaoImp implements BgManagementDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 创建赔率奖池日志
	 * @param log
	 */
	public void createPoolGameLhjLog(GameLhjLog log) {
		String sql = "insert into `t_dmg_lhj_poolgamelog"
				+ "`(gamename,roleid,rolenick,uuid,bet,reward,RewardBetween,todayWin,totalWin,rewardtype,ispool,time) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, 
				log.getGamename(), 
				log.getRoleid(), 
				log.getRolenick(),
				log.getUuid(),
				log.getBet(), 
				log.getReward(),
				Arrays.toString(log.getRewardBetween()),
				log.getTodayWin(),
				log.getTotalWin(),
				log.getRewardtype(),
				log.getIspool(),
				log.getTime());
	}
	
	/**
	 * 查询所有游戏信息
	 * messageid = 101
	 * @return 
	 */
	@Override
	public List<SoltGameInfo> queryAllGameInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_soltinfo`";
		List<SoltGameInfo> result = jdbcTemplate.query(sql, new RowMapper<SoltGameInfo>(){
			@Override
			public SoltGameInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SoltGameInfo si = new SoltGameInfo();
				si.setGameName(rs.getString("gameName"));
				si.setRedisName(rs.getString("redisName"));
				si.setGameId(rs.getInt("gameId"));
				si.setNumber(rs.getInt("number"));
				si.setInAmount(rs.getDouble("inAmount"));
				si.setState(rs.getInt("state"));
				si.setPlayerNumber(rs.getInt("playerNumber"));
				si.setBetList(rs.getString("betList"));
				si.setTotalBet(rs.getDouble("totalBet"));
				si.setTotalPay(rs.getDouble("totalPay"));
				si.setInventory(rs.getDouble("inventory"));
				si.setOdds(rs.getDouble("Odds"));
				si.setTotalPlayerNumber(rs.getInt("totalPlayerNumber"));
				si.setWinReward(rs.getInt("winReward"));
				si.setCheckAmount(rs.getDouble("checkAmount"));
				si.setCheckOdds(rs.getDouble("checkOdds"));
				return si;
			}});
		return result;
	}
	
	/**
	 * 查询单个指定游戏信息
	 * @return 
	 */
	public SoltGameInfo querySingleGameInfo(String gamename) {
		String sql = "SELECT * FROM `t_dmg_lhj_soltinfo` WHERE gameName = " + "'" + gamename + "'";
		List<SoltGameInfo> result = jdbcTemplate.query(sql, new RowMapper<SoltGameInfo>(){
			@Override
			public SoltGameInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				SoltGameInfo si = new SoltGameInfo();
				si.setGameName(rs.getString("gameName"));
				si.setRedisName(rs.getString("redisName"));
				si.setGameId(rs.getInt("gameId"));
				si.setNumber(rs.getInt("number"));
				si.setInAmount(rs.getDouble("inAmount"));
				si.setState(rs.getInt("state"));
				si.setPlayerNumber(rs.getInt("playerNumber"));
				si.setBetList(rs.getString("betList"));
				si.setTotalBet(rs.getDouble("totalBet"));
				si.setTotalPay(rs.getDouble("totalPay"));
				si.setInventory(rs.getDouble("inventory"));
				si.setOdds(rs.getDouble("Odds"));
				si.setTotalPlayerNumber(rs.getInt("totalPlayerNumber"));
				si.setWinReward(rs.getInt("winReward"));
				si.setCheckAmount(rs.getDouble("checkAmount"));
				si.setCheckOdds(rs.getDouble("checkOdds"));
				return si;
			}});
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 获取老虎机游戏列表 
	 * messageid = 102
	 * @return
	 */
	@Override
	public List<SoltGameList> getSoltList() {
		String sql = "SELECT * FROM `t_dmg_lhj_gamelist` WHERE status=1";
		List<SoltGameList> result = jdbcTemplate.query(sql, new RowMapper<SoltGameList>() {
			@Override
			public SoltGameList mapRow(ResultSet rs, int rowNum) throws SQLException {
				SoltGameList sl = new SoltGameList();
				sl.setId(rs.getInt("id"));
				sl.setName(rs.getString("name"));
				return sl;
			}
		});
		return result;
	}

	/**
	 * 设置老虎机游戏信息为开启状态
	 *  messageid = 103
	 * @return 
	 */
	@Override
	public int addSoltInfo(String gamename, int number) throws Exception{
		String sql = "UPDATE `t_dmg_lhj_soltinfo` SET number = " + number +", state = 1 WHERE gameName =" + gamename;
		try {
			int update = jdbcTemplate.update(sql);
			return update;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//SoltGameInfo singleGameInfo = querySingleGameInfo(gamename);
		//CacheManager.instance().updateGameInfo(singleGameInfo);
		return 0;
	}
	
	/**
	 * 设置游戏信息为关闭状态
	 *  messageid = 104
	 * @return 
	 * @return
	 */
	@Override
	public int delSoltInfo(int gameId) {
		String sql = "UPDATE `t_dmg_lhj_soltinfo` SET state = 0 WHERE gameId = " + gameId;
		int update = jdbcTemplate.update(sql);
		
		//SoltGameInfo singleGameInfo = querySingleGameInfo(gameId);
		//CacheManager.instance().updateGameInfo(singleGameInfo);
		return update;
	}
	
	
	
	/**
	 * 设置所有老虎机为开启状态
	 *  messageid = 203
	 * @return 
	 * @return
	 */
	@Override
	public int openAllsoltGame() {
		String sql = "UPDATE `t_dmg_lhj_soltinfo` SET state = 1";
		int update = jdbcTemplate.update(sql);
		
		//List<SoltGameInfo> allGameInfo = queryAllGameInfo();
		//CacheManager.instance().updateAllGameInfo(allGameInfo);
		return update;
	}

	/**
	 * 设置所有老虎机为关闭状态
	 *  messageid = 204
	 * @return 
	 * @return
	 */
	@Override
	public int closeAllsoltGame() {
		String sql = "UPDATE `t_dmg_lhj_soltinfo` SET state = 0";
		int update = jdbcTemplate.update(sql);
		
		//List<SoltGameInfo> allGameInfo = queryAllGameInfo();
		//CacheManager.instance().updateAllGameInfo(allGameInfo);
		return update;
	}

	/**
	 * 根据id查询单个游戏场次配置信息
	 *  messageid = 201
	 */
	@Override
	public SoltGameInfo querySingleGameInfo(int gameId) {
		List<SoltGameInfo> result = queryAllGameInfo();
		for (SoltGameInfo soltInfo : result) {
			if (soltInfo.getGameId() == gameId) {
				return soltInfo;
			}
		}
		return null;
	}
	
	/**
	 * 更新游戏场次配置信息
	 * messageid = 202
	 */
	@Override
	public int updateSoltInfo(SoltGameInfo data) {
		String sql = "UPDATE  `t_dmg_lhj_soltinfo` SET number = ?,state = ?,totalPlayerNumber = ?,winReward = ?,betList = ?,inAmount = ? ,checkAmount = ?,checkOdds = ?"
				+ "WHERE gameId = " + data.getGameId();
		int update = jdbcTemplate.update(sql,
				data.getNumber(),
				data.getState(),
				data.getTotalPlayerNumber(),
				data.getWinReward(),
				JSONObject.toJSONString(data.getBetList()),
				data.getInAmount(),
				data.getCheckAmount(),
				data.getCheckOdds()
				);
		
		//CacheManager.instance().updateGameInfo(querySingleGameInfo(data.getGameId()));
		return update;
	}
	
	/**
	 * 更新单个游戏信息
	 * @param gameId
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public int updateSoltInventoryInfo(SoltGameInfo soltInfo) {
		String sql = "UPDATE  `t_dmg_lhj_soltinfo` SET totalBet = ?,totalPay = ?,inventory = ?,Odds = ? "
				+ "WHERE gamename = " + "'" + soltInfo.getGameName() + "'";
		int update = jdbcTemplate.update(sql,
				soltInfo.getTotalBet(),
				soltInfo.getTotalPay(),
				soltInfo.getInventory(),
				soltInfo.getOdds()
				);
		//CacheManager.instance().updateGameInfo(querySingleGameInfo(soltInfo.getGameName()));
		return update;
	}

	/**
	 * 查询单个游戏库存配置信息
	 * messageid = 301
	 * @return 
	 */
	@Override
	public List<SoltGameInventoryConfig> queryInventoryInfo(int gameId) {
		String sql = "SELECT * FROM `t_dmg_lhj_inventoryconfig` WHERE gameId = " + gameId;
		 List<SoltGameInventoryConfig> query = jdbcTemplate.query(sql, new RowMapper<SoltGameInventoryConfig>(){
			@Override
			public SoltGameInventoryConfig mapRow(ResultSet rs, int arg1) throws SQLException {
				SoltGameInventoryConfig sc = new SoltGameInventoryConfig();
				sc.setGameId(gameId);
				sc.setGamename(rs.getString("gamename"));
				sc.setNumber(rs.getInt("number"));
				sc.setSvalue(rs.getDouble("svalue"));
				sc.setBvalue(rs.getDouble("bvalue"));
				sc.setOdds(rs.getDouble("odds"));
				return sc;
			}});
		 return query;
	}
	
	/**
	 * 查询单个游戏库存配置信息
	 * @return 
	 */
	public List<SoltGameInventoryConfig> queryInventoryInfo(String gamename) {
		String sql = "SELECT * FROM `t_dmg_lhj_inventoryconfig` WHERE gameName =" + "'" +gamename + "'";
		 List<SoltGameInventoryConfig> query = jdbcTemplate.query(sql, new RowMapper<SoltGameInventoryConfig>(){
			@Override
			public SoltGameInventoryConfig mapRow(ResultSet rs, int arg1) throws SQLException {
				SoltGameInventoryConfig sc = new SoltGameInventoryConfig();
				sc.setGameId(rs.getInt("gameId"));
				sc.setGamename(rs.getString("gamename"));
				sc.setNumber(rs.getInt("number"));
				sc.setSvalue(rs.getDouble("svalue"));
				sc.setBvalue(rs.getDouble("bvalue"));
				sc.setOdds(rs.getDouble("odds"));
				return sc;
			}});
		 return query;
	}
	
	/**
	 * 查询单个游戏单条库存配置信息
	 * @return 
	 */
	public SoltGameInventoryConfig queryInventoryInfo(int gameId ,int number) {
		String sql = "SELECT * FROM `t_dmg_lhj_inventoryconfig` WHERE gameId=" + gameId + " AND number=" + number;
		 List<SoltGameInventoryConfig> query = jdbcTemplate.query(sql, new RowMapper<SoltGameInventoryConfig>(){
			@Override
			public SoltGameInventoryConfig mapRow(ResultSet rs, int arg1) throws SQLException {
				SoltGameInventoryConfig sc = new SoltGameInventoryConfig();
				sc.setGameId(rs.getInt("gameId"));
				sc.setGamename(rs.getString("gamename"));
				sc.setNumber(rs.getInt("number"));
				sc.setSvalue(rs.getDouble("svalue"));
				sc.setBvalue(rs.getDouble("bvalue"));
				sc.setOdds(rs.getDouble("odds"));
				return sc;
			}});
		 if (query.size() > 0) {
			return query.get(0);
		}
		 return null;
	}
	
	/**
	 * 查询所有游戏库存配置信息
	 * @return 
	 */
	public List<SoltGameInventoryConfig> queryAllInventoryInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_inventoryconfig` ";
		 List<SoltGameInventoryConfig> query = jdbcTemplate.query(sql, new RowMapper<SoltGameInventoryConfig>(){
			@Override
			public SoltGameInventoryConfig mapRow(ResultSet rs, int arg1) throws SQLException {
				SoltGameInventoryConfig sc = new SoltGameInventoryConfig();
				sc.setGameId(rs.getInt("gameId"));
				sc.setGamename(rs.getString("gamename"));
				sc.setNumber(rs.getInt("number"));
				sc.setSvalue(rs.getDouble("svalue"));
				sc.setBvalue(rs.getDouble("bvalue"));
				sc.setOdds(rs.getDouble("odds"));
				return sc;
			}});
		 return query;
	}
	
	/**
	 * 添加库存配置信息
	 * messageid = 302
	 * @return 
	 */
	@Override
	public int addInventoryInfo(SoltGameInventoryConfig data ) {
		String sql = "INSERT INTO `t_dmg_lhj_inventoryconfig` (gameId,gamename,number,svalue,bvalue,odds) VALUES (?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql, data.getGameId(), data.getGamename(), 
				data.getNumber(), data.getSvalue(), data.getBvalue(), data.getOdds());
		
		//CacheManager.instance().updateSoltGameInventoryConfig(queryInventoryInfo(data.getGameId(),data.getNumber()));
		return update;
	}
	
	
	
	
	/**
	 *删除库存配置信息
	 *messageid = 303
	 * @return 
	 */
	@Override
	public int delInventoryInfo(int gameId, int number) {
		String sql = "DELETE FROM `t_dmg_lhj_inventoryconfig` WHERE gameId = " + gameId + " AND number = " + number;
		int update = jdbcTemplate.update(sql);
		
		//CacheManager.instance().delSoltGameInventoryConfig(gameId,number);
		return update;
	}

	/**
	 * 查询库存控制配置
	 * @param data
	 * @return
	 */
	@Override
	public LhjInventoryControlDTO queryInventoryControl(int gameId) {
		String sql = "SELECT * FROM `t_dmg_lhj_inventorycontrol` WHERE gameId = " + gameId;
		LhjInventoryControlDTO query = null;
		try {
			query = jdbcTemplate.queryForObject(sql, new RowMapper<LhjInventoryControlDTO>(){
				@Override
				public LhjInventoryControlDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					LhjInventoryControlDTO dto = new LhjInventoryControlDTO();
					dto.setGameId(gameId);
					dto.setGameName(rs.getString("gameName"));
					dto.setRedisName(rs.getString("redisName"));
					dto.setCurrentInventory(querySingleGameInfo(gameId).getInventory());
					dto.setSetInventory(rs.getDouble("setInventory"));
					dto.setModel(rs.getInt("model"));
					dto.setType(rs.getInt("type"));
					dto.setWaterValue(rs.getDouble("waterValue"));
					return dto;
				}});
		} catch (Exception e) {
		}
		return query;
	}
	
	
	/**
	 * 更新库存控制配置
	 * @param waterValue 
	 * @param type 
	 * @param model 
	 * @param currentWinLoseValue 
	 * @param setInventory 
	 * @param gameId 
	 * @param waterValue 
	 * @param type 
	 * @param model 
	 * @param currentWinLoseValue 
	 * @param data
	 * @return
	 */
	@Override
	public int updateInventoryControl(LhjInventoryControlDTO data) {
		String sql = "UPDATE  `t_dmg_lhj_inventorycontrol` SET setInventory = ?,currentWinLoseValue = ?,model = ?,type = ?,waterValue = ? WHERE gameId = " + data.getGameId();
		int update = jdbcTemplate.update(sql, data.getSetInventory(),data.getCurrentWinLoseValue(),data.getModel(),data.getType(),data.getWaterValue());
		return update;
	}
	
	
	/**
	 * 检查序号是否被使用
	 * @param gameId
	 * @param number
	 * @return 返回使用序号游戏信息
	 */
	public SoltGameInfo checkNumber(int gameId, int number) {
		List<SoltGameInfo> result = queryAllGameInfo();
		for (SoltGameInfo soltInfo : result) {
			if (soltInfo.getNumber() == number) {
				return soltInfo;
			}
		}
		return null;
	}
	
	/**
	 * 获取游戏单个奖池配置
	 * messageid = 401
	 */
	@Override
	public GamePoolConfig querySinglePoolConfig(int gameId, String poolName) {
		String sql = "SELECT * FROM `t_dmg_lhj_poolconfig` WHERE gameId = " + gameId + " AND poolName = " + "\"" +  poolName + "\"" ;
		List<GamePoolConfig> query = jdbcTemplate.query(sql, new RowMapper<GamePoolConfig>(){
			@Override
			public GamePoolConfig mapRow(ResultSet rs, int arg1) throws SQLException {
				GamePoolConfig pc = new GamePoolConfig();
				// 游戏id
				pc.setGameId(rs.getInt("gameId"));
				// 游戏名字
				pc.setGameName(rs.getString("gameName"));
				// 奖池名称
				pc.setPoolName(rs.getString("poolName"));
				// 初始化金额
				pc.setInitAmount(rs.getDouble("initAmount"));
				// 最低累计下注金额
				pc.setLowBet(rs.getDouble("lowBet"));
				// 奖池累计比例
				pc.setPoolTotalRatio(rs.getDouble("poolTotalRatio"));
				// 奖池开启下限
				pc.setPoolOpenLow(rs.getDouble("poolOpenLow"));
				// 中奖概率
				pc.setBonusLv(rs.getDouble("bonusLv"));
				// 中奖比例
				pc.setRewardRatio(rs.getDouble("rewardRatio"));
				return pc;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}
	
	/**
	 * 获取游戏所有奖池配置
	 */
	public List<GamePoolConfig> queryAllPoolConfig(int gameId) {
		String sql = "SELECT * FROM `t_dmg_lhj_poolconfig` WHERE gameId = " + gameId;
		List<GamePoolConfig> query = jdbcTemplate.query(sql, new RowMapper<GamePoolConfig>(){
			@Override
			public GamePoolConfig mapRow(ResultSet rs, int arg1) throws SQLException {
				GamePoolConfig pc = new GamePoolConfig();
				// 游戏id
				pc.setGameId(rs.getInt("gameId"));
				// 游戏名字
				pc.setGameName(rs.getString("gameName"));
				// 奖池名称
				pc.setPoolName(rs.getString("poolName"));
				// 初始化金额
				pc.setInitAmount(rs.getDouble("initAmount"));
				// 最低累计下注金额
				pc.setLowBet(rs.getDouble("lowBet"));
				// 奖池累计比例
				pc.setPoolTotalRatio(rs.getDouble("poolTotalRatio"));
				// 奖池开启下限
				pc.setPoolOpenLow(rs.getDouble("poolOpenLow"));
				// 中奖概率
				pc.setBonusLv(rs.getDouble("bonusLv"));
				// 中奖比例
				pc.setRewardRatio(rs.getDouble("rewardRatio"));
				return pc;
			}});
		return query;
	}
	
	/**
	 * 添加奖池配置
	 */
	@Override
	public int addPoolConfig(GamePoolConfigEnum data) {
		String sql = "INSERT INTO `t_dmg_lhj_poolconfig` (gameId,gameName,poolName,initAmount,lowBet,poolTotalRatio,poolOpenLow,bonusLv,rewardRatio) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql,data.getGameId(),data.getGameName(),data.getPoolName(),data.getInitAmount(),data.getLowBet(),
				data.getPoolTotalRatio(),data.getPoolOpenLow(),data.getBonusLv(),data.getRewardRatio());
		
		//CacheManager.instance().updatePoolConfig(querySinglePoolConfig(data.getGameId(), data.getPoolName()));
		return update;
	}
	
	/**
	 * 更新奖池配置
	 * messageid = 402
	 */
	@Override
	public int updatePoolConfig(GamePoolConfig pc) {
		String sql = "UPDATE  `t_dmg_lhj_poolconfig` SET initAmount = ?,poolTotalRatio = ?,poolOpenLow = ?,bonusLv = ?,rewardRatio = ?,LowBet = ? "
				+ "WHERE gameId = " + pc.getGameId() + " AND poolName = " + "\"" +  pc.getPoolName() + "\"" ;
		int update = jdbcTemplate.update(sql, pc.getInitAmount(), pc.getPoolTotalRatio(), pc.getPoolOpenLow(),
				pc.getBonusLv(), pc.getRewardRatio(), pc.getLowBet());
		
		//CacheManager.instance().updatePoolConfig(querySinglePoolConfig(pc.getGameId(), pc.getPoolName()));
		return update;
	}
	
	/**
	 * 查询赔率奖池信息
	 * messageid = 501
	 * @return 
	 */
	@Override
	public OddsPoolConfig queryOddsPoolInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_oddspoolconfig` ";
		List<OddsPoolConfig> query = jdbcTemplate.query(sql, new RowMapper<OddsPoolConfig>(){
			@Override
			public OddsPoolConfig mapRow(ResultSet rs, int arg1) throws SQLException {
				OddsPoolConfig oc = new OddsPoolConfig();
				oc.setPoolId(rs.getInt("poolId"));
				oc.setPoolName(rs.getString("poolName"));
				oc.setPoolTotalRatio(rs.getDouble("poolTotalRatio"));
				oc.setCurrentAmount(rs.getDouble("currentAmount"));
				oc.setState(rs.getInt("state"));
				oc.setPayCount(rs.getInt("payCount"));
				oc.setPayTotal(rs.getDouble("payTotal"));
				oc.setAverageAmount(rs.getDouble("averageAmount"));
				oc.setLastPayTime(rs.getString("lastPayTime"));
				return oc;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}
	
	/**
	 * 更新赔率奖池信息
	 * messageid = 509
	 */
	@Override
	public int updateOddsPoolInfo(int state, double Ratio) {
		String sql = "UPDATE  `t_dmg_lhj_oddspoolconfig` SET state = "+ state  +",poolTotalRatio = " + Ratio;
		int update = jdbcTemplate.update(sql);
		
		//CacheManager.instance().updateOddsPoolConfig(queryOddsPoolInfo());
		return update;
	}

	/**
	 * 添加赔率奖池信息
	 */
	public int addOddsPoolInfo(OddsPoolConfig data){
		String sql = "INSERT INTO `t_dmg_lhj_oddspoolconfig` (poolId,poolName,poolTotalRatio,currentAmount,state,payCount,payTotal,averageAmount,lastPayTime) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		int update = 0;
		try {
			update = jdbcTemplate.update(sql,
					data.getPoolId(),
					data.getPoolName(),
					data.getPoolTotalRatio(),
					data.getCurrentAmount(),
					data.getState(),
					data.getPayCount(),
					data.getPayTotal(),
					data.getAverageAmount(),
					data.getLastPayTime()
						);
		} catch (Exception e) {
		}
		return update;
	}
	
	/**
	 * 更新赔率奖池信息
	 * @return
	 */
	public int updateOddsPool(OddsPoolConfig data){
		String sql = "UPDATE  `t_dmg_lhj_oddspoolconfig` SET currentAmount = ?,payCount = ?,payTotal = ?,averageAmount = ?,lastPayTime = ?";
		int update = jdbcTemplate.update(sql,
				data.getCurrentAmount(),
				data.getPayCount(),
				data.getPayTotal(),
				data.getAverageAmount(),
				data.getLastPayTime()
				);
		
		//CacheManager.instance().updateOddsPoolConfig(queryOddsPoolInfo());
		return update;
	}
	
	/**
	 * 查询派奖条件
	 * messageid = 502
	 * @return 
	 */
	@Override
	public List<PayoutLimit> queryPayLimitInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_paylimit` ";
		List<PayoutLimit> query = jdbcTemplate.query(sql,new RowMapper<PayoutLimit>(){

			@Override
			public PayoutLimit mapRow(ResultSet rs, int arg1) throws SQLException {
				PayoutLimit pl = new PayoutLimit();
				pl.setNumber(rs.getInt("number"));
				pl.setPayLowLimit(rs.getDouble("payLowLimit"));
				pl.setOdds(rs.getDouble("odds"));
				pl.setPayRatio(rs.getDouble("payRatio"));
				return pl;
			}});
		return query;
	}
	
	/**
	 * 查询单个派奖条件
	 * @return 
	 */
	public PayoutLimit queryPayLimitInfo(int number) {
		String sql = "SELECT * FROM `t_dmg_lhj_paylimit` WHERE number =" + number;
		List<PayoutLimit> query = jdbcTemplate.query(sql,new RowMapper<PayoutLimit>(){
			@Override
			public PayoutLimit mapRow(ResultSet rs, int arg1) throws SQLException {
				PayoutLimit pl = new PayoutLimit();
				pl.setNumber(rs.getInt("number"));
				pl.setPayLowLimit(rs.getDouble("payLowLimit"));
				pl.setOdds(rs.getDouble("odds"));
				pl.setPayRatio(rs.getDouble("payRatio"));
				return pl;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}
	
	/**
	 * 添加派奖条件
	 * messageid = 503
	 */
	@Override
	public int addPayLimitInfo(PayoutLimit data) {
		String sql = "INSERT INTO `t_dmg_lhj_paylimit` (number,payLowLimit,odds,payRatio) VALUES (?,?,?,?)";
		int update = jdbcTemplate.update(sql, data.getNumber(), data.getPayLowLimit(), data.getOdds(), data.getPayRatio());
		//CacheManager.instance().updatePayLimit(queryPayLimitInfo(data.getNumber()));
		return update;
	}
	
	
	/**
	 * 更新派奖条件
	 *  messageid = 510
	 */
	@Override
	public int updatePayLimitInfo(PayoutLimit data) {
		String sql = "UPDATE  `t_dmg_lhj_paylimit` SET payLowLimit = ?,odds = ?,payRatio = ? WHERE number = " + data.getNumber();
		int update = jdbcTemplate.update(sql,
				data.getPayLowLimit(),
				data.getOdds(),
				data.getPayRatio()
				);
		//CacheManager.instance().updatePayLimit(queryPayLimitInfo(data.getNumber()));
		return update;
	}

	/**
	 * 删除派奖条件
	 * messageid = 504
	 */
	@Override
	public int delPayLimitInfo(int number) {
		String sql = "DELETE FROM `t_dmg_lhj_paylimit` WHERE number = " + number ;
		int update = jdbcTemplate.update(sql);
		//CacheManager.instance().delPayLimit(number);
		return update;
	}
	
	/**
	 * 查询玩家派奖条件
	 * messageid = 505
	 * @return 
	 */
	@Override
	public List<PayoutPlayerLimit> queryPayPlayerLimitInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_payplayerlimit` ";
		List<PayoutPlayerLimit> query = jdbcTemplate.query(sql,new RowMapper<PayoutPlayerLimit>(){
			@Override
			public PayoutPlayerLimit mapRow(ResultSet rs, int arg1) throws SQLException {
				PayoutPlayerLimit ppl = new PayoutPlayerLimit();
				ppl.setNumber(rs.getInt("number"));
				ppl.setTotalLowLimit(rs.getDouble("totalLowLimit"));
				ppl.setDayLowLimit(rs.getDouble("dayLowLimit"));
				ppl.setTotalWaterLow(rs.getDouble("totalWaterLow"));
				ppl.setDayWaterLow(rs.getDouble("dayWaterLow"));
				ppl.setOddsTotalHight(rs.getDouble("oddsTotalHight"));
				ppl.setDayOddsHight(rs.getDouble("dayOddsHight"));
				return ppl;
			}});
		return query;
	}
	
	/**
	 * 查询单条玩家派奖条件
	 * @return 
	 */
	public PayoutPlayerLimit queryPayPlayerLimitInfo(int number) {
		String sql = "SELECT * FROM `t_dmg_lhj_payplayerlimit` WHERE number =" + number;
		List<PayoutPlayerLimit> query = jdbcTemplate.query(sql,new RowMapper<PayoutPlayerLimit>(){
			@Override
			public PayoutPlayerLimit mapRow(ResultSet rs, int arg1) throws SQLException {
				PayoutPlayerLimit ppl = new PayoutPlayerLimit();
				ppl.setNumber(rs.getInt("number"));
				ppl.setTotalLowLimit(rs.getDouble("totalLowLimit"));
				ppl.setDayLowLimit(rs.getDouble("dayLowLimit"));
				ppl.setTotalWaterLow(rs.getDouble("totalWaterLow"));
				ppl.setDayWaterLow(rs.getDouble("dayWaterLow"));
				ppl.setOddsTotalHight(rs.getDouble("oddsTotalHight"));
				ppl.setDayOddsHight(rs.getDouble("dayOddsHight"));
				return ppl;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}

	/**
	 * 添加玩家派奖条件
	 * messageid = 506
	 */
	@Override
	public int addPayPlayerLimitInfo(PayoutPlayerLimit data) {
		String sql = "INSERT INTO `t_dmg_lhj_payplayerlimit` (number,totalLowLimit,dayLowLimit,totalWaterLow,dayWaterLow,oddsTotalHight,dayOddsHight) "
				+ "VALUES (?,?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql, 
				data.getNumber(),
				data.getTotalLowLimit(),
				data.getDayLowLimit(),
				data.getTotalWaterLow(),
				data.getDayWaterLow(),
				data.getOddsTotalHight(),
				data.getDayOddsHight()
				);
		
		//CacheManager.instance().updatePlayerPayLimit(queryPayPlayerLimitInfo(data.getNumber()));
		return update;
	}

	
	/**
	 * 更新玩家派奖条件
	 *  messageid = 511
	 */
	@Override
	public int updatePlayerPayLimitInfo(PayoutPlayerLimit data) {
		String sql = "UPDATE  `t_dmg_lhj_payplayerlimit` SET "
				+ "totalLowLimit = ?,"
				+ "dayLowLimit = ?,"
				+ "totalWaterLow = ? ,"
				+ "dayWaterLow = ?,"
				+ "oddsTotalHight = ?,"
				+ "dayOddsHight = ?"
				+ "WHERE number = " + data.getNumber();
		int update = jdbcTemplate.update(sql,
				data.getTotalLowLimit(),
				data.getDayLowLimit(),
				data.getTotalWaterLow(),
				data.getDayWaterLow(),
				data.getOddsTotalHight(),
				data.getDayOddsHight()
				);
		//CacheManager.instance().updatePlayerPayLimit(queryPayPlayerLimitInfo(data.getNumber()));
		return update;
	}

	/**
	 * 删除玩家派奖条件
	 * messageid = 507
	 */
	@Override
	public int delPayPlayerLimitInfo(int number ) {
		String sql = "DELETE FROM `t_dmg_lhj_payplayerlimit` WHERE number = " + number ;
		int update = jdbcTemplate.update(sql);
		//CacheManager.instance().delPlayerPayLimit(number);
		return update;
	}

	/**
	 * 查询玩家当日总赢取奖励
	 */
	@Override
	public double queryTodayWin(String roleid) {
		long currentTimeMillis = System.currentTimeMillis();
		Date currentDate = new Date(currentTimeMillis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = sdf.format(currentDate);
		String sql = "SELECT sum(reward) FROM `lhj_game_log_" + currentTime + "` " + "WHERE roleid =" + roleid;
		Double query = jdbcTemplate.queryForObject(sql, Double.class);
		return query;
	}
	
	/**
	 * 查询玩家当日总下注额度
	 */
	@Override
	public double queryTodayBet(String roleid) {
		long currentTimeMillis = System.currentTimeMillis();
		Date currentDate = new Date(currentTimeMillis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = sdf.format(currentDate);
		String sql = "SELECT sum(bet) FROM `lhj_game_log_" + currentTime + "` " + "WHERE roleid =" + roleid;
		Double query = jdbcTemplate.queryForObject(sql, Double.class);
		return query;
	}
	/**
	 * 查询玩家当日的总收益
	 */
	@Override
	public PayoutRecord queryTodayTotalReward(String roleid) {
		String sql = "SELECT todayWin,totalWin FROM `t_dmg_lhj_palyerwaterrecord` WHERE roleid =" + roleid;
		List<PayoutRecord> query = jdbcTemplate.query(sql,new RowMapper<PayoutRecord>(){
			@Override
			public PayoutRecord mapRow(ResultSet rs, int arg1) throws SQLException {
				PayoutRecord pr = new PayoutRecord();
				pr.setTodayWin(rs.getDouble("todayWin"));
				pr.setTotalWin(rs.getDouble("totalWin"));
				return pr;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}
	
	/**
	 * 查询所有的日志
	 */
	@Override
	public List<Map<String, Object>> queryAllLog() {
		String sql = "SELECT t.table_name FROM information_schema.TABLES t WHERE t.TABLE_SCHEMA = 'zgqp_log' AND t.TABLE_NAME LIKE '%lhj_game_log_%'";
		List<Map<String, Object>> query = jdbcTemplate.queryForList(sql);
		if (query.size() > 0) {
			return query;
		}
		return null;
	}

	/**
	 * 查询所有玩家派奖记录
	 * messageid = 508
	 * @param roleid 
	 */
	@SuppressWarnings("finally")
	@Override
	public List<PayoutRecord> queryPlayerRecord(int page , int limit) {
		String sql = "SELECT SQL_CALC_FOUND_ROWS * FROM `t_dmg_lhj_poolgamelog` " + " LIMIT " + (page - 1)*limit + "," + limit ;
		List<PayoutRecord> query = new ArrayList<>();
		try {
			query = jdbcTemplate.query(sql,new RowMapper<PayoutRecord>(){
				@Override
				public PayoutRecord mapRow(ResultSet rs, int arg1) throws SQLException {
					PayoutRecord pr = new PayoutRecord();
					pr.setId(rs.getInt("id"));
					pr.setTime(rs.getLong("time"));
					pr.setPlayerId(Integer.parseInt(rs.getString("roleid")));
					pr.setNickName(rs.getString("rolenick"));
					pr.setReward(rs.getDouble("reward"));
					pr.setRewardType(rs.getInt("rewardtype"));
					pr.setRewardName(rs.getString("gamename"));
					return pr;
				}});
		} finally {
			return query;
		}
	}
	/**
	 * 分页查询记录条数
	 * @return
	 */
	public int count(){
		//String sql = "SELECT FOUND_ROWS() AS count";
		String sql = "SELECT COUNT(*) AS count  FROM `t_dmg_lhj_poolgamelog` ";
		return jdbcTemplate.queryForObject(sql, new RowMapper<Integer>(){

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("count");
			}
			
		});
	}

	/**
	 * 查询玩家流水记录
	 */
	@Override
	public palyerWaterRecord queryPlayerWaterRecord(String roleid) {
		String sql = "SELECT * FROM `t_dmg_lhj_palyerwaterrecord` WHERE roleid = " + roleid;
		List<palyerWaterRecord> query = jdbcTemplate.query(sql,new RowMapper<palyerWaterRecord>(){
			@Override
			public palyerWaterRecord mapRow(ResultSet rs, int arg1) throws SQLException {
				palyerWaterRecord pr = new palyerWaterRecord();
				pr.setTodayBet(rs.getDouble("todayBet"));
				pr.setTodayreward(rs.getDouble("todayreward"));
				pr.setTodayWin(rs.getDouble("todayWin"));
				pr.setTodayWinlv(rs.getDouble("todayWinlv"));
				pr.setTotalBet(rs.getDouble("totalBet"));
				pr.setTotalreward(rs.getDouble("totalreward"));
				pr.setTotalWin(rs.getDouble("totalWin"));
				pr.setTotalWinlv(rs.getDouble("totalWinlv"));
				return pr;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}
	
	/**
	 * 查询总流水记录
	 */
	@Override
	public totalWaterRecord queryTotalWaterRecord() {
		String sql = "SELECT todayBet,totalBet FROM `t_dmg_lhj_totalwaterrecord` ";
		List<totalWaterRecord> query = jdbcTemplate.query(sql,new RowMapper<totalWaterRecord>(){
			@Override
			public totalWaterRecord mapRow(ResultSet rs, int arg1) throws SQLException {
				totalWaterRecord pr = new totalWaterRecord();
				pr.setTodayBet(rs.getDouble("todayBet"));
				pr.setTotalBet(rs.getDouble("totalBet"));
				return pr;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}
	
	
	/**
	 * 查询所有玩家当日总收益记录
	 */
	public palyerWaterRecord queryAllPlayerTodayWin(String roleid) {
		String sql = "SELECT `todayWin` FROM `t_dmg_lhj_palyerwaterrecord` ";
		List<palyerWaterRecord> query = jdbcTemplate.query(sql,new RowMapper<palyerWaterRecord>(){
			@Override
			public palyerWaterRecord mapRow(ResultSet rs, int arg1) throws SQLException {
				palyerWaterRecord pr = new palyerWaterRecord();
				pr.setTodayWin(rs.getDouble("todayWin"));
				return pr;
			}});
		if (query.size() > 0) {
			return query.get(0);
		}
		return null;
	}

	/**
	 * 添加玩家流水记录
	 * @return 
	 */
	@Override
	public int addPlayerWaterRecord(palyerWaterRecord data) {
		//String sql = "REPLACE INTO `palyerwaterrecord` (roleid,rolenick,uuid,todayWin,totalWin) VALUES (?,?,?,?,?)";
		String sql = "INSERT INTO `t_dmg_lhj_palyerwaterrecord` "
				+ "(roleid,rolenick,uuid,todayBet,todayreward,todayWin,totalBet,totalreward,totalWin) VALUES(?,?,?,?,?,?,?,?,?) "
				+ "ON DUPLICATE KEY UPDATE "
				+ "todayBet = todayBet + VALUES(todayBet),"
				+ "todayreward = todayreward + VALUES(todayreward),"
				+ "todayWin = todayWin + VALUES(todayWin),"
				+ "todayWinlv = todayreward/todayBet,"
				+ "totalBet = totalBet + VALUES(totalBet),"
				+ "totalreward = totalreward + VALUES(totalreward),"
				+ "totalWin = totalWin + VALUES(totalWin),"
				+ "totalWinlv = totalreward/totalBet";
		int update = jdbcTemplate.update(sql, 
				data.getRoleid(),
				data.getRolenick(),
				data.getUuid(),
				data.getTodayBet(),
				data.getTodayreward(),
				data.getTodayWin(),
				data.getTotalBet(),
				data.getTotalreward(),
				data.getTotalWin()
				);
		return update;
	}
	
	
	/**
	 * 添加总流水记录
	 * @return 
	 */
	@Override
	public int addTotalWaterRecord(totalWaterRecord data) {
		String sql = "INSERT INTO `t_dmg_lhj_totalwaterrecord` "
				+ "(tableName,todayBet,todayreward,todayWin,totalBet,totalreward,totalWin) VALUES(?,?,?,?,?,?,?) "
				+ "ON DUPLICATE KEY UPDATE "
				+ "todayBet = todayBet + VALUES(todayBet),"
				+ "todayreward = todayreward + VALUES(todayreward),"
				+ "todayWin = todayWin + VALUES(todayWin),"
				+ "totalBet = totalBet + VALUES(totalBet),"
				+ "totalreward = totalreward + VALUES(totalreward),"
				+ "totalWin = totalWin + VALUES(totalWin)";
		int update = jdbcTemplate.update(sql, 
				data.getTableName(),
				data.getTodayBet(),
				data.getTodayreward(),
				data.getTodayWin(),
				data.getTotalBet(),
				data.getTotalreward(),
				data.getTotalWin()
				);
		return update;
	}

	/**
	 * 更新玩家流水当日记录为0
	 * @return 
	 */
	@Override
	public int updatePlayerWaterRecord() {
		String sql = "UPDATE `t_dmg_lhj_palyerwaterrecord` SET todayBet = 0,todayreward = 0,todayWin = 0,todayWinlv = 0";
		int update = jdbcTemplate.update(sql);
		return update;
	}
	
	/**
	 * 更新总流水当日记录为0
	 * @return 
	 */
	@Override
	public int updateTotalWaterRecord() {
		String sql = "UPDATE `t_dmg_lhj_totalwaterrecord` SET todayBet = 0,todayreward = 0,todayWin = 0";
		int update = jdbcTemplate.update(sql);
		return update;
	}
	
}
