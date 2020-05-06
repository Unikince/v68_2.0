package com.dmg.niuniuserver.process;

import java.util.*;

import com.dmg.niuniuserver.constant.Constant;
import com.dmg.niuniuserver.model.dto.GameFileConfigDTO;
import com.dmg.niuniuserver.service.cache.GameFileConfigCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.dto.LoginDTO;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.service.PushService;

/**
 * @Description 登录游戏大厅
 * @Author mice
 * @Date 2019/18/30 16:00
 * @Version V1.0
 **/
@Slf4j
@Service
public class LoginProcess implements AbstractMessageHandler {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PushService pushService;

    @Autowired
    private GameFileConfigCacheService fileBaseCacheService;

    @Override
    public String getMessageId() {
        return MessageConfig.LOGIN;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        // 检查是否维护
        if (RoomManager.instance().isShutdownServer()) {
            MessageResult messageResult = new MessageResult(MessageConfig.SHUTDOWN_SERVER_NTC);
            this.pushService.push(userId, messageResult);
            return;
        }
        Player player = this.playerService.getPlayerPlatform(userId);
        player.setOnline(true);
        Map map = RoomManager.instance().getPlayerRoomIdMap();
        if (null != map.get(userId)) {
            player.setRoomId(Integer.valueOf(map.get(userId).toString()));
        } else {
            player.setRoomId(0);
        }
        this.playerService.update(player);
        LoginDTO loginDTO = new LoginDTO();
        LoginDTO.PlayerDTO playerDTO = new LoginDTO.PlayerDTO();
        BeanUtils.copyProperties(player, playerDTO);
        loginDTO.setPlayerDTO(playerDTO);
        loginDTO.setRoomConfig(this.getRoomConfig());
        MessageResult messageResult = new MessageResult(MessageConfig.LOGIN, loginDTO);
        this.pushService.push(userId, messageResult);
    }

    /**
     * @Author liubo
     * @Description //TODO 获取房间配置信息
     * @Date 14:29 2019/10/11
     **/
    private List<Map<String, Object>> getRoomConfig() {
        List<GameFileConfigDTO> gameFileConfigDTOList = fileBaseCacheService.getGameFileConfigByGameId(Constant.GAME_ID);
        List<Map<String, Object>> roomConfigList = new ArrayList<>();
        for (GameFileConfigDTO gameFileConfigDTO : gameFileConfigDTOList) {
            Map<String, Object> roomConfig = new HashMap<>();
            roomConfig.put("level", gameFileConfigDTO.getFileId());
            roomConfig.put("baseScore", gameFileConfigDTO.getBaseScore());
            roomConfig.put("lowerLimitScore", gameFileConfigDTO.getLowerLimit());
            roomConfig.put("maxLimitScore", gameFileConfigDTO.getUpperLimit());
            roomConfig.put("levelName", gameFileConfigDTO.getFileName());
            //roomConfig.put("topBet", gameFileConfigDTO.getTotalBet());
            roomConfigList.add(roomConfig);
        }
        return roomConfigList;
    }
}