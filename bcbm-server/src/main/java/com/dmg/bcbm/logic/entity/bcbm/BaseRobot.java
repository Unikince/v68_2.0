package com.dmg.bcbm.logic.entity.bcbm;

import lombok.Data;

import java.io.Serializable;

import com.dmg.bcbm.logic.entity.SimplePlayer;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/29 11:44
 * @Version V1.0
 **/
@Data
public class BaseRobot extends SimplePlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 局数限制 达到该局数之后自动退出房间
    */
    private int playRoundLimit;
}