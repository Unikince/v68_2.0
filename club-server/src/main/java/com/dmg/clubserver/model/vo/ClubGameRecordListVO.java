package com.dmg.clubserver.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/31 15:44
 * @Version V1.0
 **/
@Data
public class ClubGameRecordListVO implements Serializable {
    private Integer roleId;
    private Integer clubId;
}