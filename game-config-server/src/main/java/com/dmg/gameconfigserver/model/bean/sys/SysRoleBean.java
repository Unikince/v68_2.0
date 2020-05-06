package com.dmg.gameconfigserver.model.bean.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:51 2019/12/24
 */
@Data
@TableName("t_dmg_game_sys_role")
public class SysRoleBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 角色编码
     */
    private String role;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否启用
     */
    private Boolean status;
}
