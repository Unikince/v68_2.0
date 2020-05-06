package com.dmg.gameconfigserver.service.sys.event;

import com.dmg.gameconfigserver.model.dto.sys.SysUserLoginLogDTO;
import org.springframework.context.ApplicationEvent;


/**
 * @Author liubo
 * @Description //TODO 系统登陆log事件
 * @Date 15:40 2019/12/25
 */
public class SysLoginLogEvent extends ApplicationEvent {

    public SysLoginLogEvent(SysUserLoginLogDTO source) {
        super(source);
    }
}
