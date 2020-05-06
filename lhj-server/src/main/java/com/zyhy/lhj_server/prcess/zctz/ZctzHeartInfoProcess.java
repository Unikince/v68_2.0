/**
 * 
 */
package com.zyhy.lhj_server.prcess.zctz;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author ASUS
 *
 */
@Order
@Component
public class ZctzHeartInfoProcess extends AbstractHttpMsgProcess{
	private static final Logger LOG = LoggerFactory.getLogger(ZctzHeartInfoProcess.class);
	@Autowired
	private UserService userService;
	@Override
	public int getMessageId() {
		return MessageConstants.ZCTZ_HEART_INFO;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body) throws Throwable {
		String roleid = body.get("roleid");
		HttpMessageResult res = new HttpMessageResult();
		userService.userHeartInfo(roleid,MessageIdEnum.ZCJB.getBgId());
		LOG.info("ZctzHeartInfo =====> " + roleid);
		return res;
	}

}
