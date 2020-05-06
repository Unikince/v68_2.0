package com.dmg.bairenzhajinhuaserver.model.dto;

import java.util.HashMap;
import java.util.Map;
import com.dmg.bairenzhajinhuaserver.model.Table;

import lombok.Data;
@Data
public class DealPokerDTO {
	/**
     *  5个牌桌位 key:牌位 value:牌桌
     */
    private Map<String, Table> tableMap = new HashMap<>();
    
    private int startIndex; //起始发牌位置
}