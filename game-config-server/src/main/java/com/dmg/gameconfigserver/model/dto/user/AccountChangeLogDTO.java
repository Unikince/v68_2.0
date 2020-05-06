package com.dmg.gameconfigserver.model.dto.user;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:58 2019/12/30
 **/
@Data
public class AccountChangeLogDTO extends PageReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 查询起期
     */
    private Date startDate;
    /**
     * 查询止期
     */
    private Date endDate;
    /**
     * userId
     */
    @NotNull(message = "userId不能为空")
    private Long userId;

}