package com.dmg.gameconfigserver.service.config.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.common.constant.UserControlConstant;
import com.dmg.gameconfigserver.dao.config.UserControlListDao;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;
import com.dmg.gameconfigserver.model.vo.config.UserControlListVO;
import com.dmg.gameconfigserver.service.config.UserControlListService;
import com.dmg.gameconfigserver.service.config.UserControlRecordService;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userControlListService")
public class UserControlListServiceimpl implements UserControlListService {

    @Autowired
    private UserControlListDao userControlListDao;
    
    @Autowired
    private UserControlRecordService userControlRecordService;
    
    @Autowired
    private SysActionLogService sysActionLogService;
    
    private StringRedisTemplate redisTemplate;
    
	@Override
	public IPage<UserControlListDTO> getUserControlList(UserControlListVO vo) {
		Long userId= vo.getUserId();
		String userNickname= vo.getUserNickname();
		int controlState=vo.getControlState();
		int startNum = controlState;
		int endNum = controlState;
		// 查询全部赢或全部输
		if (controlState == UserControlConstant.CONTROL_WIN || controlState == UserControlConstant.CONTROL_LOSE) { 
			endNum = controlState + 3;
		}
		Page<UserControlListDTO> pageParam = new Page<>(vo.getCurrent(), vo.getSize()); // 当前页码，每页条数
		IPage<UserControlListDTO> userList = userControlListDao.getUserList(pageParam, userId, userNickname, startNum, endNum); 
		return userList;
	}
	
	@Override
	public UserControlListDTO getUserControlInfo(Long userId) {
		UserControlListVO vo = new UserControlListVO();
		vo.setUserId(userId);
		vo.setUserNickname("");
		vo.setControlState(0);
		IPage<UserControlListDTO> PageList = getUserControlList(vo);
		List<UserControlListDTO> userControlList = PageList.getRecords();
		if (userControlList.size() > 0) {
			return userControlList.get(0);
		}
		return null;
	}

	@Override
	public void updateWithActionLog(UserControlListBean userControlListBean, String loginIp, Long sysUserId) {
		UserControlListDTO target = getUserControlInfo(userControlListBean.getUserId());
		update(userControlListBean);
		// 保存控制记录
		userControlRecordService.saveUserControlRecordInfo(userControlListBean, target.getTotalWinLose());
		// 保存后台操作记录
		UserControlListDTO source = new UserControlListDTO();
		BeanUtils.copyProperties(userControlListBean, source);
		try {
            sysActionLogService.pushActionLog(source, target, loginIp, sysUserId);
        } catch (Exception e) {
            log.error("发送操作记录失败:{}", e);
        }
	}

	@Override
	public void update(UserControlListBean userControlListBean) {
		log.info("更新用户控制信息 =====> userId: {}, bean: {} !",userControlListBean.getUserId(),userControlListBean);
		userControlListDao.update(userControlListBean, new UpdateWrapper<UserControlListBean>()
				 .eq("user_id", userControlListBean.getUserId()));
	}

	@Override
	public void updateState(Long userId, int state) {
		userControlListDao.updateState(userId, state);
	}

	@Override
	public List<UserControlListDTO> getUserPointControlInfoList(List<Long> userIds) {
		List<UserControlListBean> sourceList = userControlListDao.selectList(new QueryWrapper<UserControlListBean>().in("user_id", userIds));
		List<UserControlListDTO> tagerList = new ArrayList<>();
		for (UserControlListBean source : sourceList) {
			UserControlListDTO tager = new UserControlListDTO();
			BeanUtils.copyProperties(source, tager);
			tagerList.add(tager);
		}
		return tagerList;
	}
	
}