package com.dmg.gameconfigserver.service.config.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dmg.gameconfigserver.common.constant.UserControlConstant;
import com.dmg.gameconfigserver.dao.config.UserControlConfigDao;
import com.dmg.gameconfigserver.model.bean.config.UserControlConfigBean;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlConfigDTO;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;
import com.dmg.gameconfigserver.service.config.UserControlConfigService;
import com.dmg.gameconfigserver.service.config.UserControlListService;
import com.dmg.gameconfigserver.service.config.UserControlRecordService;
import com.dmg.gameconfigserver.service.sys.SysActionLogService;
import com.dmg.server.common.enums.UserControlStateEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userControlConfigService")
public class UserControlConfigServiceimpl implements UserControlConfigService {
	@Autowired
    private SysActionLogService sysActionLogService;
	
    @Autowired
    private UserControlConfigDao userControlConfigDao;
    
    @Autowired
    private UserControlRecordService userControlRecordService;
    
    @Autowired
    private UserControlListService userControlListService;
    
    private StringRedisTemplate redisTemplate;

	@Override
	public List<UserControlConfigDTO> getUserControlConfigList() {
		List<UserControlConfigDTO> userControlConfigDTOList = new ArrayList<>();
		List<UserControlConfigBean> UserControlConfigBeanList = userControlConfigDao.selectList(null);
		UserControlConfigBeanList.forEach(v -> {
			UserControlConfigDTO dto = new UserControlConfigDTO();
			BeanUtils.copyProperties(v,dto);
			userControlConfigDTOList.add(dto);
		});
		return userControlConfigDTOList;
	}
	
	@Override
	public void saveOne(UserControlConfigBean userControlConfigBean) {
		userControlConfigDao.insert(userControlConfigBean);
	}

	@Override
	public void updateWithActionLog(UserControlConfigBean userControlConfigBean, String loginIp, Long sysUserId) {
		UserControlConfigBean oldUserControlConfigBean = userControlConfigDao.selectById(userControlConfigBean.getId());
		updateOne(userControlConfigBean);
		// 操作记录
		UserControlConfigDTO source = new UserControlConfigDTO();
		UserControlConfigDTO target = new UserControlConfigDTO();
		BeanUtils.copyProperties(userControlConfigBean, source);
		BeanUtils.copyProperties(oldUserControlConfigBean, target);
		try {
            sysActionLogService.pushActionLog(source, target, loginIp, sysUserId);
        } catch (Exception e) {
            log.error("发送操作记录失败:{}", e);
        }
	}
	
	@Override
	public void updateOne(UserControlConfigBean userControlConfigBean) {
		userControlConfigDao.updateById(userControlConfigBean);
		
	}

	@Override
	public void deleteOne(int id) {
		userControlConfigDao.deleteById(id);
	}

	@Override
	public int getModel(UserControlListDTO userControlListDTO) {
		int model = 0;
		if (userControlListDTO != null) {
			// 控制类型
			Integer controlType = UserControlStateEnum.getCodeByStateId(userControlListDTO.getControlState());
			// 如果处于点控状态,返回0
			if (controlType == UserControlStateEnum.CONTROL_STATE_POINT.getCode()) {
				return model;
			}
			// 玩家自控列表
			List<UserControlConfigDTO> userControlConfigList = getUserControlConfigList();
			// 玩家当前总流水
			BigDecimal totalWater = userControlListDTO.getTotalBet();
			// 玩家当前输赢值
			BigDecimal totalWinLose = userControlListDTO.getTotalWinLose();
			for (UserControlConfigDTO dto : userControlConfigList) {
				// 最小流水值
				BigDecimal waterMinValue = dto.getWaterMinValue();
				// 最大流水值
				BigDecimal waterMaxValue = dto.getWaterMaxValue();
				// 可赢钱最大值
				BigDecimal rewardMaxValue = dto.getRewardMaxValue();
				// 查找流水区间(大于等于最小流水值,小于最大流水值)
				if ((totalWater.compareTo(waterMinValue) == 1 || totalWater.compareTo(waterMinValue) == 0)
						&& totalWater.compareTo(waterMaxValue) == -1) {
					// 自控输
					if (totalWinLose.compareTo(rewardMaxValue) == 1) {
						log.info("用户进入自控模式 =====> userId: {}, totalWater: {}, totalWinLose: {}, rewardMaxValue: {} !",
								userControlListDTO.getUserId(),totalWater,totalWinLose,rewardMaxValue);
						// 控制状态
						userControlListDTO.setControlState(UserControlConstant.CONTROL_LOSE2);
						// 控制人
						userControlListDTO.setOperator(UserControlConstant.ADMIN);
						// 控制时间
						userControlListDTO.setOperatingTime(new Date());
						UserControlListBean userControlListBean = new UserControlListBean();
						BeanUtils.copyProperties(userControlListDTO, userControlListBean);
						// 更新控制列表
						userControlListService.update(userControlListBean);
						// 保存控制记录
						userControlRecordService.saveUserControlRecordInfo(userControlListBean, userControlListDTO.getTotalWinLose());
						return dto.getAutoControlModel();
					} else { 
						if (controlType != 0) { // 自控输结束
							log.info("用户退出自控模式 =====> userId: {}, totalWater: {}, totalWinLose: {}, rewardMaxValue: {} !",
									userControlListDTO.getUserId(),totalWater,totalWinLose,rewardMaxValue);
							// 控制状态
							userControlListDTO.setControlState(UserControlConstant.CONTROL_NORMAL);
							UserControlListBean userControlListBean = new UserControlListBean();
							BeanUtils.copyProperties(userControlListDTO, userControlListBean);
							// 更新控制列表
							userControlListService.update(userControlListBean);
							// 更新控制记录
							userControlRecordService.updateUserControlRecordInfo(userControlListBean, userControlListDTO.getTotalWinLose());
						}
					}
				}
			}
		}
		return model;
	}
}