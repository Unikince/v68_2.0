package com.zyhy.lhj_server.manager.timerWork.work;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.dmg.common.core.config.RedisRegionConfig;
import com.zyhy.lhj_server.bgmanagement.manager.SpringContextUtil;
import com.zyhy.lhj_server.manager.timerWork.annotation.Cron;
import com.zyhy.lhj_server.manager.timerWork.model.TimeWork;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author zhuqd
 * @Date 2017年9月5日
 * @Desc 检测在线人数
 */
@Cron("0/20 * * * * ?" )
public class LhjOnlineNum extends TimeWork {
	private static final Logger LOG = LoggerFactory.getLogger(LhjOnlineNum.class);
	@Override
	public void init(Object... args) {
	}

	@Override
	public void go() {
		StringRedisTemplate redisTemplate = SpringContextUtil.getBean(StringRedisTemplate.class);
		UserService userService = SpringContextUtil.getBean(UserService.class);
		Map<Integer, Map<String, Long>> userHeartInfo = userService.getUserHeartInfo();
		LOG.info("=====> 开始检测心跳");
    	// 超时时间
    	long timeout = 10*1000; 
    	if (userHeartInfo.size() > 0) {
        	for (Map<String, Long> map : userHeartInfo.values()) {
        		for (String roleId : map.keySet()) {
        			if (System.currentTimeMillis() -  map.get(roleId) >= timeout) {
        				map.remove(roleId);
        				LOG.info("LhjOnlineNum =====> 玩家:{} 已经超过{}ms未发送心跳,判断为退出游戏!",roleId);
        			}
				}
        	}	
    	}
		
    	// 更新在线人数
    	for (Integer gameId : userHeartInfo.keySet()) {
    		redisTemplate.opsForValue().set(RedisRegionConfig.ONLINE_NUM_KEY + ":" + gameId + "_" + 1,String.valueOf(userHeartInfo.get(gameId).size()));
    		LOG.info(" LhjOnlineNum =====> gameId: {} ,在线人数: {}!", gameId,userHeartInfo.get(gameId).size());
		}
	}
}
