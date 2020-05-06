package com.dmg.niuniuserver.model.bean;

import java.util.Map;

/**
 * 私人场规则集合
 */
public class CustomRule {

    /**
     * 游戏玩法,目前的玩法定义为五种默认
     * 1,明牌抢庄
     * 2,牛牛上庄
     * 3,自由抢庄
     * 4,通比牛牛
     * 5,固定庄家
     */
    private int gamePlay;

    /**
     * 支付有三种选择
     * 1,房主支付
     * 2,大赢家支付
     * 3,AA支付
     */
    private int payOptions;

    /**
     * 支付数量不管是AA还是所有
     */
    private int payNumber;

    /**
     * 规则选择,是否自动开始
     */
    private boolean autoStart;

    /**
     * 闲家推注
     */
    private int xianJiaTueiZhu;

    /**
     * 翻倍规则
     * 1,普通规则
     * 2,高级规则
     * 3,疯狂规则
     */
    private int doublingRule;

    /**
     * 特殊牌型,顺子牛勾选
     */
    private boolean shuenZiNiou;

    /**
     * 特殊牌型,同花牛勾选
     */
    private boolean tongHuaNiou;

    /**
     * 特殊牌型,五花牛勾选
     */
    private boolean wuHuaNiou;

    /**
     * 特殊牌型,葫芦牛勾选
     */
    private boolean huLuNiou;

    /**
     * 特殊牌型,炸弹牛勾选
     */
    private boolean zhaDanNiou;

    /**
     * 特殊牌型,五小牛勾选
     */
    private boolean wuXiaoNiou;

    /**
     * 是否王癞子玩法
     */
    private boolean wangPai;

    /**
     * 是否可中途加入
     */
    private boolean midwayJion;

    /**
     * 是否禁止搓牌
     */
    private boolean cuoPai;

    /**
     * 是否闲家买码
     */
    private boolean xianJiaMaiMa;

    /**
     * 是否下注限制
     */
    private boolean xiaZhuLimit;

    /**
     * 是否暗抢庄家
     */
    private boolean anQiangZhuangJia;

    /**
     * 是否下注翻倍
     */
    private boolean xiaZhuDoubling;

    /**
     * 缓存规则集合
     */
    private Map<String, Object> ruleMaps;

    /**
     * 带入分
     */
    private int carryFraction;

    /**
     * 底分
     */
    private int diFen;

    /**
     * 私人场房间规则创建, 状态来源于客户端选择
     */
    CustomRule(Map<String, Object> maps) {
        ruleMaps = maps;
        gamePlay = (int) maps.get("gamePlay");
        payOptions = (int) maps.get("payOptions");
        payNumber = (int) maps.get("cost");
        autoStart = (boolean) maps.get("autoStart");
        xianJiaTueiZhu = (int) maps.get("xianJiaTueiZhu");
        doublingRule = (int) maps.get("doublingRule");
        carryFraction = (int) maps.get("carryFraction");
        shuenZiNiou = (boolean) maps.get("shuenZiNiou");
        tongHuaNiou = (boolean) maps.get("tongHuaNiou");
        wuHuaNiou = (boolean) maps.get("wuHuaNiou");
        huLuNiou = (boolean) maps.get("huLuNiou");
        zhaDanNiou = (boolean) maps.get("zhaDanNiou");
        wuXiaoNiou = (boolean) maps.get("wuXiaoNiou");
        wangPai = (boolean) maps.get("wangPai");
        midwayJion = (boolean) maps.get("midwayJion");
        cuoPai = (boolean) maps.get("cuoPai");
        xianJiaMaiMa = (boolean) maps.get("xianJiaMaiMa");
        xiaZhuLimit = (boolean) maps.get("xiaZhuLimit");
        anQiangZhuangJia = (boolean) maps.get("anQiangZhuangJia");
        xiaZhuDoubling = (boolean) maps.get("xiaZhuDoubling");
        diFen = (int) maps.get("diFen");
    }

    public int getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(int gamePlay) {
        this.gamePlay = gamePlay;
    }

