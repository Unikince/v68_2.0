package com.dmg.zhajinhuaserver.process;
import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.QueryRoundRecordService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.QUERY_ROUND;

/**
 * @description: 查询战绩
 * @return
 * @author mice
 * @date 2019/7/10
*/
@Service
public class QueryRoundRecordProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return QUERY_ROUND;
    }

    @Autowired
    private QueryRoundRecordService queryRoundRecordService;
    @Autowired
    private PlayerService playerCacheService;

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        int type = params.getIntValue("type");
        queryRoundRecordService.queryRoundRecord(player, type);

    }
}
