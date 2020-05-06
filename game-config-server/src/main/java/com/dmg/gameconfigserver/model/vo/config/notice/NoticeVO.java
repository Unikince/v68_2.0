package com.dmg.gameconfigserver.model.vo.config.notice;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 19:11 2020/1/7
 */
@Data
public class NoticeVO {
    /**
     * 类型
     */
    private Integer type;
    /**
     * 位置
     */
    private Integer position;
    /**
     * 展示内容
     */
    private String notifyContent;
    /**
     * 是否启用
     */
    private Boolean status;
    /**
     * 间隔时间
     */
    private Integer intervalTime;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;
}
