package com.dmg.bairenlonghu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dmg.bairenlonghu.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dmg.bairenlonghu.common.exception.BusinessException;
import com.dmg.bairenlonghu.common.result.ResultEnum;
import com.dmg.bairenlonghu.common.work.QueueType;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.manager.TimerManager;
import com.dmg.bairenlonghu.manager.WorkManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.constants.Combination;
import com.dmg.bairenlonghu.service.logic.StartGameService;
import com.dmg.bairenlonghu.sysconfig.RegionConfig;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        this.cleanRobotCache();
        TimerManager.instance().init();
        WorkManager.instance().init(QueueType.values());
        int[] a = new int[2];
        for (int i = 0; i < 2; i++)
            a[i] = i + 1;
        this.initFullyArranged(RoomManager.intance().getFullyArrangedMap(), a, 2, 0);
        log.info("全排列初始化完成:{}", JSONObject.toJSONString(RoomManager.intance().getFullyArrangedMap()));
        this.initsystemWinConfig();
        this.initRoomconfig();
        this.initRoom();
    }

    public void cleanRobotCache() {
        Set<String> keys = redisTemplate.keys(RegionConfig.ROBOT_REDIS_KEY + "*");
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
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
        List<BairenGameConfigDTO> bairenGameConfigDTOS = gameConfigService.getBairenGameConfigData(String.valueOf(Constant.GAME_ID));
        Map<Integer, BairenGameConfigDTO> bairenGameConfigDTOMap = new HashMap<>();
        for (BairenGameConfigDTO bairenGameConfigDTO : bairenGameConfigDTOS) {
            if (bairenGameConfigDTO.getBairenFileConfigDTO().getOpenStatus() == 1) {
                bairenGameConfigDTOMap.put(bairenGameConfigDTO.getFileId(), bairenGameConfigDTO);
            }
        }
        RoomManager.intance().setGameConfigMap(bairenGameConfigDTOMap);
        log.info("==>初始化系统配置完成");
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
        keys.forEach(level -> {
            //对应级别房间的配置信息
            List<Integer> betChipsListForLevel = new ArrayList<>();
            String[] betChips = RoomManager.intance().getGameConfigMap().get(level).getBairenFileConfigDTO().getBetChips().split(",");
            for (int i = 0; i < betChips.length; i++) {
                betChipsListForLevel.add(Integer.parseInt(betChips[i]));
            }
            RoomManager.intance().getBetChipsMap().put(level, betChipsListForLevel);
            JSONObject cardTypeMultiple = RoomManager.intance().getGameConfigMap().get(level).getBairenControlConfigDTO().getCardTypeMultiple();
            Map<Integer, Integer> multipleConfigForLevel = new HashMap<>();
            if (cardTypeMultiple != null) {
                Arrays.stream(Combination.values()).forEach(combination -> {
                    if (combination.getValue() != -1 && !cardTypeMultiple.containsKey(combination.getValue() + "")) {
                        throw new BusinessException(ResultEnum.SYSTEM_INIT_ERROR.getCode() + "", "牌型配置与json配置文件未对应,初始化数据失败");
                    }
                    multipleConfigForLevel.put(combination.getValue(), cardTypeMultiple.getIntValue(combination.getValue() + ""));
                });
            }
            RoomManager.intance().getMultipleConfigMap().put(level, multipleConfigForLevel);
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
        StartGameService startGameService = SpringUtil.getBean(StartGameService.class);
        RoomManager.intance().getMultipleConfigMap().keySet().forEach(level -> {
            Room room = RoomManager.intance().createRoom(level);
            RoomManager.intance().getRoomGoldMap().put(level, BigDecimal.ZERO);
            RoomManager.intance().getPumpMap().put(level, BigDecimal.ZERO);

            Object fileWinGold = redisTemplate.opsForValue().get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + level);
            if (fileWinGold != null) {
                RoomManager.intance().addRoomWinGold(level, new BigDecimal((String) fileWinGold));
            }
            Object filePump = redisTemplate.opsForValue().get(RedisRegionConfig.FILE_PUMP_KEY + ":" + Constant.GAME_ID + "_" + level);
            if (filePump != null) {
                RoomManager.intance().addPump(level, new BigDecimal((String) filePump));
            }
            startGameService.startGame(room.getRoomId());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info("==>初始化房间完成");
    }

    /**
     * @param a
     * @param n
     * @param k
     * @return void
     * @description: 初始化1 2 3 4 5的全排列数据
     * @author mice
     * @date 2019/8/5
     */
    public void initFullyArranged(Map<Integer, List<Integer>> fullyArrangedMap, int a[], int n, int k) {
        if (k == n) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                integers.add(a[i]);
            }
            fullyArrangedMap.put(fullyArrangedMap.size() + 1, integers);
        }
        //当前位置为k，依次和后面的位置交换
        for (int i = k; i < n; i++) {
            int temp = a[k];
            a[k] = a[i];
            a[i] = temp;
            initFullyArranged(fullyArrangedMap, a, n, k + 1);
            //回溯
            temp = a[k];
            a[k] = a[i];
            a[i] = temp;
        }
    }
}