package com.dmg.clubserver.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 14:09
 * @Version V1.0
 **/
@Data
public class RequestJoinClubVO implements Serializable {
    private static final long serialVersionUID = -1346198412518436008L;
    private Integer roleId;
    private Integer clubId;
}