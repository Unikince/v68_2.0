package com.dmg.clubserver.model.dto;


import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/3 14:15
 * @Version V1.0
 **/
@Data
public class ClubLogListDTO {
    private Integer operatorId;
    private String operatorNickName;
    private String headImage;
    private Integer position;
    private Integer logType;
    private Date operateDate;
    private Integer beOperatorId;
    private String beOperatorNickName;
    private String remark;
}