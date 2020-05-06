package com.dmg.game.task.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO 公共字段
 * @Date 15:28 2019/9/27
 **/
@Data
public class CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

}
