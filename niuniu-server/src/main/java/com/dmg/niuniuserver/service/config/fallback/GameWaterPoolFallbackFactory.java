package com.dmg.niuniuserver.service.config.fallback;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.constant.Constant;
import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.niuniuserver.model.dto.GameWaterPoolByWaterDTO;
import com.dmg.niuniuserver.service.config.GameWaterPoolService;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:17 2019/10/15
 */
@Slf4j
@Component
public class GameWaterPoolFallbackFactory implements FallbackFactory<GameWaterPoolService> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public GameWaterPoolService create(Throwable throwable) {
        return new GameWaterPoolService() {

            @Override
            public Result<GameWaterPoolDTO> getInfoByWater(GameWaterPoolByWaterDTO gameWaterPoolByWaterDTO) {
                log.info("水池熔断查询：{}", gameWaterPoolByWaterDTO.toString());
                Result<GameWaterPoolDTO> result = new Result<>();
                result.setCode(BaseResultEnum.SUCCESS.getCode().toString());
                List<Object> waterPoolDTOList = redisUtil.lGet(RegionConstant.GAME_WATER_POOL + ":" + Constant.GAME_ID, 0, -1);
                waterPoolDTOList.forEach(gameWaterPoolDTO -> {
                    GameWaterPoolDTO gameWaterPool = (GameWaterPoolDTO) gameWaterPoolDTO;
                    if (gameWaterPool.getRoomLevel().intValue() == gameWaterPoolByWaterDTO.getRoomLevel().intValue()) {
                        if ((gameWaterPool.getWaterLow() == null || gameWaterPool.getWaterLow() != null && gameWaterPool.getWaterLow().compareTo(gameWaterPoolByWaterDTO.getWater()) <= 0)
                                && (gameWaterPool.getWaterHigh() == null || gameWaterPool.getWaterHigh() != null && gameWaterPool.getWaterHigh().compareTo(gameWaterPoolByWaterDTO.getWater()) > 0)) {
                            result.setData(gameWaterPool);
                        }
                    }
                });
                return result;
            }

            @Override
            public Result<List<GameWaterPoolDTO>> getInfoByWater(Integer gameId) {
                return null;
            }
        };
    }
}
