package com.dmg.gameconfigserver.service.config.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.config.UserControlRecordDao;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.bean.config.UserControlRecordBean;
import com.dmg.gameconfigserver.model.vo.config.UserControlRecordVO;
import com.dmg.gameconfigserver.service.config.UserControlRecordService;
import com.dmg.server.common.enums.UserControlStateEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userControlRecordService")
public class UserControlRecordServiceimpl implements UserControlRecordService {

    @Autowired
    private UserControlRecordDao userControlRecordDao;

	@Override
	public IPage<UserControlRecordBean> getUserControlRecordList(UserControlRecordVO vo) {
		Page<UserControlRecordBean> pageParam = new Page<>(vo.getCurrent(), vo.getSize()); // 当前页码，每页条数
		QueryWrapper<UserControlRecordBean> queryWrapper = new QueryWrapper<UserControlRecordBean>();
		queryWrapper.eq("user_id", vo.getUserId());
		if (vo.getControlType() > 0) {
			queryWrapper.eq("control_type", vo.getControlType());
		} 
		queryWrapper.between("control_start_time", vo.getControlStartTime(), vo.getControlEndTime());
		IPage<UserControlRecordBean> pageResult = userControlRecordDao.selectPage(pageParam, queryWrapper);
		return pageResult;
	}

	@Override
	public void updateUserControlRecordInfo(UserControlListBean userControlListBean,BigDecimal winLose) {
		UserControlRecordBean userControlRecordBean = new UserControlRecordBean();
		// 控制结束时间
		userControlRecordBean.setControlEndTime(new Date());
		// 控制结束金额
		if (UserControlStateEnum.getCodeByStateId(userControlListBean.getControlState()) == 1) { // 点控此字段为控制结束后的当前分数
			userControlRecordBean.setControlEndScore(userControlListBean.getCurrentScore());
		} else { // 自控此字段为控制结束时玩家的输赢值
			userControlRecordBean.setControlEndScore(winLose);
		}
		userControlRecordDao.update(userControlRecordBean, new UpdateWrapper<UserControlRecordBean>()
				.eq("control_start_time", userControlListBean.getOperatingTime()));
		log.info("更新用户控制记录 =====> userId: {}, bean: {}, winLose: {} , startTime: {} !",
				userControlListBean.getUserId(),userControlListBean,winLose,userControlListBean.getOperatingTime());
	}

	@Override
	public void saveUserControlRecordInfo(UserControlListBean userControlListBean,BigDecimal winLose) {
		UserControlRecordBean userControlRecordBean = new UserControlRecordBean();
		// 玩家id
		userControlRecordBean.setUserId(userControlListBean.getUserId());
		// 控制类型
		Integer controlType = UserControlStateEnum.getCodeByStateId(userControlListBean.getControlState());
		userControlRecordBean.setControlType(controlType);
		// 玩家输赢
		userControlRecordBean.setWinLose(winLose);
		// 控制金额
		userControlRecordBean.setControlScore(userControlListBean.getControlScore());
		// 控制模型
		userControlRecordBean.setControlModel(userControlListBean.getControlModel());
		// 控制开始时间
		userControlRecordBean.setControlStartTime(userControlListBean.getOperatingTime());
		// 操作人员
		userControlRecordBean.setOperator(userControlListBean.getOperator());
		// 操作备注
		userControlRecordBean.setOperatingNote(userControlListBean.getOperatingNote());
		userControlRecordDao.insert(userControlRecordBean);
		log.info("保存用户控制记录 =====> userId: {}, bean: {}, winLose: {} , startTime: {} !",
				userControlListBean.getUserId(),userControlListBean,winLose,userControlListBean.getOperatingTime());
	}
	
}