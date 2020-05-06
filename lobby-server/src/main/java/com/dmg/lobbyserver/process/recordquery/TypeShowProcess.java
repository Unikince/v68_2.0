package com.dmg.lobbyserver.process.recordquery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.lobbyserver.model.constants.GamePromotionType;
import com.dmg.lobbyserver.model.constants.GameRechargeType;
import com.dmg.lobbyserver.model.constants.GameWithdrawalType;
import com.dmg.lobbyserver.model.vo.GameInfoVO;
import com.dmg.lobbyserver.model.vo.TypeToshowVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.config.GameInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmg.lobbyserver.config.MessageConfig.TYPE_SHOW;

/**
 * @Description 记录类型type
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
@Service
@Slf4j
public class TypeShowProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return TYPE_SHOW;
    }

    @Autowired
    private GameInfoService gameInfoService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Map<String, List<TypeToshowVo>> map = new HashMap<>();
        TypeToshowVo vo = null;
        List<TypeToshowVo> list = new ArrayList<>();
        List<TypeToshowVo> list1 = new ArrayList<>();
        List<TypeToshowVo> list2 = new ArrayList<>();
        List<TypeToshowVo> list3 = new ArrayList<>();
        List<GameInfoVO> gameInfoVOList = this.getGameInfo();
        if (gameInfoVOList != null) {
            gameInfoVOList.forEach(gameInfoVO -> {
                TypeToshowVo typeToshowVo = new TypeToshowVo();
                typeToshowVo.setCode(gameInfoVO.getGameId());
                typeToshowVo.setDesc(gameInfoVO.getGameName());
                list.add(typeToshowVo);
            });
        }
        map.put("gameType", list);
        for (GameRechargeType type1 : GameRechargeType.values()) {
            vo = new TypeToshowVo();
            vo.setCode(type1.getKey());
            vo.setDesc(type1.getValue());
            list1.add(vo);
        }
        map.put("gameRecharge", list1);
        for (GamePromotionType type2 : GamePromotionType.values()) {
            vo = new TypeToshowVo();
            vo.setCode(type2.getKey());
            vo.setDesc(type2.getValue());
            list2.add(vo);
        }
        map.put("gamePromotion", list2);
        for (GameWithdrawalType type3 : GameWithdrawalType.values()) {
            vo = new TypeToshowVo();
            vo.setCode(type3.getKey());
            vo.setDesc(type3.getValue());
            list3.add(vo);
        }
        map.put("gameWithdrawal", list3);
        result.setMsg(JSON.toJSON(map));
    }

    /**
     * @Author liubo
     * @Description //TODO 查询游戏信息
     * @Date 12:51 2019/11/29
     **/
    private List<GameInfoVO> getGameInfo() {
        log.info("调用game-config-server服务【查询游戏信息】");
        Result<List<GameInfoVO>> result;
        try {
            result = gameInfoService.getGameOpen();
            log.info("调用game-config-server服务【查询游戏信息】resp：{}", result.toString());
            if (!BaseResultEnum.SUCCESS.getCode().toString().equals(result.getCode())) {
                return null;
            }
            return result.getData();
        } catch (Exception e) {
            log.error("调用game-config-server服务【查询游戏信息】出现异常:{}", e);
            return null;
        }
    }
}
