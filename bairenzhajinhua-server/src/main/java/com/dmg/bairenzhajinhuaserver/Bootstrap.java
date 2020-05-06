package com.dmg.bairenzhajinhuaserver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dmg.server.common.util.GameOnlineChangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dmg.bairenzhajinhuaserver.common.exception.BusinessException;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.common.utils.SpringContextUtil;
import com.dmg.bairenzhajinhuaserver.common.work.QueueType;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.manager.TimerManager;
import com.dmg.bairenzhajinhuaserver.manager.WorkManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.constants.Combination;
import com.dmg.bairenzhajinhuaserver.service.logic.RoomActionService;
import com.dmg.bairenzhajinhuaserver.sysconfig.RegionConfig;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.SpringUtil;
import com.dmg.gameconfigserverapi.dto.BairenGameConfigDTO;
import com.dmg.gameconfigserverapi.feign.GameConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 10:11
 * @Version V1.0
 **/
@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {
    @Value("classpath:roomconfig.json")
    private Resource roomconfig;
    @Value("classpath:systemwinconfig.json")
    private Resource systemWinConfig;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${game-id}")
    private String gameId;

    @Override
    public void run(String... args) throws Exception {
        this.cleanCache();
        TimerManager.instance().init();
        WorkManager.instance().init(QueueType.values());
        int[] a = new int[5];
        for (int i = 0; i < 5; i++) {
            a[i] = i + 1;
        }
        this.initFullyArranged(RoomManager.intance().getFullyArrangedMap(), a, 5, 0);
        log.info("全排列初始化完成:{}", JSON.toJSONString(RoomManager.intance().getFullyArrangedMap()));
        this.initsystemWinConfig();
        this.initRoomconfig();
        this.initRoom();
    }

    public void cleanCache() {
        Set<String> robotkeys = redisTemplate.keys(RegionConfig.ROBOT_REDIS_KEY+"*");
        if (CollectionUtils.isNotEmpty(robotkeys)) {
            redisTemplate.delete(robotkeys);
        }
        Set<String> playerkeys = redisTemplate.keys(RegionConfig.PLAYER_REDIS_KEY+"*");
        if (CollectionUtils.isNotEmpty(playerkeys)) {
            redisTemplate.delete(playerkeys);
        }
    }

    /**
     * @param
     * @return void
     * @description: 初始化系统赢钱几率配置
     * @author mice
     * @date 2019/7/30
     */
    public void initsystemWinConfig() {
        GameConfigService gameConfigService = SpringUtil.getBean(GameConfigService.class);
        List<BairenGameConfigDTO> bairenGameConfigDTOS = gameConfigService.getBairenGameConfigData(this.gameId);
        RoomManager.intance().getGameConfigMap().clear();
        Map<Integer, BairenGameConfigDTO> bairenGameConfigDTOMap = new HashMap<>();
        for (BairenGameConfigDTO bairenGameConfigDTO : bairenGameConfigDTOS) {
            if (bairenGameConfigDTO.getBairenFileConfigDTO().getOpenStatus() == 1) {
                bairenGameConfigDTOMap.put(bairenGameConfigDTO.getFileId(), bairenGameConfigDTO);
                RoomManager.intance().setExchangeRate(bairenGameConfigDTO.getExchangeRate());
            }
        }
        RoomManager.intance().setGameConfigMap(bairenGameConfigDTOMap);
        log.info("==>初始化系统水池配置完成");
    }

    /**
     * @param
     * @return void
     * @description: 初始化房间配置
     * @author mice
     * @date 2019/7/30
     */
    public void initRoomconfig() {
        Set<Integer> keys = RoomManager.intance().getGameConfigMap().keySet();
        RoomManager manager = RoomManager.intance();
        manager.setGameId(this.gameId);
        keys.forEach(level -> {
            // 初始化在线人数
            GameOnlineChangeUtils.initOnlineNum(Integer.parseInt(gameId),level);
            // 对应级别房间的配置信息
            List<Integer> betChipsListForLevel = new ArrayList<>();
            String[] betChips = RoomManager.intance().getGameConfigMap().get(level).getBairenFileConfigDTO().getBetChips().split(",");
            for (int i = 0; i < betChips.length; i++) {
                betChipsListForLevel.add(Integer.parseInt(betChips[i]));
            }
            manager.getBetChipsMap().put(level, betChipsListForLevel);
            manager.getTaiHongMap().put(level, RoomManager.intance().getGameConfigMap().get(level).getBairenFileConfigDTO().getAreaBetUpLimit() * 4);
            JSONObject cardTypeMultiple = RoomManager.intance().getGameConfigMap().get(level).getBairenControlConfigDTO().getCardTypeMultiple();
            Map<String, Integer> multipleConfigForLevel = new HashMap<>();
            Arrays.stream(Combination.values()).forEach(combination -> {
                if (combination.getValue() != -1 && !cardTypeMultiple.containsKey(combination.getValue() + "")) {
                    throw new BusinessException(ResultEnum.SYSTEM_INIT_ERROR.getCode() + "", "牌型配置与数据库配置文件未对应,初始化数据失败");
                }
                multipleConfigForLevel.put(combination.getValue() + "", cardTypeMultiple.getIntValue(combination.getValue() + ""));
            });
            manager.getMultipleConfigMap().put(level, multipleConfigForLevel);
        });
        log.info("==>初始化房间配置完成");
    }

    /**
     * @param
     * @return void
     * @description: 初始化房间
     * @author mice
     * @date 2019/7/30
     */
    public void initRoom() {
        RoomActionService startGameService = SpringContextUtil.getBean(RoomActionService.class);
        RoomManager.intance().getGameConfigMap().keySet().forEach(level -> {
            Room room = RoomManager.intance().createRoom(level);
            RoomManager.intance().getPumpMap().put(level, BigDecimal.ZERO);
            Object fileWinGold = this.redisTemplate.opsForValue().get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + level);
            if (fileWinGold != null) {
                RoomManager.intance().addRoomWinGold(level, new BigDecimal((String) fileWinGold));
            }
            Object filePump = this.redisTemplate.opsForValue().get(RedisRegionConfig.FILE_PUMP_KEY + ":" + this.gameId + "_" + level);
            if (filePump != null) {
                RoomManager.intance().addPump(level, new BigDecimal((String) filePump));
            }
            startGameService.action(room.getRoomId());
            try {
                Thread.currentThread();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info("==>初始化房间完成");
    }

    /**
     * @description: 初始化1 2 3 4 5的全排列数据
     * @param a
     * @param n
     * @param k
     * @return void
     * @author mice
     * @date 2019/8/5
     */
    private void initFullyArranged(Map<Integer, List<Integer>> fullyArrangedMap, int a[], int n, int k) {
        if (k == n) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                integers.add(a[i]);
            }
            fullyArrangedMap.put(fullyArrangedMap.size() + 1, integers);
        }
        // 当前位置为k，依次和后面的位置交换
        for (int i = k; i < n; i++) {
            int temp = a[k];
            a[k] = a[i];
            a[i] = temp;
            this.initFullyArranged(fullyArrangedMap, a, n, k + 1);
            // 回溯
            temp = a[k];
            a[k] = a[i];
            a[i] = temp;
        }
    }
}