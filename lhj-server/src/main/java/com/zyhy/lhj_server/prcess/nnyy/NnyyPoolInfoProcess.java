/**
 * 
 */
package com.zyhy.lhj_server.prcess.nnyy;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.pool.DragonPool;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.manager.pool.PoolManager;
import com.zyhy.lhj_server.prcess.result.nnyy.NnyyPoolResult;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class NnyyPoolInfoProcess extends AbstractHttpMsgProcess{
	@Autowired
	private PoolManager poolManager;
	@Override
	public int getMessageId() {
		return MessageConstants.NNYY_POOL_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		DragonPool pool = poolManager.getPool();
		NnyyPoolResult res = new NnyyPoolResult();
		res.setGold(new BigDecimal(pool.getGrand()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		res.setRed(new BigDecimal(pool.getMajor()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		res.setBlue(new BigDecimal(pool.getMinor()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		res.setGreen(new BigDecimal(pool.getMini()).setScale(2, BigDecimal.ROUND_DOWN).doubleValue());
		return res;
	}

}
