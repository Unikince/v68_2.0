/**
 * 
 */
package com.zyhy.lhj_server.bgmanagement.prcess.http;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.constants.SoltMessageConstants;
import com.zyhy.lhj_server.bgmanagement.manager.SpringContextUtil;
import com.zyhy.lhj_server.game.ajxz.test.AjxzTest;
import com.zyhy.lhj_server.game.alsj.test.AlsjTest;
import com.zyhy.lhj_server.game.bqtp.test.BqtpTest;
import com.zyhy.lhj_server.game.bxlm.test.BxlmTest;
import com.zyhy.lhj_server.game.czdbz.test.CzdbzTest;
import com.zyhy.lhj_server.game.fkmj.test.FkmjTest;
import com.zyhy.lhj_server.game.gghz.test.gghzTest;
import com.zyhy.lhj_server.game.gsgl.test.GsglTest;
import com.zyhy.lhj_server.game.hjws.test.HjwsTest;
import com.zyhy.lhj_server.game.lll.test.LllTest;
import com.zyhy.lhj_server.game.lqjx.test.lqjxTest;
import com.zyhy.lhj_server.game.nnyy.test.NnyyTest;
import com.zyhy.lhj_server.game.sbhz.test.SbhzTest;
import com.zyhy.lhj_server.game.swk.test.SwkTest;
import com.zyhy.lhj_server.game.tgpd.test.TgpdTest;
import com.zyhy.lhj_server.game.xywjs.test.XywjsTest;
import com.zyhy.lhj_server.game.yhdd.test.YhddTest;
import com.zyhy.lhj_server.game.yzhx.test.YzhxTest;
import com.zyhy.lhj_server.game.zctz.test.ZctzTest;

/**
 * 添加老虎机游戏
 */
@Order
@Component
public class SoltTestProcess extends AbstractHttpMsgProcess{
	@Override
	public int getMessageId() {
		return SoltMessageConstants.SOLTTEST;
	}

	@Override
	public HttpMessageResult handler(String uuid, Map<String, String> body)
			throws Throwable {
		HttpMessageResult result = new HttpMessageResult();
		int gameId = Integer.parseInt(body.get("gameId"));
		
		if (gameId == MessageIdEnum.BXQY.getGameId()) {
			BxlmTest bxlmTest = SpringContextUtil.getBean(BxlmTest.class);
			bxlmTest.initTest();
		} else if (gameId == MessageIdEnum.TGPD.getGameId()) {
			TgpdTest tgpdTest = SpringContextUtil.getBean(TgpdTest.class);
			tgpdTest.initTest();
		} else if (gameId == MessageIdEnum.AJXZ.getGameId()) {
			AjxzTest ajxzTest = SpringContextUtil.getBean(AjxzTest.class);
			ajxzTest.initTest();
		} else if (gameId == MessageIdEnum.BQTP.getGameId()) {
			BqtpTest bqtpTest = SpringContextUtil.getBean(BqtpTest.class);
			bqtpTest.initTest();
		} else if (gameId == MessageIdEnum.CZDBZ.getGameId()) {
			CzdbzTest czdbzTest = SpringContextUtil.getBean(CzdbzTest.class);
			czdbzTest.initTest();
		} else if (gameId == MessageIdEnum.HJWS.getGameId()) {
			HjwsTest hjwsTest = SpringContextUtil.getBean(HjwsTest.class);
			hjwsTest.initTest();
		}  else if (gameId == MessageIdEnum.HYHZ.getGameId()) {
			gghzTest gghzTest = SpringContextUtil.getBean(gghzTest.class);
			gghzTest.initTest();
		} else if (gameId == MessageIdEnum.NNYC.getGameId()) {
			NnyyTest nnyyTest = SpringContextUtil.getBean(NnyyTest.class);
			nnyyTest.initTest();
		} else if (gameId == MessageIdEnum.ZCJB.getGameId()) {
			ZctzTest zctzTest = SpringContextUtil.getBean(ZctzTest.class);
			zctzTest.initTest();
		} else if (gameId == MessageIdEnum.JSGL.getGameId()) {
			GsglTest gsglTest = SpringContextUtil.getBean(GsglTest.class);
			gsglTest.initTest();
		} else if (gameId == MessageIdEnum.ALSJBY.getGameId()) {
			AlsjTest alsjTest = SpringContextUtil.getBean(AlsjTest.class);
			alsjTest.initTest();
		} else if (gameId == MessageIdEnum.YZHX.getGameId()) {
			YzhxTest yzhxTest = SpringContextUtil.getBean(YzhxTest.class);
			yzhxTest.initTest();
		} else if (gameId == MessageIdEnum.LLL.getGameId()) {
			LllTest lllTest = SpringContextUtil.getBean(LllTest.class);
			lllTest.initTest();
		}  else if (gameId == MessageIdEnum.SBHZ.getGameId()) {
			SbhzTest sbhzTest = SpringContextUtil.getBean(SbhzTest.class);
			sbhzTest.initTest();
		} else if (gameId == MessageIdEnum.SWK.getGameId()) {
			SwkTest swkTest = SpringContextUtil.getBean(SwkTest.class);
			swkTest.initTest();
		} else if (gameId == MessageIdEnum.FKMJ.getGameId()) {
			FkmjTest fkmjTest = SpringContextUtil.getBean(FkmjTest.class);
			fkmjTest.initTest();
		} else if (gameId == MessageIdEnum.YHDD.getGameId()) {
			YhddTest yhddTest = SpringContextUtil.getBean(YhddTest.class);
			yhddTest.initTest();
		} else if (gameId == MessageIdEnum.LQJX.getGameId()) {
			lqjxTest lqjxTest = SpringContextUtil.getBean(lqjxTest.class);
			lqjxTest.initTest();
		} else if (gameId == MessageIdEnum.XYWJS.getGameId()) {
			XywjsTest xywjsTest = SpringContextUtil.getBean(XywjsTest.class);
			xywjsTest.initTest();
		}
		
		return result;
		
	}

}
