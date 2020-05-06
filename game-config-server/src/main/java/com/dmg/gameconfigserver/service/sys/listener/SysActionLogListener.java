package com.dmg.gameconfigserver.service.sys.listener;

import com.dmg.gameconfigserver.dao.sys.SysActionLogDao;
import com.dmg.gameconfigserver.model.bean.sys.SysActionLogBean;
import com.dmg.gameconfigserver.model.dto.sys.SysActionLogDTO;
import com.dmg.gameconfigserver.service.sys.event.SysActionLogEvent;
import com.dmg.server.common.util.DmgBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:46 2019/12/25
 */
@Slf4j
@Component
public class SysActionLogListener implements ApplicationListener<SysActionLogEvent> {

    @Autowired
    private SysActionLogDao sysActionLogDao;

    @Async
    @Override
    public void onApplicationEvent(SysActionLogEvent sysActionLogEvent) {
        //系统操作日志事件
        SysActionLogDTO sysActionLogDTO = (SysActionLogDTO) sysActionLogEvent.getSource();
        log.info("接收到系统操作日志事件 data:{}", sysActionLogDTO.toString());
        SysActionLogBean sysActionLogBean = new SysActionLogBean();
        DmgBeanUtils.copyProperties(sysActionLogDTO, sysActionLogBean);
        sysActionLogBean.setModifyDate(new Date());
        sysActionLogDao.insert(sysActionLogBean);
    }
}
