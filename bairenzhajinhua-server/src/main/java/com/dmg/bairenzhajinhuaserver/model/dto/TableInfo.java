package com.dmg.bairenzhajinhuaserver.model.dto;

import com.dmg.bairenzhajinhuaserver.common.model.Poker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    // poker
    private List<Poker> pokers;
    // 牌型
    private String pokerType;
    // 牌型倍数
    private Integer mul;
}