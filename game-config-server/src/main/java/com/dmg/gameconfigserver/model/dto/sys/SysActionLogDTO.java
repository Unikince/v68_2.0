package com.dmg.gameconfigserver.model.dto.sys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:42 2019/12/24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysActionLogDTO {
    private Long id;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 顺序
     */
    private Long sort;
    /**
     * 操作内容
     */
    private String actionDesc;
    /**
     * ip
     */
    private String loginIp;

}
