package com.dmg.zhajinhuaserver.model.constants;

public enum GameType {
    NIUNIU(1,"牛牛"),
    ZHA_JIN_HUA(2,"炸金花"),
    REAL_PERSON(3,"真人"),
    ESPORTS(4,"电子"),
    CHESS(5,"棋牌"),
    FISHING(6,"捕鱼"),
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
