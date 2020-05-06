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
@TableName("t_dmg_game_sys_action_log")
public class SysActionLogBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 操作内容
     */
    private String actionDesc;
    /**
     * ip
     */
    private String loginIp;
}
