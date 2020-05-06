package com.dmg.gameconfigserver.model.vo.sys;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:00 2019/11/6
 */
@Data
public class SysUserVO {
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
     * 创建人
     */
    private String createUserNickName;
    /**
     * userName
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 状态
     */
    private Boolean status;
    /**
     * 登陆时间
     */
    private Date loginDate;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色编码
     */
    private String role;

}
