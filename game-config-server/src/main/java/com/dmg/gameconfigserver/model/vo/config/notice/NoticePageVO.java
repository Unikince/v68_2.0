package com.dmg.gameconfigserver.model.vo.config.notice;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:30 2020/1/9
 */
@Data
public class NoticePageVO {
    private Long id;
    /**
     * 修改时间
     */
    private Date modifyDate;
    /**
     * 修改人
     */
    private Long modifyUser;

    private String nickName;
    /**
     * 顺序
     */
    private Long sort;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 位置
     */
    private Integer position;
    /**
     * 位置
     */
    private String positionName;
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
    /**
     * 发送状态
     */
    private Integer sendStatus;
}
