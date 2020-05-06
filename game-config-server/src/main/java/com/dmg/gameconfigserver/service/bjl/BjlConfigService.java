package com.dmg.gameconfigserver.service.bjl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.dao.bjl.BjlTableDao;
import com.dmg.gameconfigserverapi.bjl.feign.BjlConfigRedis;

import cn.hutool.core.date.DateUtil;

/**
 * 百家乐配置服务
 */
@Service
public class BjlConfigService {
    @Autowired
    private BjlTableDao bjlTableDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    /** 游戏id */
    Integer gameId = 9;

    public void update() {
        this.stringRedisTemplate.convertAndSend(BjlConfigRedis.BJL_TABLE, "");
        this.stringRedisTemplate.opsForList().rightPush(BjlConfigRedis.BJL_TABLE, DateUtil.now());
    }

//    /** 获取捕鱼房间信息 */
//    public List<FishRoomInfoGetRes> getFishRoomInfo() {
//        List<FishRoomBean> beans = this.fishRoomDao.selectList(new LambdaQueryWrapper<>());
//        List<FishRoomInfoGetRes> resVos = new ArrayList<>();
//        for (FishRoomBean bean : beans) {
//            FishRoomInfoGetRes resVo = new FishRoomInfoGetRes();
//            resVos.add(resVo);
//            BeanUtil.copyProperties(bean, resVo);
//
//            Integer curPersionNum = 0;// 当前玩家数量
//            String playerNumStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + bean.getId());
//            if (StringUtils.isNotBlank(playerNumStr)) {
//                curPersionNum = Integer.parseInt(playerNumStr);
//            }
//            resVo.setCurPersionNum(curPersionNum);
//
//            Integer curRobotNum = 0;// 当前机器人数量
//            String curRobotNumStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + bean.getId());
//            if (StringUtils.isNotBlank(curRobotNumStr)) {
//                curRobotNum = Integer.parseInt(curRobotNumStr);
//            }
//            resVo.setCurRobotNum(curRobotNum);
//
//            Long sumBet = 0L;// 总下注
//            String sumBetStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_BET_GOLD_KEY + ":" + this.gameId + "_" + bean.getId());
//            if (StringUtils.isNotBlank(sumBetStr)) {
//                sumBet = Long.parseLong(sumBetStr);
//            }
//            resVo.setSumBet(sumBet);
//
//            Long sumPay = 0L;// 总赔付
//            String sumPayStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PAYOUT_GOLD_KEY + ":" + this.gameId + "_" + bean.getId());
//            if (StringUtils.isNotBlank(sumPayStr)) {
//                sumPay = Long.parseLong(sumPayStr);
//            }
//            resVo.setSumPay(sumPay);
//
//            BigDecimal returnRate = BigDecimal.ZERO;// 当前赔率
//            if (sumBet != 0) {
//                returnRate = new BigDecimal(sumPay).divide(new BigDecimal(sumBet), 6, BigDecimal.ROUND_HALF_UP);
//            }
//            resVo.setReturnRate(returnRate);
//
//            Long sumWin = 0L;// 当前库存
//            String sumWinStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + bean.getId());
//            if (StringUtils.isNotBlank(sumWinStr)) {
//                sumWin = Long.parseLong(sumWinStr);
//            }
//            resVo.setSumWin(sumWin);
//        }
//        return resVos;
//    }
//
//    /** 获取捕鱼房间配置 */
//    public FishRoomConfigGetRes getFishRoomConfig(FishRoomConfigGetReq reqVo) {
//        FishRoomBean bean = this.fishRoomDao.selectById(reqVo.getId());
//        if (bean == null) {
//            return new FishRoomConfigGetRes();
//        }
//        FishRoomConfigGetRes resVo = new FishRoomConfigGetRes();
//        BeanUtil.copyProperties(bean, resVo);
//
//        List<Integer> batteryScores = JSON.parseArray(bean.getBatteryScores(), Integer.class);// 炮台分数
//        resVo.setBatteryScores(batteryScores);
//
//        return resVo;
//    }
//
//    /** 修改捕鱼房间配置 */
//    public void updateFishRoomConfig(FishRoomConfigUpdateReq reqVo) {
//        FishRoomBean bean = this.fishRoomDao.selectById(reqVo.getId());
//        if (bean == null) {
//            return;
//        }
//        BeanUtil.copyProperties(reqVo, bean);
//        bean.setBatteryScores(JSON.toJSONString(reqVo.getBatteryScores()));
//        this.fishRoomDao.updateById(bean);
//        this.stringRedisTemplate.convertAndSend(FishConfigRedis.FISH_ROOM, "");
//        this.stringRedisTemplate.opsForList().rightPush(FishConfigRedis.FISH_ROOM, DateUtil.now());
//    }
//
//    /** 获取捕鱼库存配置 */
//    public List<FishStockConfigGetRes> getFishStockConfig(FishStockConfigGetReq reqVo) {
//        FishRoomBean bean = this.fishRoomDao.selectById(reqVo.getId());
//        if (bean == null) {
//            return new ArrayList<>();
//        }
//        List<FishStockConfigGetRes> resVos = JSON.parseArray(bean.getStocks(), FishStockConfigGetRes.class);
//        Collections.sort(resVos, new Comparator<FishStockConfigGetRes>() {
//            @Override
//            public int compare(FishStockConfigGetRes o1, FishStockConfigGetRes o2) {
//                return o1.getLower() - o2.getLower();// 赔率下限排序
//            }
//        });
//        return resVos;
//    }
//
//    /** 修改捕鱼库存配置 */
//    public void updateFishStockConfig(FishStockConfigUpdateReq reqVo) {
//        FishRoomBean bean = this.fishRoomDao.selectById(reqVo.getId());
//        if (bean == null) {
//            return;
//        }
//        List<FishStockConfigVo> stocks = reqVo.getStocks();
//        bean.setStocks(JSON.toJSONString(stocks));
//        this.fishRoomDao.updateById(bean);
//        this.stringRedisTemplate.convertAndSend(FishConfigRedis.FISH_ROOM, "");
//        this.stringRedisTemplate.opsForList().rightPush(FishConfigRedis.FISH_ROOM, DateUtil.now());
//    }
}