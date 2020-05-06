/**
 * 
 */
package com.zyhy.lhj_server.service.lll;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.game.Icon;
import com.zyhy.lhj_server.game.lll.LllIconEnum;
import com.zyhy.lhj_server.game.lll.LllRollerWeightEnum;
import com.zyhy.lhj_server.game.lll.LllWinInfo;
import com.zyhy.lhj_server.game.lll.LllWindowInfo;
import com.zyhy.lhj_server.game.lll.poi.impl.LllTemplateService;
import com.zyhy.lhj_server.game.lll.poi.template.LllOdds;
import com.zyhy.lhj_server.prcess.result.lll.LllGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class LllGameService extends BaseLogic{
	@Autowired
	private LllTemplateService templateService;

	/**
	 * 
	 * @param totalBet 
	 * @param iconId 
	 * @param jetton 档位
	 * @param betnum 级别
	 * @return
	 */
	public LllGameBetResult doGameProcess(String roleid, BetInfo betinfo, double totalBet, int iconId) {
		//奖池信息
		//JackPool p = jackPoolManager.getPool();
		//boolean k;
		LllGameBetResult res = null;
		//do{
			//k = false;
			res = new LllGameBetResult();
			
			// 获取图标
			List<LllOdds> baseinfos = templateService.getList(LllOdds.class);
			//System.out.println("加载进来的图标有:" + baseinfos);
			
			// 获取指定赔率图标
			Iterator<LllOdds> iterator = baseinfos.iterator();
			while (iterator.hasNext()) {
				LllOdds next = iterator.next();
				if (next.getType() != iconId) {
					iterator.remove();
				}
			}
			//System.out.println("加载进来的图标有:" + baseinfos);
			
			//窗口信息
			List<LllWindowInfo> ws = new ArrayList<LllWindowInfo>();
			for(int i=1; i<=3; i++){
				ws.addAll(LllRollerWeightEnum.windowInfo(i,baseinfos));
			}
			
			for (LllWindowInfo lw : ws) {
				if (lw.getIndex() != 2) {
					lw.getRoller().setIcon(null);
				}
			}
			
			
			res.setWindowinfos(ws);
			//获胜线路信息
			LllWinInfo w = getWininfo(ws);
			if(w != null){
				//计算奖励
				double mul = getReward(w);
				res.setRewardcoin(NumberTool.multiply(mul, totalBet).doubleValue());
			}
			res.setWinrouteinfos(w);
			
			/*if(res.getRewardcoin() > 0 && p.getStatus() == JackPool.STATUS_KILL){
				k = true;
			}else if(res.getRewardcoin() < 0 && p.getStatus() == JackPool.STATUS_WIN){
				k = true;
			}
			
		}while(p.getStatus() != JackPool.STATUS_NORMAL && k);*/
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * @param ws
	 * @return
	 */
	private LllWinInfo getWininfo(List<LllWindowInfo> ws) {
		LllWindowInfo q1 = ws.get(1);
		LllWindowInfo q2 = ws.get(4);
		LllWindowInfo q3 = ws.get(7);
		if(q1.getRoller().getIcon() != null && q2.getRoller().getIcon() != null && q3.getRoller().getIcon() != null){
//			if(i1 == i2 && i2 == i3){
//				LllWinInfo w = new LllWinInfo();
//				w.setId(1);
//				w.setIndex1(q1);
//				w.setIndex2(q2);
//				w.setIndex3(q3);
//				return w;
//			}else if(i1-3 > 0 && i2-3 > 0 && i3-3 > 0){
//				LllWinInfo w = new LllWinInfo();
//				w.setId(1);
//				w.setIndex1(q1);
//				w.setIndex2(q2);
//				w.setIndex3(q3);
//				return w;
//			}
			
			LllWinInfo w = new LllWinInfo();
			w.setId(1);
			w.setIndex1(q1);
			w.setIndex2(q2);
			w.setIndex3(q3);
			return w;
		}
		return null;
	}

	/**
	 * 玩家赢得奖励
	 * @param w
	 * @return
	 */
	private double getReward(LllWinInfo w) {
		Icon i1 = w.getIndex1().getRoller().getIcon();
		Icon i2 = w.getIndex2().getRoller().getIcon();
		Icon i3 = w.getIndex3().getRoller().getIcon();
		Icon e = null;
		if(i1 == i2 && i2 == i3){
			e = i1;
			return e.getTr3();
		}else{
			e = LllIconEnum.T4;
			return e.getTr3();
		}
	}
}
