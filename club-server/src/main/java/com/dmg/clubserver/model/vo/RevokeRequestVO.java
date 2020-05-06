package com.dmg.clubserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 14:45
 * @Version V1.0
 **/
@Data
public class RevokeRequestVO {
    private Integer roleId;
    private Integer clubId;
}