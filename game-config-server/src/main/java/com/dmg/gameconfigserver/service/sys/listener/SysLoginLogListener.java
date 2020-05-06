package com.dmg.gameconfigserver.service.sys.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.sys.SysUserDao;
import com.dmg.gameconfigserver.dao.sys.SysWhiteDao;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.bean.sys.SysWhiteBean;
import com.dmg.gameconfigserver.model.dto.sys.SysUserLoginLogDTO;
import com.dmg.gameconfigserver.service.sys.event.SysLoginLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:46 2019/12/25
 */
@Slf4j
@Component
public class SysLoginLogListener implements ApplicationListener<SysLoginLogEvent> {

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysWhiteDao sysWhiteDao;

    @Async
    @Override
    public void onApplicationEvent(SysLoginLogEvent sysLoginLogEvent) {
        //系统登陆日志事件
        SysUserLoginLogDTO data = (SysUserLoginLogDTO) sysLoginLogEvent.getSource();
        log.info("接收到系统登陆日志事件 data:{}", data.toString());
        SysUserBean sysUserBean = new SysUserBean();
        sysUserBean.setId(data.getUserId());
        sysUserBean.setLoginDate(data.getLoginDate());
        sysUserDao.updateById(sysUserBean);

        SysWhiteBean sysWhiteBean = new SysWhiteBean();
        sysWhiteBean.setLoginDate(data.getLoginDate());
        sysWhiteDao.update(sysWhiteBean, new LambdaQueryWrapper<SysWhiteBean>().eq(SysWhiteBean::getIp, data.getIp()));
    }
}
