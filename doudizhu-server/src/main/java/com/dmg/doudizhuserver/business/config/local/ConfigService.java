package com.dmg.doudizhuserver.business.config.local;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dmg.doudizhuserver.business.config.server.GameFileConfigDTO;
import com.dmg.doudizhuserver.business.config.server.PlatfromConfigCacheService;

/** 房间配置 */
@Service
public class ConfigService {
    @Value("${game-id}")
    private int gameId;
    @Autowired
    private PlatfromConfigCacheService cacheService;

    /** 获取所有房间配置 */
    public List<RoomConfig> getRoomConfigs() {
        List<GameFileConfigDTO> dtoList = this.cacheService.getGameFileConfigByGameId(this.gameId);
        if (dtoList == null || dtoList.isEmpty()) {
            return null;
        }
        List<RoomConfig> result = new ArrayList<>();
        for (GameFileConfigDTO dto : dtoList) {
            RoomConfig config = new RoomConfig();
            config.setLevel(dto.getFileId());
            config.setName(dto.getFileName());
            config.setOpen(dto.getOpenStatus() == 1);
            config.setPlayerUpLimit(dto.getPlayerUpLimit());
            config.setPumpRate(dto.getPumpRate());
            config.setBaseScore(dto.getBaseScore());
            config.setEnterScore(dto.getLowerLimit());
            config.setMaxMultiple(Integer.parseInt(dto.getDiscards()));
            result.add(config);
        }
        return result;
    }

    /** 获取指定房间配置 */
    public RoomConfig getRoomConfig(int roomId) {
        List<GameFileConfigDTO> dtoList = this.cacheService.getGameFileConfigByGameId(this.gameId);
        if (dtoList == null || dtoList.isEmpty()) {
            return null;
        }
        for (GameFileConfigDTO dto : dtoList) {
            if (dto.getFileId() != roomId) {
                continue;
            }
            RoomConfig config = new RoomConfig();
            config.setLevel(dto.getFileId());
            config.setName(dto.getFileName());
            config.setOpen(dto.getOpenStatus() == 1);
            config.setPlayerUpLimit(dto.getPlayerUpLimit());
            config.setPumpRate(dto.getPumpRate());
            config.setBaseScore(dto.getBaseScore());
            config.setEnterScore(dto.getLowerLimit());
            config.setMaxMultiple(Integer.parseInt(dto.getDiscards()));
            return config;
        }
        return null;
    }

    /** 获取当前水位线控制 */
    public WaterlineConfig getWaterlineConfig(int roomId, BigDecimal water) {
//        GameWaterPoolDTO dto = this.cacheService.getGameWaterPool(water, this.gameId, roomId);
        return null;
    }

}
