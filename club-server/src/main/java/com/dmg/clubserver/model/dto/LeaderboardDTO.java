package com.dmg.clubserver.model.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/31 17:12
 * @Version V1.0
 **/
@Data
public class LeaderboardDTO {
    private Integer id;
    private Integer roleId;
    private String headImage;
    private String nickName;
    private Integer winScore;
    private Integer totalRound;
}