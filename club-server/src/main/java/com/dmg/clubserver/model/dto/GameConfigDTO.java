package com.dmg.clubserver.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/4 9:38
 * @Version V1.0
 **/
@Data
public class GameConfigDTO{
    private Map<Integer,Integer> roomCost;
}