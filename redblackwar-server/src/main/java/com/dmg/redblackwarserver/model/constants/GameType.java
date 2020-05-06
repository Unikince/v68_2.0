package com.dmg.redblackwarserver.model.constants;

public enum GameType {
    NIUNIU(1,"牛牛"),
    ZHA_JIN_HUA(2,"炸金花"),
    BAIREN_NIUNIU(3,"百人牛牛"),
    BAIREN_ZHA_JIN_HUA(4,"百人炸金花"),
    REAL_PERSON(5,"真人"),
    ESPORTS(6,"电子"),
    CHESS(7,"棋牌"),
    FISHING(8,"捕鱼"),
    BAIJIALE(9,"百家乐"),
    MAJIANG(10,"麻将"),
    REDBLACK(11,"红黑大战"),
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
