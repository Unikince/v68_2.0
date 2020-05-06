package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/24 18:11
 * @Version V1.0
 **/
@Data
public class LeaderboardDTO {
    private Integer rank;
    private String userName;
    private BigDecimal accountBalance;
}