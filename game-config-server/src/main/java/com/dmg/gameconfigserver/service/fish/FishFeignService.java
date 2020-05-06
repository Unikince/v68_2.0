package com.dmg.gameconfigserver.service.fish;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.dao.fish.FishCtrlReturnRateDao;
import com.dmg.gameconfigserver.dao.fish.FishCtrlStockDao;
import com.dmg.gameconfigserver.dao.fish.FishDao;
import com.dmg.gameconfigserver.dao.fish.FishRoomDao;
import com.dmg.gameconfigserver.dao.fish.FishRouteDao;
import com.dmg.gameconfigserver.dao.fish.FishScenceDao;
import com.dmg.gameconfigserver.dao.fish.FishStrategyDao;
import com.dmg.gameconfigserver.model.bean.fish.FishBean;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlReturnRateBean;
import com.dmg.gameconfigserver.model.bean.fish.FishCtrlStockBean;
import com.dmg.gameconfigserver.model.bean.fish.FishRoomBean;
import com.dmg.gameconfigserver.model.bean.fish.FishRouteBean;
import com.dmg.gameconfigserver.model.bean.fish.FishScenceBean;
import com.dmg.gameconfigserver.model.bean.fish.FishStrategyBean;
import com.dmg.gameconfigserver.model.vo.config.rate.GameExchangeRateVO;
import com.dmg.gameconfigserver.service.config.GameExchangeRateService;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlStockDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishRoomDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishRouteDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishScenceDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishStrategyDTO;
import com.dmg.gameconfigserverapi.fish.feign.FishConfigRedis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;

/**
 * 捕鱼配置获取服务
 */
@Service
public class FishFeignService {
    @Autowired
    private FishDao fishDao;
    @Autowired
    private FishRoomDao fishRoomDao;
    @Autowired
    private FishCtrlReturnRateDao fishCtrlReturnRateDao;
    @Autowired
    private FishCtrlStockDao fishCtrlStockDao;
    @Autowired
    private FishRouteDao fishRouteDao;
    @Autowired
    private FishScenceDao fishScenceDao;
    @Autowired
    private FishStrategyDao fishStrategyDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${exchange-rate-code}")
    private String exchangeRateCode;
    @Autowired
    private GameExchangeRateService gameExchangeRateService;

    /** 鱼 */
    public List<FishDTO> getFish() {
        List<FishDTO> dtos = new ArrayList<>();
        List<FishBean> beans = this.fishDao.selectList(new LambdaQueryWrapper<>());
        for (FishBean bean : beans) {
            FishDTO dto = new FishDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    /** 房间配置 */
    public List<FishRoomDTO> getFishRoom() {
        GameExchangeRateVO gameExchangeRateVo = this.gameExchangeRateService.getExchangeRateByCode(this.exchangeRateCode);
        List<FishRoomDTO> dtos = new ArrayList<>();
        List<FishRoomBean> beans = this.fishRoomDao.selectList(new LambdaQueryWrapper<>());
        for (FishRoomBean bean : beans) {
            FishRoomDTO dto = new FishRoomDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);

            if (gameExchangeRateVo != null) {
                // 炮台分数
                List<Integer> batteryScores = JSON.parseArray(bean.getBatteryScores(), Integer.class);
                for (int i = 0; i < batteryScores.size(); i++) {
                    Integer batteryScore = batteryScores.get(i);
                    batteryScore = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(batteryScore)).intValue();
                    batteryScores.set(i, batteryScore);
                }
                dto.setBatteryScores(JSON.toJSONString(batteryScores));

                // 入场金币下限
                Long goldLimitLower = bean.getGoldLimitLower();
                goldLimitLower = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(goldLimitLower)).longValue();
                dto.setGoldLimitLower(goldLimitLower);

                // 入场金币上限
                Long goldLimitUpper = bean.getGoldLimitUpper();
                goldLimitUpper = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(goldLimitUpper)).longValue();
                if (goldLimitUpper <= 0) {
                    goldLimitUpper = Long.MAX_VALUE;
                }
                dto.setGoldLimitUpper(goldLimitUpper);

                // 大额赔付值
                Long bigPayoutValue = bean.getBigPayoutValue();
                bigPayoutValue = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(bigPayoutValue)).longValue();
                dto.setBigPayoutValue(bigPayoutValue);

