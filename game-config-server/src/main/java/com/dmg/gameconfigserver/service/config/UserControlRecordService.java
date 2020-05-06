package com.dmg.gameconfigserver.service.config;


import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.bean.config.UserControlRecordBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlRecordDTO;
import com.dmg.gameconfigserver.model.vo.config.UserControlRecordVO;

/**
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
public interface UserControlRecordService {

	/**
	 * @param userId 用户id
	 * @param controlType 控制类型
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @throws Exception 
	 */
	IPage<UserControlRecordBean> getUserControlRecordList(UserControlRecordVO vo);
	
	void updateUserControlRecordInfo(UserControlListBean userControlListBean,BigDecimal winLose);
	
	void saveUserControlRecordInfo(UserControlListBean userControlListBean,BigDecimal winLose);
}

