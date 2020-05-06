package com.dmg.fish.business.model.room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;

/**
 * 捕鱼seat
 */
public class Seat {
    /** 所属桌子 */
    public final Table table;
    /** 座位顺序(从0开始),没特殊需求最好不该变它 */
    public final int order;
    /** 玩家id */
    public long playerId;
    /** 玩家昵称 */
    public String nickName = "";
    /** 是否在线 */
    public boolean online;
    /** 0:男,非0:女 */
    public int sex;
    /** 头像 */
    public String icon = "";
    /** 是否是机器人 true:是 */
    public boolean robot;

    /** 炮管分数 */
    public int batteryScore;
    /** 玩家发出的子弹，key:子弹id,val:分数 */
    public Map<Integer, Integer> bullets = new ConcurrentHashMap<>();
    /** 玩家发出的子弹，key:子弹id,val:发出的时间 */
    public Map<Integer, Long> bulletTimes = new ConcurrentHashMap<>();
    /** 玩家是否准备好(请求恢复场景数据成功) */
    public boolean ready;
    /** 上一次发炮时间戳 */
    public long preFireTime = 0;
    /** 合计发炮分数 */
    public long totalFireScore = 0;
    /** 合计打死鱼分数 */
    public long totalFishScore = 0;
    /** 金币 */
    public long gold;
    /** 子弹id种子(用于服务器分配的子弹id,如:鱼死后散出子弹,子弹id由服务器分配) */
    public int bulletSeed;

    /** 玩家控制类型(1玩家点控、2玩家自控、3房间库存抽放水) */
    public int ctrlType;
    /** 玩家控制模型 */
    public FishCtrlReturnRateDTO ctrlModel;

    /** 上次结算缓存金币 */
    public long preGold;
    /** 上次结算缓存合计发炮分数 */
    public long preTotalFireScore = 0;
    /** 上次结算缓存合计打死鱼分数 */
    public long preTotalFishScore = 0;

    public Seat(int order, Table table) {
        this.table = table;
        this.order = order;
    }

    /**
     * 清空座位数据
     */
    public void clear() {
        this.playerId = 0;
        this.nickName = "";
        this.online = false;
        this.sex = 0;
        this.icon = "";
        this.robot = false;

        this.batteryScore = 0;
        this.bullets.clear();
        this.bulletTimes.clear();
        this.preFireTime = 0;
        this.totalFireScore = 0;
        this.totalFishScore = 0;
        this.gold = 0;
        this.bulletSeed = 0;
        this.robot = false;
        this.ready = false;

        this.ctrlType = 0;
        this.ctrlModel = null;

        this.preGold = 0;
        this.preTotalFireScore = 0;
        this.preTotalFishScore = 0;
    }
}
