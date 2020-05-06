package com.dmg.fish.business.model;

/**
 * 玩家捕鱼数据
 */
public class FishPlayerData {
    /** 鱼币 */
    private long coin;
    /** 库存 */
    private long stock;

    /**
     * 获取：鱼币
     *
     * @return 鱼币
     */
    public long getCoin() {
        return this.coin;
    }

    /**
     * 设置：鱼币
     *
     * @param coin 鱼币
     */
    public void setCoin(long coin) {
        this.coin = coin;
    }

    /**
     * 获取：库存
     *
     * @return 库存
     */
    public long getStock() {
        return this.stock;
    }

    /**
     * 设置：库存
     *
     * @param stock 库存
     */
    public void setStock(long stock) {
        this.stock = stock;
    }

}
