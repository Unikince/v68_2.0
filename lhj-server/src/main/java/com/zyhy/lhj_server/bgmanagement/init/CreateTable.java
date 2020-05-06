package com.zyhy.lhj_server.bgmanagement.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CreateTable {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public void init() {
		String soltinventoryconfig = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_inventoryconfig"
		        //+ "`(`id`  int(30) NOT NULL AUTO_INCREMENT ,"
		        + "`(`gameId` int(30) NOT NULL DEFAULT '0',"
				+ "`gamename`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`number` int(30) NOT NULL DEFAULT '0',"
				+ "`svalue` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`bvalue` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`odds` double(30,2) NOT NULL DEFAULT '0.00')";
				//+ "PRIMARY KEY (`id`))";
		jdbcTemplate.update(soltinventoryconfig);
		
		String soltInfo = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_soltinfo"
				+ "`(`gameName`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`redisName`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`gameId` int(30) NOT NULL DEFAULT '0',"
				+ "`number` int(30) NOT NULL DEFAULT '0',"
				+ "`inAmount` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`state` int(30) NOT NULL DEFAULT '0',"
				+ "`playerNumber` int(30) NOT NULL DEFAULT '0',"
				+ "`betList`  varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`totalBet` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalPay` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`inventory` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`Odds` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalPlayerNumber` int(30) NOT NULL DEFAULT '0',"
				+ "`winReward` int(30) NOT NULL DEFAULT '0',"
				+ "`checkAmount` double(30,2) NOT NULL DEFAULT '0.00' COMMENT '大额赔付验证金额',"
				+ "`checkOdds` double(30,2) NOT NULL DEFAULT '0.00' COMMENT '大额配置验证赔率',"
				+ "`bigRewardOdds` double(30,2) NOT NULL DEFAULT '0.00' COMMENT '大奖几率',"
				+ "PRIMARY KEY (`gameId`))";
		jdbcTemplate.update(soltInfo);
		
		/*String SoltList = "CREATE TABLE IF NOT EXISTS `SoltList"
		        + "`(`id`  int(30) NOT NULL AUTO_INCREMENT ,"
				+ "`name`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' COMMENT '名称',"
				+ "`code`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`status` int(30) NOT NULL DEFAULT '0' COMMENT '0未激活 1激活',"
				+ "PRIMARY KEY (`id`))"
				+ "ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT"
				;
		jdbcTemplate.update(SoltList);*/
		
		String poolConfig = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_poolconfig"
		        + "`(`gameId` int(30) NOT NULL DEFAULT '0',"
				+ "`gameName`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`poolName`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`initAmount` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`LowBet` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`poolTotalRatio` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`poolOpenLow` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`bonusLv` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`rewardRatio` double(30,2) NOT NULL DEFAULT '0.00')";
				//+ "PRIMARY KEY (`id`))";
		jdbcTemplate.update(poolConfig);
		
		String oddsPoolConfig = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_oddspoolconfig"
		        + "`(`PoolId` int(30) NOT NULL DEFAULT '0',"
				+ "`poolName`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`poolTotalRatio` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`currentAmount` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`state` int(30) NOT NULL DEFAULT '0',"
				+ "`payCount` int(30) NOT NULL DEFAULT '0',"
				+ "`payTotal` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`averageAmount` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`lastPayTime` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"',"
				+ "PRIMARY KEY (`poolName`))";
		jdbcTemplate.update(oddsPoolConfig);
		
		String payPlayerLimit = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_payplayerlimit"
		        + "`(`number` int(30) NOT NULL DEFAULT '0',"
				+ "`totalLowLimit` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`dayLowLimit` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalWaterLow` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`dayWaterLow` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`oddsTotalHight` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`dayOddsHight` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "PRIMARY KEY (`number`))";
		jdbcTemplate.update(payPlayerLimit);
		
		String payLimit = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_paylimit"
		        + "`(`number` int(30) NOT NULL DEFAULT '0',"
				+ "`payLowLimit` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`odds` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`payRatio` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "PRIMARY KEY (`number`))";
		jdbcTemplate.update(payLimit);
		
		
		
		String lhjPoolGameLog = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_poolgamelog` ("
				+ "`id` int(11) NOT NULL AUTO_INCREMENT," 
				+ "`gamename`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`roleid`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`rolenick`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`uuid`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`bet` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`reward` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`RewardBetween`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`todayWin` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalWin` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`rewardtype` int(30) NOT NULL DEFAULT '0',"
				+ "`ispool` int(30) NOT NULL DEFAULT '0',"
				+ "`time`  bigint(30) NOT NULL DEFAULT 0 ,"
				+ "PRIMARY KEY (`id`))";
				//+ "KEY `index_todayWin` (`todayWin`),"
				//+ "KEY `index_totalWin` (`totalWin`),"
				//+ "FOREIGN KEY (`totalWin`) REFERENCES `palyerwaterrecord` (`totalWin`) ON DELETE CASCADE ON UPDATE CASCADE,"
				//+ "FOREIGN KEY (`todayWin`) REFERENCES `palyerwaterrecord` (`todayWin`) ON DELETE CASCADE ON UPDATE CASCADE"
				//+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT";
		jdbcTemplate.update(lhjPoolGameLog);
		
		String palyerWaterRecord = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_palyerwaterrecord"
				+ "`(`roleid`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' COMMENT '玩家id',"
				+ "`rolenick`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`uuid`  varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '\"\"' ,"
				+ "`todayBet` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`todayreward` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`todayWin` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`todayWinlv` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalBet` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalreward` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalWin` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalWinlv` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "PRIMARY KEY (`roleid`),"
				+ "INDEX index_todayWin(todayWin),"
				+ "INDEX index_totalWin(totalWin))"
				+ "ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT"
				;
		jdbcTemplate.update(palyerWaterRecord);
		
		String totalWaterRecord = "CREATE TABLE IF NOT EXISTS `t_dmg_lhj_totalwaterrecord"
				+ "`(`tableName`  varchar(30) NOT NULL DEFAULT '\"\"' COMMENT '表名',"
				+ "`todayBet` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`todayreward` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`todayWin` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalBet` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalreward` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "`totalWin` double(30,2) NOT NULL DEFAULT '0.00',"
				+ "PRIMARY KEY (`tableName`),"
				+ "INDEX index_todayBet(todayBet),"
				+ "INDEX index_totalBet(totalBet))"
				+ "ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT"
				;
		jdbcTemplate.update(totalWaterRecord);
		
	}
}
