package com.dmg.zhajinhuaserver.service.config.fallback;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.dto.RobotSeeDTO;
import com.dmg.zhajinhuaserver.service.config.RobotSeeService;
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
public class RobotSeeFallbackFactory implements FallbackFactory<RobotSeeService> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public RobotSeeService create(Throwable throwable) {
        return new RobotSeeService() {

            @Override
            public Result<RobotSeeDTO> getRobotSeeByRound(Integer round) {
                log.info("熔断查询，getRobotSeeByRound");
                Result<RobotSeeDTO> result = new Result<>();
                result.setCode(BaseResultEnum.SUCCESS.getCode().toString());
                List<Object> robotSeeList = redisUtil.lGet(RegionConstant.ROBOT_SEE, 0, -1);
                robotSeeList.forEach(robotSee -> {
                    RobotSeeDTO robotSeeDTO = (RobotSeeDTO) robotSee;
                    if (robotSeeDTO.getRoundMax().intValue() >= round.intValue() && robotSeeDTO.getRoundMin() < round.intValue()) {
                        result.setData(robotSeeDTO);
                    }
                });
                return result;
            }

            @Override
            public Result<List<RobotSeeDTO>> getList() {
                return null;
            }
        };
    }
}
