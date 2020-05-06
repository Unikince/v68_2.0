package com.zyhy.lhj_server.bgmanagement.prcess.work;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyhy.common_server.model.GameLhjLog;
import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;
import com.zyhy.lhj_server.bgmanagement.entity.OddsPoolConfig;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.entity.palyerWaterRecord;
import com.zyhy.lhj_server.bgmanagement.entity.totalWaterRecord;
import com.zyhy.lhj_server.bgmanagement.manager.SpringContextUtil;
import com.zyhy.lhj_server.bgmanagement.work.IQueueType;
import com.zyhy.lhj_server.bgmanagement.work.QueueType;
import com.zyhy.lhj_server.bgmanagement.work.Work;

/**
 * @author zhuqd
 * @Date 2017年8月10日
 * @Desc 更新库存信息
 */
public class updaeInventory extends Work {
	private static BgManagementDaoImp bgManagementDaoImp = SpringContextUtil.getBean(BgManagementDaoImp.class);
	private static final Logger LOG = LoggerFactory.getLogger(updaeInventory.class);
	private GameLhjLog log = null;
	@Override
	public void init(Object... args) {
		log = (GameLhjLog) args[0];
	}
	@Override
	public IQueueType queue() {
		return QueueType.SQL;
	}
	@Override
	public void go() {
		try {
			long startTime = System.currentTimeMillis();
			// TODO 赔率奖池
			OddsPoolConfig oddsPoolConfig = bgManagementDaoImp.queryOddsPoolInfo(); // 奖池配置信息
			
			if (log.getIspool() == 1) {
				// 转存赔率奖池日志
				bgManagementDaoImp.createPoolGameLhjLog(log);
				oddsPoolConfig.setPayCount(oddsPoolConfig.getPayCount() + 1); // 总派奖次数
				oddsPoolConfig.setPayTotal(oddsPoolConfig.getPayTotal() + log.getReward()); // 总奖励
				oddsPoolConfig.setCurrentAmount(oddsPoolConfig.getCurrentAmount() - log.getReward()); // 赔率奖池当前金额
				// 最后一次派奖时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm");
				Date date = new Date(log.getTime());
				String format = sdf.format(date);
				oddsPoolConfig.setLastPayTime(format);
			}
			
			// 更新赔率奖池库存信息
			double poolTotalRatio = oddsPoolConfig.getPoolTotalRatio(); // 累计比例
			double total = log.getBet() * poolTotalRatio; // 本次累计金额
			oddsPoolConfig.setCurrentAmount(oddsPoolConfig.getCurrentAmount() + total);  //  赔率奖池总金额
			double averageAmount = 0;
			if (oddsPoolConfig.getPayTotal() != 0 && oddsPoolConfig.getPayCount() != 0) {
				averageAmount = new BigDecimal(oddsPoolConfig.getPayTotal()/oddsPoolConfig.getPayCount())
						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			oddsPoolConfig.setAverageAmount(averageAmount); // 平均派奖额度
			bgManagementDaoImp.updateOddsPool(oddsPoolConfig); 
			
			// TODO 更新单个游戏库存信息
			SoltGameInfo gameInfo = bgManagementDaoImp.querySingleGameInfo(log.getGamename());
			gameInfo.setTotalBet(gameInfo.getTotalBet() + log.getBet()); // 更新总下注
			if (log.getIspool() != 1) {
				gameInfo.setTotalPay(gameInfo.getTotalPay() + log.getReward()); // 更新总派彩
			}
			gameInfo.setInventory(gameInfo.getTotalBet() - gameInfo.getTotalPay()); // 更新库存
			// 更新赔率
			gameInfo.setOdds(gameInfo.getTotalPay()/gameInfo.getTotalBet());
			
			bgManagementDaoImp.updateSoltInventoryInfo(gameInfo);
			
			// TODO 更新玩家总流水
			palyerWaterRecord pr = new palyerWaterRecord();
			pr.setRoleid(log.getRoleid());
			pr.setRolenick(log.getRolenick());
			pr.setUuid(log.getUuid());
			pr.setTodayBet(log.getBet());
			pr.setTodayreward(log.getReward());
			pr.setTodayWin(log.getReward()-log.getBet());
			pr.setTotalBet(log.getBet());
			pr.setTotalreward(log.getReward());
			pr.setTotalWin(log.getReward()-log.getBet());
			bgManagementDaoImp.addPlayerWaterRecord(pr);
			// TODO 更新总库存表
			totalWaterRecord tr = new totalWaterRecord();
			tr.setTableName("totalwater");
			tr.setTodayBet(log.getBet());
			tr.setTodayreward(log.getReward());
			tr.setTodayWin(log.getReward()-log.getBet());
			tr.setTotalBet(log.getBet());
			tr.setTotalreward(log.getReward());
			tr.setTotalWin(log.getReward()-log.getBet());
			bgManagementDaoImp.addTotalWaterRecord(tr);
			long endTime = System.currentTimeMillis();
			//LOG.info("UpdateSoltInventoryInfo Success!========> ! time : " + (endTime - startTime) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
