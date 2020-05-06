package com.dmg.gameconfigserver.model.dto.config.notice;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 19:09 2020/1/7
 */
@Data
public class NoticeDTO {
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;
    /**
     * 顺序
     */
    @NotNull(message = "顺序不能为空", groups = SaveValid.class)
    private Long sort;
    /**
     * 位置
     */
    @NotNull(message = "位置不能为空", groups = SaveValid.class)
    private Integer type;
    /**
     * 位置
     */
    @NotNull(message = "位置不能为空", groups = SaveValid.class)
    private Integer position;
    /**
     * 展示内容
     */
    @NotNull(message = "公告内容不能为空", groups = SaveValid.class)
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
    @NotNull(message = "开始时间不能为空", groups = SaveValid.class)
    private Date startDate;
    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空", groups = SaveValid.class)
    private Date endDate;
}
