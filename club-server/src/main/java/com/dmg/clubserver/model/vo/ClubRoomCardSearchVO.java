package com.dmg.clubserver.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/3 17:14
 * @Version V1.0
 **/
@Data
public class ClubRoomCardSearchVO {
    private Date startDate;
    private Date endDate;
    private Integer clubId;
}