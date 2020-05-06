package com.dmg.gameconfigserver.model.bean.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:23 2020/3/16
 **/
@Data
@TableName("t_dmg_user_white")
public class UserWhiteBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private Long userId;
}
