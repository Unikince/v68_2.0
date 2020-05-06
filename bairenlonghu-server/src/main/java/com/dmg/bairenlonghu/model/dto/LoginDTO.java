package com.dmg.bairenlonghu.model.dto;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenlonghu.common.model.BasePlayer;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/1 16:03
 * @Version V1.0
 **/
@Data
public class LoginDTO {

    private BasePlayer player;
    /** 进房间额度限制
     * key:房间等级  value:限制额
     */
    private Map<String, Integer> goldLimitMap = new HashMap<>();
    /** 房间上庄额度限制
     * key:房间等级  value:限制额
     */
    private Map<String, Integer> bankerLimitMap = new HashMap<>();

    /** 牌型倍数配置
     *
     */
    private JSONObject multipleConfig;

    private Map<String,String> betChipMap;
}