                // 机器人进入游戏分数下限
                Long robotEnterPointLower = bean.getRobotEnterPointLower();
                robotEnterPointLower = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(robotEnterPointLower)).longValue();
                dto.setRobotEnterPointLower(robotEnterPointLower);

                // 机器人进入游戏分数上限
                Long robotEnterPointUpper = bean.getRobotEnterPointUpper();
                robotEnterPointUpper = gameExchangeRateVo.getExchangeRate().multiply(new BigDecimal(robotEnterPointUpper)).longValue();
                if (robotEnterPointUpper <= 0) {
                    robotEnterPointUpper = Long.MAX_VALUE;
                }
                dto.setRobotEnterPointUpper(robotEnterPointUpper);
            }

        }
        return dtos;
    }

    /** 返奖率控制 */
    public List<FishCtrlReturnRateDTO> getFishCtrlReturnRate() {
        List<FishCtrlReturnRateDTO> dtos = new ArrayList<>();
        List<FishCtrlReturnRateBean> beans = this.fishCtrlReturnRateDao.selectList(new LambdaQueryWrapper<>());
        for (FishCtrlReturnRateBean bean : beans) {
            FishCtrlReturnRateDTO dto = new FishCtrlReturnRateDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    /** 库存控制 */
    public List<FishCtrlStockDTO> getFishCtrlStock() {
        List<FishCtrlStockDTO> dtos = new ArrayList<>();
        List<FishCtrlStockBean> beans = this.fishCtrlStockDao.selectList(new LambdaQueryWrapper<>());
        for (FishCtrlStockBean bean : beans) {
            FishCtrlStockDTO dto = new FishCtrlStockDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    /** 库存控制更新 */
    public void updateFishCtrlStock(Map<String, String> params) {
        List<FishCtrlStockBean> beans = this.fishCtrlStockDao.selectList(new LambdaQueryWrapper<>());
        boolean updateFlag = false;// 是否需要通知更新
        for (FishCtrlStockBean bean : beans) {
            if (!bean.isStatus()) {// 如果是关闭则不需要修改
                continue;
            }
            String changeStr = params.get("" + bean.getId());
            if (StringUtils.isBlank(changeStr)) {// 没有更新的分数，直接跳过
                continue;
            }
            Long change = Long.parseLong(changeStr);
            if (change > 0) {
                this.redisUtil.incr(FishCtrlStockDTO.CHANGE_REDIS_KEY + bean.getId(), change + 0.0);
            } else {
                this.redisUtil.decr(FishCtrlStockDTO.CHANGE_REDIS_KEY + bean.getId(), -change + 0.0);
            }
            String ctrlStockNumStr = (String) this.redisUtil.get(FishCtrlStockDTO.CHANGE_REDIS_KEY + bean.getId());
            Long ctrlStockNum = Long.parseLong(ctrlStockNumStr);
            long target = bean.getTarget();
            if ((target > 0 && ctrlStockNum >= target) || (target < 0 && ctrlStockNum <= target)) {
                bean.setStatus(false);
                this.fishCtrlStockDao.updateById(bean);
                updateFlag = true;
                this.redisUtil.set(FishCtrlStockDTO.CHANGE_REDIS_KEY + bean.getId(), "0.0");
            }
        }

        if (updateFlag) {
            this.stringRedisTemplate.convertAndSend(FishConfigRedis.FISH_CTRL_STOCK, "");
            this.stringRedisTemplate.opsForList().rightPush(FishConfigRedis.FISH_CTRL_STOCK, DateUtil.now());
        }
    }

    /** 鱼路线图 */
    public List<FishRouteDTO> getFishRoute() {
        List<FishRouteDTO> dtos = new ArrayList<>();
        List<FishRouteBean> beans = this.fishRouteDao.selectList(new LambdaQueryWrapper<>());
        for (FishRouteBean bean : beans) {
            FishRouteDTO dto = new FishRouteDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    /** 场景 */
    public List<FishScenceDTO> getFishScence() {
        List<FishScenceDTO> dtos = new ArrayList<>();
        List<FishScenceBean> beans = this.fishScenceDao.selectList(new LambdaQueryWrapper<>());
        for (FishScenceBean bean : beans) {
            FishScenceDTO dto = new FishScenceDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);
        }
        return dtos;
    }

    /** 刷鱼策略 */
    public List<FishStrategyDTO> getFishStrategy() {
        List<FishStrategyDTO> dtos = new ArrayList<>();
        List<FishStrategyBean> beans = this.fishStrategyDao.selectList(new LambdaQueryWrapper<>());
        for (FishStrategyBean bean : beans) {
            FishStrategyDTO dto = new FishStrategyDTO();
            BeanUtil.copyProperties(bean, dto);
            dtos.add(dto);
        }
        return dtos;
    }
}