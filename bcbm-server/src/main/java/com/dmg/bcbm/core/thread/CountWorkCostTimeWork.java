package com.dmg.bcbm.core.thread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.abs.work.TimeWork;
import com.dmg.bcbm.core.annotation.Cron;

/**
 * @author zhuqd
 * @Date 2017年7月25日
 */
@Cron("10 */10 * * * ?")
public class CountWorkCostTimeWork extends TimeWork {

	private static Logger logger = LoggerFactory.getLogger(CountWorkCostTimeWork.class);

	@Override
	public void init(Object... args) {
	}

	@Override
	public void go() {
		Map<Class<?>, ThreadCostTime> costTimeCountMap = ThreadWorkCounter.instance().getCostTimeCountMap();
		List<ThreadCostTime> list = new ArrayList<>();
		for (Entry<Class<?>, ThreadCostTime> entry : costTimeCountMap.entrySet()) {
			ThreadCostTime costData = entry.getValue();
			if (costData.getLoop() == 0) {
				continue;
			}
			double avg = costData.getCost() * 1.0 / costData.getLoop();
			int avgRate = (int) (avg * 1000);
			costData.setAvg(avgRate / 1000.0);
			list.add(costData);
		}
		Collections.sort(list);
		logger.info(" ---------------------------------- work cost time count ----------------------------------------");
		for (ThreadCostTime costData : list) {
			String name = costData.getClazz().getSimpleName();
			for (int i = name.length(); i < 28; i++) {
				name += " ";
			}
			String costString = "cost:" + costData.getCost();
			for (int i = costString.length(); i < 12; i++) {
				costString += " ";
			}
			String string = name + costString;
			string += "\tcount: " + costData.getLoop();
			string += "\tavg: " + costData.getAvg();
			string += "\tmax: " + costData.getMax();
			string += "\tmin: " + costData.getMin();
			logger.info(string);
		}
	}

}
