package com.dmg.niuniuserver.service.config.fallback;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.niuniuserver.model.dto.NiuniuRobotActionDTO;
import com.dmg.niuniuserver.service.config.RobotActionService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:17 2019/10/15
 */
@Slf4j
@Component
public class RobotActionFallbackFactory implements FallbackFactory<RobotActionService> {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public RobotActionService create(Throwable throwable) {
        return new RobotActionService() {

            @Override
            public Result<List<NiuniuRobotActionDTO>> getRobInfoByCard(Integer card) {
                log.info("熔断查询，getRobInfoByCard");
                Result<List<NiuniuRobotActionDTO>> result = new Result<>();
                result.setCode(BaseResultEnum.SUCCESS.getCode().toString());
                List<Object> robotActionList = redisUtil.lGet(RegionConstant.ROBOT_ACTION, 0, -1);
                List<NiuniuRobotActionDTO> robotActionDTOList = new ArrayList<>();
                robotActionList.forEach(robotAction -> {
                    NiuniuRobotActionDTO robotActionDTO = (NiuniuRobotActionDTO) robotAction;
                    if (robotActionDTO.getCardMin().intValue() < card.intValue()
                            && (robotActionDTO.getCardMax() == null || robotActionDTO.getCardMax() != null && robotActionDTO.getCardMax().intValue() >= card.intValue())) {
                        robotActionDTOList.add(robotActionDTO);
                    }
                });
                result.setData(robotActionDTOList.stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getRobType()))),
                                ArrayList::new)));
                return result;
            }

            @Override
            public Result<List<NiuniuRobotActionDTO>> getPressureInfoByRob(Integer card, Integer robType) {
                Result<List<NiuniuRobotActionDTO>> result = new Result<>();
                result.setCode(BaseResultEnum.SUCCESS.getCode().toString());
                List<Object> robotActionList = redisUtil.lGet(RegionConstant.ROBOT_ACTION, 0, -1);
                log.info("熔断查询，getPressureInfoByRob");
                List<NiuniuRobotActionDTO> robotActionDTOList = new ArrayList<>();
                robotActionList.forEach(robotAction -> {
                    NiuniuRobotActionDTO robotActionDTO = (NiuniuRobotActionDTO) robotAction;
                    if (robotActionDTO.getCardMin().intValue() < card.intValue()
                            && robotActionDTO.getRobType().intValue() == robType.intValue()
                            && (robotActionDTO.getCardMax() == null || robotActionDTO.getCardMax() != null && robotActionDTO.getCardMax().intValue() >= card.intValue())) {
                        robotActionDTOList.add(robotActionDTO);
                    }
                });
                result.setData(robotActionDTOList);
                return result;
            }

            @Override
            public Result<List<NiuniuRobotActionDTO>> getList() {
                return null;
            }
        };
    }
}