    public int getPayOptions() {
        return payOptions;
    }

    public void setPayOptions(int payOptions) {
        this.payOptions = payOptions;
    }

    public int getPayNumber() {
        return payNumber;
    }

    public void setPayNumber(int payNumber) {
        this.payNumber = payNumber;
    }

    public int getXianJiaTueiZhu() {
        return xianJiaTueiZhu;
    }

    public void setXianJiaTueiZhu(int xianJiaTueiZhu) {
        this.xianJiaTueiZhu = xianJiaTueiZhu;
    }

    public int getDoublingRule() {
        return doublingRule;
    }

    public void setDoublingRule(int doublingRule) {
        this.doublingRule = doublingRule;
    }

    public boolean isShuenZiNiou() {
        return shuenZiNiou;
    }

    public void setShuenZiNiou(boolean shuenZiNiou) {
        this.shuenZiNiou = shuenZiNiou;
    }

    public boolean isTongHuaNiou() {
        return tongHuaNiou;
    }

    public void setTongHuaNiou(boolean tongHuaNiou) {
        this.tongHuaNiou = tongHuaNiou;
    }

    public boolean isWuHuaNiou() {
        return wuHuaNiou;
    }

    public void setWuHuaNiou(boolean wuHuaNiou) {
        this.wuHuaNiou = wuHuaNiou;
    }

    public boolean isHuLuNiou() {
        return huLuNiou;
    }

    public void setHuLuNiou(boolean huLuNiou) {
        this.huLuNiou = huLuNiou;
    }

    public boolean isZhaDanNiou() {
        return zhaDanNiou;
    }

    public void setZhaDanNiou(boolean zhaDanNiou) {
        this.zhaDanNiou = zhaDanNiou;
    }

    public boolean isWuXiaoNiou() {
        return wuXiaoNiou;
    }

    public void setWuXiaoNiou(boolean wuXiaoNiou) {
        this.wuXiaoNiou = wuXiaoNiou;
    }

    public boolean isWangPai() {
        return wangPai;
    }

    public void setWangPai(boolean wangPai) {
        this.wangPai = wangPai;
    }

    public boolean isMidwayJion() {
        return midwayJion;
    }

    public void setMidwayJion(boolean midwayJion) {
        this.midwayJion = midwayJion;
    }

    public boolean isCuoPai() {
        return cuoPai;
    }

    public void setCuoPai(boolean cuoPai) {
        this.cuoPai = cuoPai;
    }

    public boolean isXianJiaMaiMa() {
        return xianJiaMaiMa;
    }

    public void setXianJiaMaiMa(boolean xianJiaMaiMa) {
        this.xianJiaMaiMa = xianJiaMaiMa;
    }

    public boolean isXiaZhuLimit() {
        return xiaZhuLimit;
    }

    public void setXiaZhuLimit(boolean xiaZhuLimit) {
        this.xiaZhuLimit = xiaZhuLimit;
    }

    public boolean isAnQiangZhuangJia() {
        return anQiangZhuangJia;
    }

    public void setAnQiangZhuangJia(boolean anQiangZhuangJia) {
        this.anQiangZhuangJia = anQiangZhuangJia;
    }

    public boolean isXiaZhuDoubling() {
        return xiaZhuDoubling;
    }

    public void setXiaZhuDoubling(boolean xiaZhuDoubling) {
        this.xiaZhuDoubling = xiaZhuDoubling;
    }

    public Map<String, Object> getRuleMaps() {
        return ruleMaps;
    }

    public void setRuleMaps(Map<String, Object> ruleMaps) {
        this.ruleMaps = ruleMaps;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public int getCarryFraction() {
        return carryFraction;
    }

    public void setCarryFraction(int carryFraction) {
        this.carryFraction = carryFraction;
    }

    public int getDiFen() {
        if (diFen <= 0) {
            diFen = 1;
        }
        return diFen;
    }

    public void setDiFen(int diFen) {
        this.diFen = diFen;
    }
}
