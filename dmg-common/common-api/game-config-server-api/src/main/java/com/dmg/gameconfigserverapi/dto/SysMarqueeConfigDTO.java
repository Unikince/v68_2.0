package com.dmg.gameconfigserverapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/25 10:42
 * @Version V1.0
 **/
@Data
public class SysMarqueeConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 跑马灯类型 1:普通公告 2:停服公告
     */
    private Integer marqueeType;
    /**
     * 需要通知到游戏服
     */
    private String notifyGameServerIds;
    /**
     * 展示内容
     */
    private List<String> notifyContent;
}