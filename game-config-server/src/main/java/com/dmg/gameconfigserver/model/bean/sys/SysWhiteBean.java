package com.dmg.gameconfigserver.model.bean.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:38 2019/12/24
 */
@Data
@TableName("t_dmg_game_sys_white")
public class SysWhiteBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
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
}
