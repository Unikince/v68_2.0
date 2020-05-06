package com.dmg.lobbyserver.process.sysmall;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.model.dto.SysMallConfigDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.SysMallConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmg.lobbyserver.config.MessageConfig.SYS_MALL_CONFIG_LIST;

/**
 * @Description 系统商城
 * @Author mice
 * @Date 2019/11/26 10:01
 * @Version V1.0
 **/
@Service
public class SysMallProcess implements AbstractMessageHandler {
    @Autowired
    private SysMallConfigService sysMallConfigService;

    @Value("${mall-conversion-ratio}")
    private String mallConversionRatio;
    @Value("${mall-conversion-limit}")
    private String mallConversionLimit;
    @Value("${withdraw-change-ratio}")
    private Double withdrawChangeRatio;

    @Override
    public String getMessageId() {
        return SYS_MALL_CONFIG_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<SysMallConfigDTO> sysMallConfigDTOS = sysMallConfigService.getSysMallConfig();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("mallConversionRatio",mallConversionRatio);
        resultMap.put("mallConversionLimit",mallConversionLimit);
        resultMap.put("withdrawChangeRatio",withdrawChangeRatio);
        resultMap.put("sysMallConfigDTOS",sysMallConfigDTOS);
        result.setMsg(resultMap);
    }
}