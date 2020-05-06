package com.dmg.zhajinhuaserver.service.cache.impl;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.model.dto.RobotActionDTO;
import com.dmg.zhajinhuaserver.service.cache.RobotActionCacheService;
import com.dmg.zhajinhuaserver.service.config.RobotActionConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:47 2019/9/30
 */
@Slf4j
@Service
public class RobotActionCacheServiceImpl implements RobotActionCacheService {

    @Autowired
    private RobotActionConfigService robotActionConfigService;

    @Override
    public RobotActionDTO getRobotActionByIsSee(Boolean isSee) {
        log.info("调用game-config服务根据是否看牌查询机器人动作概率配置信息req：{}", isSee);
        Result<RobotActionDTO> result = robotActionConfigService.getRobotActionByIsSee(isSee);
        log.info("调用game-config服务根据是否看牌查询机器人动作概率配置信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }

    @Override
    public RobotActionDTO getRobotActionByCard(Integer cardType, Integer card) {
        log.info("调用game-config服务根据牌值查询机器人动作概率配置信息req：{}", card);
        Result<RobotActionDTO> result = robotActionConfigService.getRobotActionByCard(cardType, card);
        log.info("调用game-config服务根据牌值查询机器人动作概率配置信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }
}
