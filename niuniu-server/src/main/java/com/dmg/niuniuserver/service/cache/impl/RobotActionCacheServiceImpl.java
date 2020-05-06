package com.dmg.niuniuserver.service.cache.impl;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import com.dmg.niuniuserver.service.cache.RobotActionCacheService;
import com.dmg.niuniuserver.service.config.RobotActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:33 2019/9/29
 */
@Slf4j
@Service
public class RobotActionCacheServiceImpl implements RobotActionCacheService {

    @Autowired
    private RobotActionService robotActionService;

    @Override
    public List<NiuniuRobotActionDTO> getRobInfoByCard(Integer card) {
        log.info("调用game-config服务req：{}", card);
        Result<List<NiuniuRobotActionDTO>> result = robotActionService.getRobInfoByCard(card);
        log.info("调用game-config服务resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }

    @Override
    public List<NiuniuRobotActionDTO> getPressureInfoByRob(Integer card, Integer robType) {
        log.info("调用game-config服务req：{},{}", card, robType);
        Result<List<NiuniuRobotActionDTO>> result = robotActionService.getPressureInfoByRob(card, robType);
        log.info("调用game-config服务resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }
}
