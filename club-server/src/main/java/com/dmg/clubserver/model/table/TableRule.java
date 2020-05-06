package com.dmg.clubserver.model.table;

import lombok.Data;

import java.util.Map;

/**
 * 自定义规则, 用来保存玩法规则
 * @author: CharlesLee
 * @Date 2018/5/7 0007 10:32
 */
@Data
public class TableRule {

    public TableRule(Map<String, Object> maps) {
        maximumMultiplier = (int) maps.get("maximumMultiplier");
//        changeCardNumber = (int) maps.get("changeCardNumber");
        ziMoJiaDi = (boolean) maps.get("ziMoJiaDi");
        ziMoJiaFan = (boolean) maps.get("ziMoJiaFan");
        dianGanHuaDianPao = (boolean) maps.get("dianGanHuaDianPao");
        dianGanHuaZiMo = (boolean) maps.get("dianGanHuaZiMo");
        yaoJiouJiangDuei = (boolean) maps.get("yaoJiouJiangDuei");
        menQingZhongZhang = (boolean) maps.get("menQingZhongZhang");
//        tianDiHu = (boolean) maps.get("tianDiHu");
        baseScore = maps.get("baseScore")==null ? 1:(int) maps.get("baseScore");
    }

    /**
     * 最高番数
     */
    private int maximumMultiplier = 3;
    /**
     * 底分
     */
    private int baseScore = 1;

//    /**
//     * 换牌张数
//     */
//    private int changeCardNumber = 3;

    /**
     * 是否自摸加底
     */
    private boolean ziMoJiaDi = true;

    /**
     * 是否自摸加番
     */
    private boolean ziMoJiaFan = false;

    /**
     * 点杠花(点炮)
     */
    private boolean dianGanHuaDianPao = true;

    /**
     * 点杠花(自摸)
     */
    private boolean dianGanHuaZiMo = false;

    /**
     * 幺九将对
     */
    private boolean yaoJiouJiangDuei = false;

    /**
     * 门清中张
     */
    private boolean menQingZhongZhang = false;
//
//    /**
//     * 是否可以天地胡
//     */
//    private boolean tianDiHu = false;


}
