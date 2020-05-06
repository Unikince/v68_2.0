package com.dmg.lobbyserver.model.dto.pay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:13 2019/12/6
 */
@Data
public class VnnPayReqDTO {

    /**
     * 游戏编号
     */
    @JSONField(name="appid")
    private String appId;
    /**
     * 充值金额
     */
    private double num;
    /**
     * 礼品 id
     */
    @JSONField(name="gift_id")
    private String giftId;
    /**
     * 礼品名称
     */
    @JSONField(name="gift_name")
    private String giftName;
    /**
     * 请求类型
     */
    private String action;
    /**
     * 请求流水id
     */
    private String id;
    /**
     * 时间
     */
    private long time;
    /**
     * 用户充值主账号
     */
    @JSONField(name="target_id")
    private String targetId;
    /**
     * 用户充值子账号
     */
    @JSONField(name="target_sub_id")
    private String targetSubId;
}
