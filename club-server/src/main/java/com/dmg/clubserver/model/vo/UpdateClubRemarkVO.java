package com.dmg.clubserver.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 19:06
 * @Version V1.0
 **/
@Data
public class UpdateClubRemarkVO implements Serializable {
    private static final long serialVersionUID = -8732378072220022019L;
    /*
     * 俱乐部id
    */
    private Integer clubId;
    /*
     * 俱乐部公告
     */
    private String remark;
}