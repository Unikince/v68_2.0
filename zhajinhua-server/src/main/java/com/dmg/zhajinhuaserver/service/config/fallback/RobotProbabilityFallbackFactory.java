package com.dmg.zhajinhuaserver.service.config.fallback;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.dto.RobotProbabilityDTO;
import com.dmg.zhajinhuaserver.service.config.RobotProbabilityService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:17 2019/10/15
 */
@Slf4j
@Component
public class RobotProbabilityFallbackFactory implements FallbackFactory<RobotProbabilityService> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public RobotProbabilityService create(Throwable throwable) {
        return new RobotProbabilityService() {

            @Override
            public Result<List<RobotProbabilityDTO>> getList() {
                log.info("熔断查询，getList");
                Result<List<RobotProbabilityDTO>> result = new Result<>();
                result.setCode(BaseResultEnum.SUCCESS.getCode().toString());
                List<Object> robotProbabilityList = redisUtil.lGet(RegionConstant.ROBOT_PROBABILITY, 0, -1);
                List<RobotProbabilityDTO> data = new ArrayList<>();
                robotProbabilityList.forEach(robotProbability -> {
                    data.add((RobotProbabilityDTO) robotProbability);
                });
                result.setData(data);
                return result;
            }
        };
    }
}
