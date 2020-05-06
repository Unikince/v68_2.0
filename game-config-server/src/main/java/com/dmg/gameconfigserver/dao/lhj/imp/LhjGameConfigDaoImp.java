package com.dmg.gameconfigserver.dao.lhj.imp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.dmg.gameconfigserver.dao.lhj.LhjGameConfigDao;
import com.dmg.gameconfigserver.model.dto.lhj.LhjBonusConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjFieldConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjGameListDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjInventoryControlDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjOddsPoolConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPayLimitConfigDTO;
import com.dmg.gameconfigserver.model.dto.lhj.LhjPlayerPayLimitConfigDTO;

@Service("lhjGameConfigDaoImp")
public class LhjGameConfigDaoImp implements LhjGameConfigDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<LhjGameListDTO> getSoltList() {
		String sql = "SELECT * FROM `t_dmg_lhj_gamelist` WHERE status=1";
		List<LhjGameListDTO> result = jdbcTemplate.query(sql, new RowMapper<LhjGameListDTO>() {
			@Override
			public LhjGameListDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				LhjGameListDTO sl = new LhjGameListDTO();
				sl.setId(rs.getInt("id"));
				sl.setName(rs.getString("name"));
				return sl;
			}
		});
		return result;
	}

	@Override
	public int addSoltInfo(LhjFieldConfigDTO data) {
		String sql = "INSERT INTO `t_dmg_lhj_soltinfo` "
				+ "(gameName,redisName,gameId,number,inAmount,state,playerNumber,"
				+ "betList,totalBet,totalPay,inventory,Odds,totalPlayerNumber,winReward,checkAmount,checkOdds,bigRewardOdds) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			int update = jdbcTemplate.update(sql,
					data.getGameName(),
					data.getRedisName(),
					data.getGameId(),
					data.getNumber(),
					data.getInAmount(),
					data.getState(),
					0,
					data.getBetList(),
					0,
					0,
					0,
					0,
					0,
					data.getWinReward(),
					data.getCheckAmount(),
					data.getCheckOdds(),
					data.getBigRewardOdds()
					);
			return update;
		} catch (Exception e) {
		}
		return 0;
	}

	@Override
	public int delSoltInfo(int gameId) {
		String sql = "UPDATE `t_dmg_lhj_soltinfo` SET state = 0 WHERE gameId = " + gameId;
		int update = jdbcTemplate.update(sql);
		
		//SoltGameInfo singleGameInfo = querySingleGameInfo(gameId);
		//CacheManager.instance().updateGameInfo(singleGameInfo);
		return update;
	}

	@Override
	public int updateSoltInfo(LhjFieldConfigDTO data) {
		String sql = "UPDATE  `t_dmg_lhj_soltinfo` SET number = ?,state = ?,totalPlayerNumber = ?,winReward = ?,betList = ?,inAmount = ? ,checkAmount = ?,checkOdds = ?,bigRewardOdds = ?"
				+ "WHERE gameId = " + data.getGameId();
		int update = jdbcTemplate.update(sql,
				data.getNumber(),
				data.getState(),
				data.getTotalPlayerNumber(),
				data.getWinReward(),
				data.getBetList(),
				data.getInAmount(),
				data.getCheckAmount(),
				data.getCheckOdds(),
				data.getBigRewardOdds()
				);
		
		//CacheManager.instance().updateGameInfo(querySingleGameInfo(data.getGameId()));
		return update;
	}

	@Override
	public List<LhjFieldConfigDTO> queryAllGameInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_soltinfo`";
		List<LhjFieldConfigDTO> result = jdbcTemplate.query(sql, new RowMapper<LhjFieldConfigDTO>(){
			@Override
			public LhjFieldConfigDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				LhjFieldConfigDTO si = new LhjFieldConfigDTO();
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
				si.setBigRewardOdds(rs.getDouble("bigRewardOdds"));
				return si;
			}});
		return result;
	}

	@Override
	public LhjFieldConfigDTO querySingleGameInfo(int gameId) {
		List<LhjFieldConfigDTO> result = queryAllGameInfo();
		for (LhjFieldConfigDTO soltInfo : result) {
			if (soltInfo.getGameId() == gameId) {
				return soltInfo;
			}
		}
		return null;
	}

	@Override
	public int addInventoryInfo(LhjInventoryConfigDTO data) {
		String sql = "INSERT INTO `t_dmg_lhj_inventoryconfig` (gameId,gamename,number,svalue,bvalue,odds) VALUES (?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql, data.getGameId(), data.getGamename(), 
				data.getNumber(), data.getSvalue(), data.getBvalue(), data.getOdds());
		
		//CacheManager.instance().updateSoltGameInventoryConfig(queryInventoryInfo(data.getGameId(),data.getNumber()));
		return update;
	}

	@Override
	public int delInventoryInfo(int gameId, int number) {
		String sql = "DELETE FROM `t_dmg_lhj_inventoryconfig` WHERE gameId = " + gameId + " AND number = " + number;
		int update = jdbcTemplate.update(sql);
		
		//CacheManager.instance().delSoltGameInventoryConfig(gameId,number);
		return update;
	}

	@Override
	public List<LhjInventoryConfigDTO> queryInventoryInfo(int gameId) {
		String sql = "SELECT * FROM `t_dmg_lhj_inventoryconfig` WHERE gameId = " + gameId;
		 List<LhjInventoryConfigDTO> query = jdbcTemplate.query(sql, new RowMapper<LhjInventoryConfigDTO>(){
			@Override
			public LhjInventoryConfigDTO mapRow(ResultSet rs, int arg1) throws SQLException {
				LhjInventoryConfigDTO sc = new LhjInventoryConfigDTO();
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
					dto.setCurrentWinLoseValue(rs.getDouble("currentWinLoseValue"));
					dto.setSetInventory(rs.getDouble("setInventory"));
					dto.setModel(rs.getInt("model"));
					dto.setType(rs.getInt("type"));
					dto.setWaterValue(rs.getDouble("waterValue"));
					return dto;
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}

	@Override
	public int updateInventoryControl(LhjInventoryControlDTO data) {
		String sql = "UPDATE  `t_dmg_lhj_inventorycontrol` SET setInventory = ?,model = ?,type = ?,waterValue = ? WHERE gameId = " + data.getGameId();
		int update = jdbcTemplate.update(sql, data.getSetInventory(),data.getModel(),data.getType(),data.getWaterValue());
		return update;
	}

	@Override
	public int addPoolConfig(LhjBonusConfigDTO data) {
		String sql = "INSERT INTO `t_dmg_lhj_poolconfig` (gameId,gameName,poolName,initAmount,lowBet,poolTotalRatio,poolOpenLow,bonusLv,rewardRatio) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)";
		int update = jdbcTemplate.update(sql,data.getGameId(),data.getGameName(),data.getPoolName(),data.getInitAmount(),data.getLowBet(),
				data.getPoolTotalRatio(),data.getPoolOpenLow(),data.getBonusLv(),data.getRewardRatio());
		
		//CacheManager.instance().updatePoolConfig(querySinglePoolConfig(data.getGameId(), data.getPoolName()));
		return update;
	}

	@Override
	public int updatePoolConfig(LhjBonusConfigDTO data) {
		String sql = "UPDATE  `t_dmg_lhj_poolconfig` SET initAmount = ?,poolTotalRatio = ?,poolOpenLow = ?,bonusLv = ?,rewardRatio = ?,LowBet = ? "
				+ "WHERE gameId = " + data.getGameId() + " AND poolName = " + "\"" +  data.getPoolName() + "\"" ;
		int update = jdbcTemplate.update(sql, data.getInitAmount(), data.getPoolTotalRatio(), data.getPoolOpenLow(),
				data.getBonusLv(), data.getRewardRatio(), data.getLowBet());
		
		//CacheManager.instance().updatePoolConfig(querySinglePoolConfig(pc.getGameId(), pc.getPoolName()));
		return update;
	}

	@Override
	public List<LhjBonusConfigDTO> queryGamePoolConfig(int gameId) {
		String sql = "SELECT * FROM `t_dmg_lhj_poolconfig` WHERE gameId = " + gameId;
		List<LhjBonusConfigDTO> query = jdbcTemplate.query(sql, new RowMapper<LhjBonusConfigDTO>(){
			@Override
			public LhjBonusConfigDTO mapRow(ResultSet rs, int arg1) throws SQLException {
				LhjBonusConfigDTO pc = new LhjBonusConfigDTO();
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

	@Override
	public int addOddsPoolInfo(LhjOddsPoolConfigDTO data) {
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

	@Override
	public int updateOddsPoolInfo(int state, double Ratio) {
		String sql = "UPDATE `t_dmg_lhj_oddspoolconfig` SET state = "+ state  +",poolTotalRatio = " + Ratio;
		int update = jdbcTemplate.update(sql);
		
		//CacheManager.instance().updateOddsPoolConfig(queryOddsPoolInfo());
		return update;
	}

	@Override
	public LhjOddsPoolConfigDTO queryOddsPoolInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_oddspoolconfig`";
		List<LhjOddsPoolConfigDTO> query = jdbcTemplate.query(sql, new RowMapper<LhjOddsPoolConfigDTO>(){
			@Override
			public LhjOddsPoolConfigDTO mapRow(ResultSet rs, int arg1) throws SQLException {
				LhjOddsPoolConfigDTO oc = new LhjOddsPoolConfigDTO();
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

	@Override
	public int addPayLimitInfo(LhjPayLimitConfigDTO data) {
		String sql = "INSERT INTO `t_dmg_lhj_paylimit` (number,payLowLimit,odds,payRatio) VALUES (?,?,?,?)";
		int update = jdbcTemplate.update(sql, data.getNumber(), data.getPayLowLimit(), data.getOdds(), data.getPayRatio());
		//CacheManager.instance().updatePayLimit(queryPayLimitInfo(data.getNumber()));
		return update;
	}

	@Override
	public int delPayLimitInfo(int number) {
		String sql = "DELETE FROM `t_dmg_lhj_paylimit` WHERE number = " + number ;
		int update = jdbcTemplate.update(sql);
		//CacheManager.instance().delPayLimit(number);
		return update;
	}

	@Override
	public int updatePayLimitInfo(LhjPayLimitConfigDTO data) {
		String sql = "UPDATE  `t_dmg_lhj_paylimit` SET payLowLimit = ?,odds = ?,payRatio = ? WHERE number = " + data.getNumber();
		int update = jdbcTemplate.update(sql,
				data.getPayLowLimit(),
				data.getOdds(),
				data.getPayRatio()
				);
		//CacheManager.instance().updatePayLimit(queryPayLimitInfo(data.getNumber()));
		return update;
	}

	@Override
	public List<LhjPayLimitConfigDTO> queryPayLimitInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_paylimit`";
		List<LhjPayLimitConfigDTO> query = jdbcTemplate.query(sql,new RowMapper<LhjPayLimitConfigDTO>(){

			@Override
			public LhjPayLimitConfigDTO mapRow(ResultSet rs, int arg1) throws SQLException {
				LhjPayLimitConfigDTO pl = new LhjPayLimitConfigDTO();
				pl.setNumber(rs.getInt("number"));
				pl.setPayLowLimit(rs.getDouble("payLowLimit"));
				pl.setOdds(rs.getDouble("odds"));
				pl.setPayRatio(rs.getDouble("payRatio"));
				return pl;
			}});
		return query;
	}

	@Override
	public int addPayPlayerLimitInfo(LhjPlayerPayLimitConfigDTO data) {
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

	@Override
	public int delPayPlayerLimitInfo(int number) {
		String sql = "DELETE FROM `t_dmg_lhj_payplayerlimit` WHERE number = " + number ;
		int update = jdbcTemplate.update(sql);
		//CacheManager.instance().delPlayerPayLimit(number);
		return update;
	}

	@Override
	public int updatePlayerPayLimitInfo(LhjPlayerPayLimitConfigDTO data) {
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

	@Override
	public List<LhjPlayerPayLimitConfigDTO> queryPayPlayerLimitInfo() {
		String sql = "SELECT * FROM `t_dmg_lhj_payplayerlimit`";
		List<LhjPlayerPayLimitConfigDTO> query = jdbcTemplate.query(sql,new RowMapper<LhjPlayerPayLimitConfigDTO>(){
			@Override
			public LhjPlayerPayLimitConfigDTO mapRow(ResultSet rs, int arg1) throws SQLException {
				LhjPlayerPayLimitConfigDTO ppl = new LhjPlayerPayLimitConfigDTO();
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

	
}
