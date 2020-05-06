package com.dmg.gameconfigserver.model.bean.config.notice;

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
@TableName("t_dmg_notice")
public class NoticeBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
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
