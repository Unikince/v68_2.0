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
@TableName("t_dmg_game_sys_resource")
public class SysResourceBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源标识
     */
    private String identity;
    /**
     * 请求路径
     */
    private String url;
    /**
     * 父节点ID
     */
    private Long parentId;
    /**
     * 父节点路径
     */
    private String parentIds;
    /**
     * 图标
     */
    private String icon;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 是否已删除
     */
    private Boolean isDeleted;
}
