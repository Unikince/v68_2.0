package com.dmg.clubserver.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 10:13
 * @Version V1.0
 **/
@Data
public class RoleInfoBean implements Serializable {
    private static final long serialVersionUID = -3236941705272011580L;
    private Integer roleId;
    private String nickName;
    private Integer level;
    // 是否在线 0:不在线 1:在线
    private Integer online=0;
    private Date onlineDate;
    private String headImage;

}