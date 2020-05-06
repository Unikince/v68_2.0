package com.zyhy.lhj_server.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.manager.CacheManager;
import com.zyhy.lhj_server.bgmanagement.manager.SpringContextUtil;
import com.zyhy.lhj_server.bgmanagement.work.IQueueType;
import com.zyhy.lhj_server.bgmanagement.work.QueueType;
import com.zyhy.lhj_server.bgmanagement.work.Work;

/**
 * @author zhuqd
 * @Date 2017年8月10日
 * @Desc 更新库存信息
 */
public class UpdaeInventory extends Work {
	private static BgManagementDaoImp bgManagementDaoImp = SpringContextUtil.getBean(BgManagementDaoImp.class);
	private static StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
	private static final Logger LOG = LoggerFactory.getLogger(UpdaeInventory.class);
	private GameRecordDTO log = null;
	@Override
	public void init(Object... args) {
		log = (GameRecordDTO) args[0];
	}
	@Override
	public IQueueType queue() {
		return QueueType.SQL;
	}
	@Override
	public void go() {
		try {
			long startTime = System.currentTimeMillis();
			SoltGameInfo gameInfo = CacheManager.instance().getGameInfo(MessageIdEnum.getRedisNamebyName(log.getGameName()));
			bgManagementDaoImp.updateSoltInventoryInfo(gameInfo);
			// 将当前游戏库存值转存到百人场用于统计(目前只用于统计使用)
			redisTemplate.opsForValue().set(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + log.getGameId() + "_" + 1, String.valueOf(gameInfo.getInventory()));
			long endTime = System.currentTimeMillis();
			LOG.info("UpdateSoltInventoryInfo =====> Success! , time : " + (endTime - startTime) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("UpdateSoltInventoryInfo =====> failed! ");
		}
	}
}
