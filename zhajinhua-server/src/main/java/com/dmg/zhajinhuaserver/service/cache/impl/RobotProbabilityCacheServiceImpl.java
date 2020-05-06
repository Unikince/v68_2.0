package com.dmg.zhajinhuaserver.service.cache.impl;

import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.model.dto.RobotProbabilityDTO;
import com.dmg.zhajinhuaserver.service.cache.RobotProbabilityCacheService;
import com.dmg.zhajinhuaserver.service.config.RobotProbabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:58 2019/9/30
 */
@Slf4j
@Service
public class RobotProbabilityCacheServiceImpl implements RobotProbabilityCacheService {

    @Autowired
    private RobotProbabilityService robotProbabilityService;

    @Override
    public RobotProbabilityDTO getRobotProbability() {
        log.info("调用game-config服务查询金花基础配置信息");
        Result<List<RobotProbabilityDTO>> result = robotProbabilityService.getList();
        log.info("调用game-config服务查询金花基础配置信息resp：{}", result.toString());
        if (BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
            return result.getData().get(0);
        }
        return null;
    }

}
