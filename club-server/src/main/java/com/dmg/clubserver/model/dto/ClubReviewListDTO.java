package com.dmg.clubserver.model.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 18:56
 * @Version V1.0
 **/
@Data
public class ClubReviewListDTO {
    private Integer clubId;
    private Integer roleId;
    private String nickName;
    private Integer level;
    private String headImage;
}