package com.dmg.gameconfigserver.model.vo.sys;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:29 2019/12/30
 */
@Data
public class SysRoleVO {
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
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 修改人
     */
    private Long modifyUser;
    /**
     * 顺序
     */
    private Long sort;
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
    /**
     * 资源id
     */
    private List<Long> resourceId;
}
