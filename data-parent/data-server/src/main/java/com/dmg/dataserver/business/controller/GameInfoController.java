package com.dmg.dataserver.business.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.dmg.common.core.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dmg.dataserver.business.model.AllGameInfoDTO;
import com.dmg.dataserver.business.model.User;
import com.dmg.dataserver.business.service.UserCacheService;
import com.dmg.dataserver.common.net.socket.NettyServerMap;
import com.dmg.dataserver.common.net.web.Result;
import com.dmg.gameconfigserverapi.dto.GameInfoDTO;
import com.dmg.gameconfigserverapi.feign.GameConfigService;
import com.dmg.gameconfigserverapi.feign.ResourceServerConfigService;

import io.netty.channel.Channel;

import static com.dmg.common.core.config.RedisRegionConfig.USER_WHITE_DEVICE_CODE;

@RestController
public class GameInfoController {
    public Map<Integer, String> gameCodeMap = new HashMap<>();
    @Autowired
    private GameConfigService gameConfigService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private ResourceServerConfigService resourceServerConfigService;
    @Autowired
    private RedisUtil redisUtil;

    /** 初始化 */
    @PostConstruct
    public void init() {
        List<GameInfoDTO> gameInfoDTOList = this.gameConfigService.getGameInfoList();
        for (GameInfoDTO gameInfoDTO : gameInfoDTOList) {
            this.gameCodeMap.put(gameInfoDTO.getGameId(), gameInfoDTO.getGameCode());
        }
    }
    /** 游戏服务器配置 */
    @RequestMapping("/clientConfigs/{deployEnvironment}")
    public Result getClientConfigs(@PathVariable("deployEnvironment") Integer deployEnvironment) {
        JSONObject result = new JSONObject();
        // 客户端资源服务器列表
        List<String> resourceServerList = new LinkedList<>();
        resourceServerList = this.resourceServerConfigService.getResourceServerList(deployEnvironment);
        result.put("resourceServerList", resourceServerList);
        List<String> notOpenGameList = new ArrayList<>();
        List<GameInfoDTO> gameInfoDTOList = this.gameConfigService.getGameInfoList();
        for (GameInfoDTO gameInfoDTO : gameInfoDTOList) {
            if (gameInfoDTO.getOpenStatus() == 0) {
                notOpenGameList.add(gameInfoDTO.getGameCode());
            }
        }
        result.put("notOpenGameList", notOpenGameList);
        return Result.success(result);
    }

    /** 游戏服务器配置 */
    @RequestMapping("/clientConfigs/{deviceCode}/{deployEnvironment}")
    public Result getClientConfigs(@PathVariable("deviceCode") String deviceCode,@PathVariable("deployEnvironment") Integer deployEnvironment) {
        JSONObject result = new JSONObject();
        // 客户端资源服务器列表
        List<String> resourceServerList = new LinkedList<>();
        resourceServerList = this.resourceServerConfigService.getResourceServerList(deployEnvironment);
        result.put("resourceServerList", resourceServerList);
        List<String> notOpenGameList = new ArrayList<>();
        List<GameInfoDTO> gameInfoDTOList = this.gameConfigService.getGameInfoList();
        boolean white = redisUtil.hasKey(USER_WHITE_DEVICE_CODE+":"+deviceCode);
        for (GameInfoDTO gameInfoDTO : gameInfoDTOList) {
            if (gameInfoDTO.getOpenStatus() == 0 &&!white) {
                notOpenGameList.add(gameInfoDTO.getGameCode());
            }
        }
        result.put("notOpenGameList", notOpenGameList);
        return Result.success(result);
    }

    /** 所有游戏信息 */
    @RequestMapping("/allGameInfo")
    public Result getAllGameInfo() {
        AllGameInfoDTO allGameInfoDTO = new AllGameInfoDTO();
        List<String> openGameList = new ArrayList<>();
        List<String> allGameList = new ArrayList<>();
        List<GameInfoDTO> gameInfoDTOList = this.gameConfigService.getGameInfoList();
        for (GameInfoDTO gameInfoDTO : gameInfoDTOList) {
            allGameList.add(gameInfoDTO.getGameCode());
            if (gameInfoDTO.getOpenStatus() == 1) {
                openGameList.add(gameInfoDTO.getGameCode());
            }
        }
        allGameInfoDTO.setAllGameList(allGameList);
        allGameInfoDTO.setOpenGameList(openGameList);
        return Result.success(allGameInfoDTO);
    }

    /** 玩家所在游戏 */
    @RequestMapping("/getPosition")
    public Result ebetLogin(int id) {
        User user = this.userCacheService.getCacheUser(id);

        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        if (user != null) {
            if (StringUtils.isBlank(user.getServer())) {
                result.put("gameId", 0);
                result.put("roomId", 0);
                result.put("gameCode", "");
            } else {
                Channel channel = NettyServerMap.getClientChannel(user.getServer());
                if (channel == null || !channel.isOpen()) {
                    result.put("gameId", 0);
                    result.put("roomId", 0);
                    result.put("gameCode", "");
                } else {
                    result.put("gameId", user.getGameId());
                    result.put("roomId", user.getRoomId());
                    result.put("gameCode", this.gameCodeMap.get(user.getGameId())==null?"" : this.gameCodeMap.get(user.getGameId()));
                }
            }
        } else {
            result.put("gameId", 0);
            result.put("roomId", 0);
            result.put("gameCode", "");
        }
        return Result.success(result);
    }
}