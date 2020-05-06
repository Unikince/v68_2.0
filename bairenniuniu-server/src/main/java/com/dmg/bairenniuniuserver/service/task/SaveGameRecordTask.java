package com.dmg.bairenniuniuserver.service.task;

import com.alibaba.fastjson.JSON;
import com.dmg.common.core.util.SpringUtil;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.gameconfigserverapi.feign.GameConfigService;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.UserControlInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullNumberAsZero;

/**
 * @Description
 * @Author mice
 * @Date 2020/2/26 10:41
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
@Slf4j
public class SaveGameRecordTask implements Runnable {
    private List<GameRecordDTO> gameRecordDTOS;
    private List<Long> userIds;

    @Override
    public void run() {
        if (CollectionUtils.isEmpty(userIds)){
            log.info("==>当局全是机器人 不记录战绩");
            return;
        }
        GameConfigService gameConfigService = SpringUtil.getBean(GameConfigService.class);
        MQProducer defautProducer = SpringUtil.getBean(MQProducer.class);
        Map<Long, UserControlInfoDTO>  userControlInfoDTOMap = gameConfigService.getModelByUserIds(userIds);
        gameRecordDTOS.forEach(gameRecordDTO -> {
            gameRecordDTO.setControlState(false);
            if (!gameRecordDTO.getIsRobot()){
                if (userControlInfoDTOMap.get(gameRecordDTO.getUserId())!=null){
                    gameRecordDTO.setControlState(true);
                }
            }
            // 发送结算数据
            try {
                defautProducer.sendAsync(JSON.toJSONString(gameRecordDTO,WriteNullNumberAsZero));
            } catch (Exception e) {
                log.error("战绩信息发送异常:{}", e);
            }
        });

    }
}