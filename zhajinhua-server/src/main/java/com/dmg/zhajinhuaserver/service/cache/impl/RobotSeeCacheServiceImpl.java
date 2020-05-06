package com.dmg.zhajinhuaserver.service.cache.impl;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.model.dto.RobotSeeDTO;
import com.dmg.zhajinhuaserver.service.cache.RobotSeeCacheService;
import com.dmg.zhajinhuaserver.service.config.RobotSeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:17 2019/9/30
 */
@Slf4j
@Service
public class RobotSeeCacheServiceImpl implements RobotSeeCacheService {

    @Autowired
    private RobotSeeService robotSeeService;

    @Override
    public RobotSeeDTO getRobotSeeByRound(Integer round) {
        log.info("调用game-config服务查询看牌概率配置信息req：{}", round);
        Result<RobotSeeDTO> result = robotSeeService.getRobotSeeByRound(round);
        log.info("调用game-config服务查询看牌概率配置信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData();
        }
        return null;
    }
}
