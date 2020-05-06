package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.dto.GameFileConfigDTO;
import com.dmg.zhajinhuaserver.model.dto.LoginDTO;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.cache.GameFileConfigCacheService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 登录游戏大厅
 * @Author mice
 * @Date 2019/18/30 16:00
 * @Version V1.0
 **/
@Service
@Slf4j
public class LoginProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerCacheService;

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
            pushService.push(userId, messageResult);
            return;
        }
        Player player = playerCacheService.getPlayer(userId);
        if (null == player) {
            pushService.push(userId, MessageConfig.LOGIN, ResultEnum.GET_USERINFO_FAIL.getCode());
            return;
        }

        player.setActive(true);
        Map map = RoomManager.instance().getPlayerRoomIdMap();
        if (null != map.get(userId)) {
            player.setRoomId(Integer.valueOf(map.get(userId).toString()));
        } else {
            player.setRoomId(0);
        }
        playerCacheService.update(player);
        LoginDTO loginDTO = new LoginDTO();
        LoginDTO.PlayerDTO playerDTO = new LoginDTO.PlayerDTO();
        BeanUtils.copyProperties(player, playerDTO);
        loginDTO.setPlayerDTO(playerDTO);
        loginDTO.setRoomConfig(this.getRoomConfig());
        MessageResult messageResult = new MessageResult(MessageConfig.LOGIN, loginDTO);
        pushService.push(userId, messageResult);
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
            roomConfigList.add(roomConfig);
        }
        return roomConfigList;
    }
}