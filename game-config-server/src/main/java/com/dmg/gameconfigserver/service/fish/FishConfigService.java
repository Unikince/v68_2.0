package com.dmg.gameconfigserver.service.fish;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.dao.fish.FishCtrlReturnRateDao;
import com.dmg.gameconfigserver.dao.fish.FishCtrlStockDao;
import com.dmg.gameconfigserver.dao.fish.FishRoomDao;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlReturnRateBean;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlStockBean;
import com.dmg.gameconfigserver.model.bean.fish.FishRoomBean;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomConfigGetReq;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomConfigGetRes;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomConfigUpdateReq;
import com.dmg.gameconfigserver.model.vo.fish.FishRoomInfoGetRes;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlStockDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigRedis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;

/**
 * 捕鱼配置服务
 */
@Service
public class FishConfigService {
    @Autowired
    private FishRoomDao fishRoomDao;
    @Autowired
    private FishCtrlReturnRateDao fishCtrlReturnRateDao;
    @Autowired
    private FishCtrlStockDao fishCtrlStockDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    /** 游戏id */
    Integer gameId = 9;

    /** 获取捕鱼房间信息 */
    public List<FishRoomInfoGetRes> getFishRoomInfo() {
        List<FishRoomBean> beans = this.fishRoomDao.selectList(new LambdaQueryWrapper<>());
        List<FishRoomInfoGetRes> resVos = new ArrayList<>();
        for (FishRoomBean bean : beans) {
            FishRoomInfoGetRes resVo = new FishRoomInfoGetRes();
            resVos.add(resVo);
            BeanUtil.copyProperties(bean, resVo);

            Integer curPersionNum = 0;// 当前玩家数量
            String playerNumStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + bean.getId());
            if (StringUtils.isNotBlank(playerNumStr)) {
                curPersionNum = Integer.parseInt(playerNumStr);
            }
            resVo.setCurPersionNum(curPersionNum);

            Integer curRobotNum = 0;// 当前机器人数量
            String curRobotNumStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + bean.getId());
            if (StringUtils.isNotBlank(curRobotNumStr)) {
                curRobotNum = Integer.parseInt(curRobotNumStr);
            }
            resVo.setCurRobotNum(curRobotNum);

            Long sumBet = 0L;// 总下注
            String sumBetStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_BET_GOLD_KEY + ":" + this.gameId + "_" + bean.getId());
            if (StringUtils.isNotBlank(sumBetStr)) {
                sumBet = Long.parseLong(sumBetStr);
            }
            resVo.setSumBet(sumBet);

            Long sumPay = 0L;// 总赔付
            String sumPayStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PAYOUT_GOLD_KEY + ":" + this.gameId + "_" + bean.getId());
            if (StringUtils.isNotBlank(sumPayStr)) {
                sumPay = Long.parseLong(sumPayStr);
            }
            resVo.setSumPay(sumPay);

            BigDecimal returnRate = BigDecimal.ZERO;// 当前赔率
            if (sumBet != 0) {
                returnRate = new BigDecimal(sumPay).divide(new BigDecimal(sumBet), 6, BigDecimal.ROUND_HALF_UP);
            }
            resVo.setReturnRate(returnRate);

            Long sumWin = 0L;// 当前库存
            String sumWinStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + bean.getId());
            if (StringUtils.isNotBlank(sumWinStr)) {
                sumWin = Long.parseLong(sumWinStr);
            }
            resVo.setSumWin(sumWin);

            FishCtrlStockBean ctrlStockBean = this.fishCtrlStockDao.selectById(bean.getId());
            if (ctrlStockBean.isStatus()) {
                Long ctrlStockNum = 0L;// 控制库存当前值
                String ctrlStockNumStr = (String) this.redisUtil.get(FishCtrlStockDTO.CHANGE_REDIS_KEY + bean.getId());
                if (StringUtils.isNotBlank(ctrlStockNumStr)) {
                    ctrlStockNum = Long.parseLong(ctrlStockNumStr);
                }
                resVo.setCtrlStockNum(ctrlStockNum);
                resVo.setCtrlStockMax(ctrlStockBean.getTarget());// 控制库存最大值
            } else {
                resVo.setCtrlStockNum(0L);// 控制库存当前值
                resVo.setCtrlStockMax(0L);// 控制库存最大值
            }

        }
        return resVos;
    }

    /** 获取捕鱼房间配置 */
    public FishRoomConfigGetRes getFishRoomConfig(FishRoomConfigGetReq reqVo) {
        FishRoomBean bean = this.fishRoomDao.selectById(reqVo.getId());
        if (bean == null) {
            return new FishRoomConfigGetRes();
        }
        FishRoomConfigGetRes resVo = new FishRoomConfigGetRes();
        BeanUtil.copyProperties(bean, resVo);

        List<Integer> batteryScores = JSON.parseArray(bean.getBatteryScores(), Integer.class);// 炮台分数
        resVo.setBatteryScores(batteryScores);

        return resVo;
    }

    /** 修改捕鱼房间配置 */
    public void updateFishRoomConfig(FishRoomConfigUpdateReq reqVo) {
        FishRoomBean bean = this.fishRoomDao.selectById(reqVo.getId());
        if (bean == null) {
            return;
        }
        BeanUtil.copyProperties(reqVo, bean);
        bean.setBatteryScores(JSON.toJSONString(reqVo.getBatteryScores()));
        this.fishRoomDao.updateById(bean);
        this.stringRedisTemplate.convertAndSend(FishConfigRedis.FISH_ROOM, "");
        this.stringRedisTemplate.opsForList().rightPush(FishConfigRedis.FISH_ROOM, DateUtil.now());
    }

    /** 获取捕鱼返奖率控制 */
    public List<FishCtrlReturnRateBean> getFishCtrlReturnRate() {
        List<FishCtrlReturnRateBean> beans = this.fishCtrlReturnRateDao.selectList(new LambdaQueryWrapper<>());
        if (beans == null) {
            return new ArrayList<>();
        }
        return beans;
    }

    /** 修改捕鱼返奖率控制 */
    public void updateFishCtrlReturnRate(List<FishCtrlReturnRateBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }
        for (int i = 0; i < beans.size(); i++) {
            FishCtrlReturnRateBean bean = beans.get(i);
            bean.setId(i + 1);
            this.fishCtrlReturnRateDao.updateById(bean);
        }
        this.stringRedisTemplate.convertAndSend(FishConfigRedis.FISH_CTRL_RETURN_RATE, "");
        this.stringRedisTemplate.opsForList().rightPush(FishConfigRedis.FISH_CTRL_RETURN_RATE, DateUtil.now());
    }

    /** 获取捕鱼库存控制 */
    public FishCtrlStockBean getFishCtrlStock(Integer id) {
        if (id == null) {
            return null;
        }
        return this.fishCtrlStockDao.selectById(id);
    }

    /** 获取捕鱼库存控制 */
    public void updateFishCtrlStock(FishCtrlStockBean bean) {
        if (bean == null) {
            return;
        }
        if (bean.getModel() > 7 || bean.getModel() <= 0 || bean.getTarget() == 0) {
            bean.setModel(0);
            bean.setStatus(false);
        } else {
            bean.setStatus(true);
        }
        this.fishCtrlStockDao.updateById(bean);
        this.stringRedisTemplate.convertAndSend(FishConfigRedis.FISH_CTRL_STOCK, "");
        this.stringRedisTemplate.opsForList().rightPush(FishConfigRedis.FISH_CTRL_STOCK, DateUtil.now());
        this.redisUtil.set(FishCtrlStockDTO.CHANGE_REDIS_KEY + bean.getId(), "0");
    }
}