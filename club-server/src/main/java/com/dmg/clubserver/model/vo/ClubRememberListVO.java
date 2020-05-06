package com.dmg.clubserver.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 19:20
 * @Version V1.0
 **/
@Data
public class ClubRememberListVO implements Serializable {
    private Integer clubId;
    private Integer roleId;
    private Integer searchId;
}