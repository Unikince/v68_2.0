package com.dmg.lobbyserver.config;

import java.util.Arrays;

public enum GameServerConfig {
    NIUNIU_SERVER_1(999999991,"牛牛游戏服1"),
    NIUNIU_SERVER_2(999999992,"牛牛游戏服2");

    private long serverId;
    private String value;
    GameServerConfig(long serverId, String value) {
        this.serverId = serverId;
        this.value = value;
    }
    public static String getValueByKey(long key){
        return Arrays.stream(GameServerConfig.values()).filter(g -> g.getServerId()==key).findFirst().get().getValue();
    }
    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
