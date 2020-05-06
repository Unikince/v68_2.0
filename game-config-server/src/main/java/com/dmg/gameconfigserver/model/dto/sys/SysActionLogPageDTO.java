package com.dmg.gameconfigserver.model.dto.sys;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 18:02 2019/11/6
 */
@Data
public class SysActionLogPageDTO extends PageReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * username
     */
    private String userName;

    //查询起期
    private Date startDate;
    //查询止期
    private Date endDate;
}
