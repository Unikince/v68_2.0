package com.dmg.bairenniuniuserver.model.dto;

import com.dmg.bairenniuniuserver.common.model.Poker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/2 10:52
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandPokerDTO {
    private Integer cardType;
    private List<Poker> pokerList;

}