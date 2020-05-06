/**
 * 
 */
package com.zyhy.lhj_server.service.gghz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.game.Icon;
import com.zyhy.lhj_server.game.gghz.GghzIconEnum;
import com.zyhy.lhj_server.game.gghz.GghzRollerWeightEnum;
import com.zyhy.lhj_server.game.gghz.GghzWinInfo;
import com.zyhy.lhj_server.game.gghz.GghzWindowInfo;
import com.zyhy.lhj_server.game.gghz.poi.impl.GghzTemplateService;
import com.zyhy.lhj_server.game.gghz.poi.template.GghzOdds;
import com.zyhy.lhj_server.prcess.result.gghz.GghzGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class GghzGameService extends BaseLogic{
	@Autowired
	private GghzTemplateService templateService;
	public GghzGameBetResult doGameProcess(String roleid, BetInfo betinfo, int iconId) {
		GghzGameBetResult res = new GghzGameBetResult();
		
		// 获取图标
		List<GghzOdds> baseinfos = templateService.getList(GghzOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<GghzOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			GghzOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
			//窗口信息
			List<GghzWindowInfo> ws = new ArrayList<GghzWindowInfo>();
			for(int i=1; i<=3; i++){
				ws.addAll(GghzRollerWeightEnum.windowInfo(i,baseinfos));
			}
			res.setWindowinfos(ws);
			//获胜线路信息
			GghzWinInfo w = getWininfo(ws);
			if(w != null){
				//计算奖励
				double mul = getReward(w, betinfo.getNum());
				res.setRewardcoin(NumberTool.multiply(mul, betinfo.getGold()).doubleValue());
			}
			res.setWinrouteinfos(w);
		return res;
	}

	/**
	 * 得到获胜线路信息
	 * @param ws
	 * @return
	 */
	private GghzWinInfo getWininfo(List<GghzWindowInfo> ws) {
		GghzWindowInfo q1 = null;
		GghzWindowInfo q2 = null;
		GghzWindowInfo q3 = null;
		for (GghzWindowInfo wi : ws) {
			if (wi.getId() == 1 && wi.getIndex() == 2) {
				q1 = wi;
			}
			if (wi.getId() == 2 && wi.getIndex() == 2) {
				q2 = wi;
			}
			if (wi.getId() == 3 && wi.getIndex() == 2) {
				q3 = wi;
			}
		}
		
		if(q1.getRoller().getIcon() != null && q2.getRoller().getIcon() != null && q3.getRoller().getIcon() != null){
			int i1 = q1.getRoller().getIcon().getId();
			int i2 = q2.getRoller().getIcon().getId();
			int i3 = q3.getRoller().getIcon().getId();
			if(i1 == i2 && i2 == i3){
				GghzWinInfo w = new GghzWinInfo();
				w.setId(1);
				w.setIndex1(q1);
				w.setIndex2(q2);
				w.setIndex3(q3);
				return w;
			}else if(i1 > 5 && i2 > 5 && i3 > 5){
				GghzWinInfo w = new GghzWinInfo();
				w.setId(1);
				w.setIndex1(q1);
				w.setIndex2(q2);
				w.setIndex3(q3);
				return w;
			}
		}
		return null;
	}

	/**
	 * 玩家赢得奖励
	 * @param w
	 * @param betnum
	 * @return
	 */
	private double getReward(GghzWinInfo w, int betnum) {
		Icon i1 = w.getIndex1().getRoller().getIcon();
		Icon i2 = w.getIndex2().getRoller().getIcon();
		Icon i3 = w.getIndex3().getRoller().getIcon();
		Icon e = null;
		if(i1 == i2 && i2 == i3){
			e = i1;
		}else if(i1.getId() > 5 
				&& i2.getId() > 5
				&& i3.getId() > 5){
			e = GghzIconEnum.T9;
			//w.getIndex1().getRoller().setIcon(GghzIconEnum.T9);
			//w.getIndex2().getRoller().setIcon(GghzIconEnum.T9);
			//w.getIndex3().getRoller().setIcon(GghzIconEnum.T9);
		}
		switch (betnum) {
		case 1:
			return e.getTr1();
		case 2:
			return e.getTr2();
		case 3:
			return e.getTr3();
		}
		return 0;
	}
}
