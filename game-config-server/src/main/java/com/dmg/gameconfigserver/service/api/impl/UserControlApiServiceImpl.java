package com.dmg.gameconfigserver.service.api.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmg.server.common.enums.UserControlStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.gameconfigserver.common.constant.UserControlConstant;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;
import com.dmg.gameconfigserver.service.api.UserControlApiService;
import com.dmg.gameconfigserver.service.config.UserControlConfigService;
import com.dmg.gameconfigserver.service.config.UserControlListService;
import com.dmg.gameconfigserver.service.config.UserControlRecordService;
import com.dmg.server.common.model.dto.UserControlInfoDTO;

@Service("userControlApiService")
public class UserControlApiServiceImpl implements UserControlApiService{
	@Autowired
    private UserControlListService userControlListService;
	@Autowired
    private UserControlConfigService userControlConfigService;
    @Autowired
    private UserControlRecordService userControlRecordService;
	
	@Override
	public UserControlListDTO getUserControlInfo(Long userId) {
		UserControlListDTO userControlListDTO = userControlListService.getUserControlInfo(userId);
		return userControlListDTO;
	}

	@Override
	public void updateCurrentScore(UserControlListBean userControlListBean) {
		if (userControlListBean != null && userControlListBean.getControlState() != UserControlConstant.CONTROL_NORMAL) {
			double absControlScore = Math.abs(userControlListBean.getControlScore().doubleValue()); // 控制分数的绝对值
			double absCurrentScore = Math.abs(userControlListBean.getCurrentScore().doubleValue()); // 当前的分数绝对值
			if (absCurrentScore >= absControlScore) {
				// 控制状态
				userControlListBean.setControlState(UserControlConstant.CONTROL_NORMAL);
				// 控制分数
				userControlListBean.setControlScore(new BigDecimal(0));
				// 当前分数
				userControlListBean.setCurrentScore(new BigDecimal(0));
				// 控制模型
				userControlListBean.setControlModel(0);
				// 控制人
				userControlListBean.setOperator(UserControlConstant.ADMIN);
				// 控制时间
				Date operatingTime = new Date();
		        userControlListBean.setOperatingTime(operatingTime);
				// 控制备注
				userControlListBean.setOperatingNote("");
				// 更新控制记录
				userControlRecordService.updateUserControlRecordInfo(userControlListBean,BigDecimal.ZERO);
			}
			userControlListService.update(userControlListBean);
		}
	}

	@Override
	public int getAutoControlModel(Long userId) {
		// 玩家点控数据
		UserControlListDTO dto = userControlListService.getUserControlInfo(userId);
		return userControlConfigService.getModel(dto);
	}

	@Override
	public Map<Long, UserControlInfoDTO> getModlel(List<Long> userIds) {
		List<UserControlListDTO> userControlInfoList = userControlListService.getUserPointControlInfoList(userIds);
		Map<Long, UserControlInfoDTO> infoMap = new HashMap<>();
		
		for (UserControlListDTO bean : userControlInfoList) {
			// 点控状态
			if (bean.getControlModel() > 0 && bean.getControlScore().compareTo(BigDecimal.ZERO) == 1) {
				UserControlInfoDTO dto = new UserControlInfoDTO();
				dto.setUserId(bean.getUserId());
				dto.setControlState(UserControlStateEnum.CONTROL_STATE_POINT.getCode());
				dto.setModel(bean.getControlModel());
				infoMap.put(dto.getUserId(),dto);
			} else { // 如果没有点控状态就查询自控状态
				if (bean.getControlState() == UserControlConstant.CONTROL_NORMAL) {
					int model = userControlConfigService.getModel(bean);
					if (model > 0) {
						UserControlInfoDTO dto = new UserControlInfoDTO();
						dto.setUserId(bean.getUserId());
						dto.setControlState(UserControlStateEnum.CONTROL_STATE_AUTO.getCode());
						dto.setModel(model);
						infoMap.put(dto.getUserId(),dto);
					}
				}
			}
		}
		return infoMap;
	}
	
}
