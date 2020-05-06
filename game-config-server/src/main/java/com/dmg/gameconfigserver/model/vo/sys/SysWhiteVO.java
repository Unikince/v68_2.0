package com.dmg.gameconfigserver.model.vo.sys;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:00 2019/11/6
 */
@Data
public class SysWhiteVO {
    private Long id;
    /**
     * 是否启用
     */
    private Boolean status;
    /**
     * 登陆时间
     */
    private Date loginDate;
    /**
     * ip
     */
    private String ip;
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

}
