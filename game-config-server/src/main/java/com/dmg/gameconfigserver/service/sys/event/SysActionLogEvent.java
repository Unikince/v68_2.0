package com.dmg.gameconfigserver.service.sys.event;

import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @Author liubo
 * @Description //TODO 系统操作log事件
 * @Date 15:40 2019/12/25
 */
public class SysActionLogEvent extends ApplicationEvent {

    public SysActionLogEvent(final SysActionLogDTO source) {
        super(source);
    }
}
