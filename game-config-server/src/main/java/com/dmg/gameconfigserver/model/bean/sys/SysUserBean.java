package com.dmg.gameconfigserver.model.bean.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO 用户表
 * @Date 13:49 2019/11/6
 */
@Data
@TableName("t_dmg_game_sys_user")
public class SysUserBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * userName
     */
    private String userName;
    /**
     * password
     */
    private String password;
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
}
