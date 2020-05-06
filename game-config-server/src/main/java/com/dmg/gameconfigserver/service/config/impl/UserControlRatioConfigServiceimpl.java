package com.dmg.gameconfigserver.service.config.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dmg.gameconfigserver.dao.config.UserControlRatioConfigDao;
import com.dmg.gameconfigserver.model.bean.config.UserControlRatioConfigBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlRatioConfigDTO;
import com.dmg.gameconfigserver.service.config.UserControlRatioConfigService;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userControlRatioConfigService")
public class UserControlRatioConfigServiceimpl implements UserControlRatioConfigService {
	@Autowired
    private SysActionLogService sysActionLogService;
	
    @Autowired
    private UserControlRatioConfigDao userControlRatioConfigDao;
    
    private StringRedisTemplate redisTemplate;

	@Override
	public UserControlRatioConfigDTO selectOne() {
		UserControlRatioConfigBean bean = userControlRatioConfigDao.selectOne(new QueryWrapper<UserControlRatioConfigBean>());
		UserControlRatioConfigDTO dto = new UserControlRatioConfigDTO();
		BeanUtils.copyProperties(bean,dto);
		return dto;
	}

	@Override
	public void updateWithActionLog(UserControlRatioConfigBean userControlRatioConfigBean, String loginIp,Long sysUserId) {
		UserControlRatioConfigDTO oldUserControlRatioConfigDTO = selectOne();
		update(userControlRatioConfigBean);
		// 操作记录
		UserControlRatioConfigDTO source = new UserControlRatioConfigDTO();
		BeanUtils.copyProperties(userControlRatioConfigBean, source);
		try {
            sysActionLogService.pushActionLog(source, oldUserControlRatioConfigDTO, loginIp, sysUserId);
        } catch (Exception e) {
            log.error("发送操作记录失败:{}", e);
        }
	}
	
	@Override
	public void update(UserControlRatioConfigBean userControlRatioConfigBean) {
		userControlRatioConfigDao.update(userControlRatioConfigBean, new UpdateWrapper<UserControlRatioConfigBean>());
	}
}