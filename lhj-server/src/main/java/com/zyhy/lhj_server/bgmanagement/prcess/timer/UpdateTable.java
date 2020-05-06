package com.zyhy.lhj_server.bgmanagement.prcess.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zyhy.lhj_server.bgmanagement.dao.imp.BgManagementDaoImp;
import com.zyhy.lhj_server.bgmanagement.manager.SpringContextUtil;
import com.zyhy.lhj_server.manager.timerWork.annotation.Cron;
import com.zyhy.lhj_server.manager.timerWork.model.TimeWork;

/**
 * 每日0点更新当日流水信息
 * @author Administrator
 *
 */
@Cron("0 0 0 * * ?") //每天晚上0点触发
public class UpdateTable extends TimeWork {
	private static final Logger LOG = LoggerFactory.getLogger(UpdateTable.class);
	@Override
	public void init(Object... args) {
	}

	@Override
	public void go() {
		BgManagementDaoImp bgManagementDaoImp = SpringContextUtil.getBean(BgManagementDaoImp.class);
		try {
			bgManagementDaoImp.updatePlayerWaterRecord();
			bgManagementDaoImp.updateTotalWaterRecord();
			LOG.info("更新当日流水记录成功===========>");
		} catch (Exception e) {
			LOG.info("更新当日流水记录失败===========>");
			e.printStackTrace();
		}
	}

}
