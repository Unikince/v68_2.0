package com.dmg.gameconfigserver.model.vo.sys;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:00 2019/11/6
 */
@Data
public class SysActionLogVO {
    private Long id;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * userName
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 操作内容
     */
    private String actionDesc;
    /**
     * ip
     */
    private String loginIp;

}
