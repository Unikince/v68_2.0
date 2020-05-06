package com.dmg.zhajinhuaserver.service.config.fallback;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.dto.RobotActionDTO;
import com.dmg.zhajinhuaserver.service.config.RobotActionConfigService;
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
public class RobotActionFallbackFactory implements FallbackFactory<RobotActionConfigService> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public RobotActionConfigService create(Throwable throwable) {
        return new RobotActionConfigService() {

            @Override
            public Result<RobotActionDTO> getRobotActionByIsSee(Boolean isSee) {
                log.info("熔断查询，getRobotActionByIsSee");
                Result<RobotActionDTO> result = new Result<>();
                result.setCode(BaseResultEnum.SUCCESS.getCode().toString());
                List<Object> robotActionList = redisUtil.lGet(RegionConstant.ROBOT_ACTION, 0, -1);
                robotActionList.forEach(robotAction -> {
                    RobotActionDTO robotActionDTO = (RobotActionDTO) robotAction;
                    if (robotActionDTO.getIsSee() == isSee) {
                        result.setData(robotActionDTO);
                    }
                });
                return result;
            }

            @Override
            public Result<RobotActionDTO> getRobotActionByCard(Integer cardType, Integer card) {
                log.info("熔断查询，getRobotActionByCard");
                Result<RobotActionDTO> result = new Result<>();
                result.setCode(BaseResultEnum.SUCCESS.getCode().toString());
                List<Object> robotActionList = redisUtil.lGet(RegionConstant.ROBOT_ACTION, 0, -1);
                robotActionList.forEach(robotAction -> {
                    RobotActionDTO robotActionDTO = (RobotActionDTO) robotAction;
                    if (robotActionDTO.getCardType().intValue() == cardType.intValue()
                            && (robotActionDTO.getCardMax() == null || robotActionDTO.getCardMax() != null && robotActionDTO.getCardMax().intValue() < card.intValue())
                            && (robotActionDTO.getCardMin() == null || robotActionDTO.getCardMin() != null && robotActionDTO.getCardMin().intValue() >= card.intValue())) {
                        result.setData(robotActionDTO);
                    }
                });
                return result;
            }

            @Override
            public Result<List<RobotActionDTO>> getList() {
                return null;
            }
        };
    }
}
