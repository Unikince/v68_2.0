package com.dmg.server.common.enums;

public enum GameType {
    NIUNIU(1,"扎金花"),
    ZHA_JIN_HUA(2,"抢庄牛牛"),
    BAIREN_NIUNIU(3,"百人牛牛"),
    BAIREN_ZHA_JIN_HUA(4,"百人炸金花"),
    DOUDIZHU(5,"斗地主"),
    REDBLACKWAR(6,"红黑大战"),
    BCBM(7,"奔驰宝马"),
    BAIJILE(8,"百家乐"),
    FISHING(9,"捕鱼"),
    LONGHUDOU(10,"龙虎斗"),
    SHUIGUOJI(11,"水果机"),
            ;

    private int key;
    private String value;
    GameType(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValueByKey(int key){
        for (GameType gameType : GameType.values()){
            if (key == gameType.getKey()){
                return gameType.value;
            }
        }
        return null;
    }
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
