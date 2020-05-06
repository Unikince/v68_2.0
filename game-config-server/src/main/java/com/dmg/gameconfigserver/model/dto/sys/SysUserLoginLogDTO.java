package com.dmg.gameconfigserver.model.dto.sys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:49 2019/12/26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserLoginLogDTO {
    /**
     * 登陆时间
     */
    private Date loginDate;
    /**
     * 登陆ip
     */
    private String ip;
    /**
     * 登陆账号
     */
    private Long userId;
}
