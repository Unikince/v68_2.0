package com.dmg.bjlserver.business.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.model.game.SeatRecord;

import cn.hutool.core.date.DateUtil;

@Component
public class GameHistory {
    @Autowired
    private RedisTemplate<String, SeatRecord> seatRecordTemplate;
    @Autowired
    private RedisTemplate<String, Long> longRedisTemplatea;

    private static final String GAME_ID = "bjl";
    private static final int MAX_GAME_HISTORY_QUERY = 50; // 记录玩家最近多少次游戏记录
    private static final int MAX_RANK_PER_QUERY = 30; // 大赢家显示前多少位玩家的信息

    private String getRankKey(int roomId, int tableId) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YY-MM-dd");
        String date = sdf.format(now);
        return String.format("%s:rank:room_%d:table_%d:%s", GAME_ID, tableId, roomId, date);
    }

    private String getPlayerHistoryKey(Long playerId) {
        return String.format("%s:PlayerHistory:%d", GAME_ID, playerId);
    }

    /**
     * 给指定玩家添加游戏记录
     *
     * @param playerId 玩家ID
     * @param record 游戏记录
     */
    public void addGameHistory(long playerId, SeatRecord record) {
        String key = this.getPlayerHistoryKey(playerId);
        ListOperations<String, SeatRecord> listOp = this.seatRecordTemplate.opsForList();
        listOp.leftPush(key, record);
        listOp.trim(key, 0, MAX_GAME_HISTORY_QUERY);
        this.seatRecordTemplate.expire(key, 15, TimeUnit.DAYS); // 15天过期
    }

    /**
     * 获取个人的所有战绩
     */
    public List<SeatRecord> queryGameHistory(long playerId) {
        String key = this.getPlayerHistoryKey(playerId);
        ListOperations<String, SeatRecord> listOp = this.seatRecordTemplate.opsForList();
        return listOp.range(key, 0, MAX_GAME_HISTORY_QUERY);
    }

    /**
     * 更新大赢家（按日）排行榜
     *
     * @param playerId 玩家ID
     * @param increment_gold 增量gold
     * @return 变更后的统计结果
     */
    public Long updatePlayerDayGold(int roomId, int tableId, long playerId, long increment_gold) {
        String key = this.getRankKey(roomId, tableId);
        ZSetOperations<String, Long> zSetOp = this.longRedisTemplatea.opsForZSet();
        Double result = zSetOp.incrementScore(key, playerId, increment_gold);
        this.longRedisTemplatea.expireAt(key, DateUtil.endOfDay(Calendar.getInstance().getTime())); // 当天晚上0点过期
        if (result == null) {
            return 0L;
        }
        return result.longValue();
    }

    /**
     * 查询大赢家（按日）排行榜
     */
    public Set<ZSetOperations.TypedTuple<Long>> queryRankProfit(int roomId, int tableId, int min, int max) {
        if ((max - min) > GameHistory.MAX_RANK_PER_QUERY) {
            max = min + GameHistory.MAX_RANK_PER_QUERY;
        }

        String key = this.getRankKey(roomId, tableId);
        ZSetOperations<String, Long> zSetOperations = this.longRedisTemplatea.opsForZSet();
        return zSetOperations.reverseRangeWithScores(key, min, max);
    }
}
