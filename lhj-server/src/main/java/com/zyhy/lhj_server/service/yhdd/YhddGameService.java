/**
 * 
 */
package com.zyhy.lhj_server.service.yhdd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Icon;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.game.yhdd.YhddIconEnum;
import com.zyhy.lhj_server.game.yhdd.YhddRollerWeightEnum;
import com.zyhy.lhj_server.game.yhdd.YhddWinLineEnum;
import com.zyhy.lhj_server.game.yhdd.poi.impl.YhddTemplateService;
import com.zyhy.lhj_server.game.yhdd.poi.template.YhddOdds;
import com.zyhy.lhj_server.prcess.result.yhdd.YhddGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class YhddGameService extends BaseLogic{
	@Autowired
	private YhddBonusService bonusService;
	@Autowired
	private YhddTemplateService templateService;
	/**
	 * 
	 * @param iconId 
	 * @param jetton 档位
	 * @param betnum 下注的线路数
	 * @return
	 */
	public YhddGameBetResult doGameProcess(String roleid, BetInfo betinfo, int iconId) {
		
		YhddGameBetResult res = new YhddGameBetResult();
		// 获取图标
		List<YhddOdds> baseinfos = templateService.getList(YhddOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<YhddOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			YhddOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		//窗口信息
		List<Window> ws = new ArrayList<Window>();
		for(int i=1; i<=5; i++){
			ws.addAll(YhddRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		//获胜线路信息
		List<WinLineInfo> wins = getWininfo(ws, betinfo);
		//计算总奖励
		double totalReward = 0;
		if(wins != null && wins.size() > 0){
			//总的奖励数
			for(WinLineInfo w : wins){
				double reward = NumberTool.multiply(w.getReward(), betinfo.getGold()).doubleValue();
				totalReward += reward;
				w.setReward(reward);
			}
		}
		//scatter奖励
		double scatterReward = 0;
		WinLineInfo ckickSatter = CkickSatter(ws);
		if(ckickSatter.getNum() > 2){
			scatterReward = NumberTool.multiply(YhddIconEnum.SCATTER.getTrs()[CkickSatter(ws).getNum() - 1],betinfo.getTotalBet()).doubleValue();
			res.setScatterReward(scatterReward);
			ckickSatter.setReward(scatterReward);
			wins.add(ckickSatter);
		}
		res.setRewardcoin(NumberTool.add(totalReward, scatterReward).doubleValue());
		res.setWinrouteinfos(wins);
		
		//bonus小游戏是否触发
		List<Window> bonuswins = bonusService.getBonusWindows(ws);
		if(bonuswins != null){
			WinLineInfo wl = new WinLineInfo();
			wl.setWindows(bonuswins);
			wl.setIcon(YhddIconEnum.BONUS);
			res.getWinrouteinfos().add(wl);
			res.setBonus(true);
		}
		return res;
	}

	/**
	 * 判断scatter奖励
	 * @param ws
	 * @return
	 */
	public WinLineInfo CkickSatter(List<Window> ws){
		List<Window> list = new ArrayList<>();
			for(Window w:ws){
				if(w.getIcon() == YhddIconEnum.SCATTER){
					list.add(w);
				}
			}
			WinLineInfo winfo = new WinLineInfo();
			if (list.size() > 2) {
				winfo.setId("Scatter");
				winfo.setNum(list.size());
				winfo.setIcon(YhddIconEnum.SCATTER);
				winfo.setWindows(list);
			}
		return winfo;
	}

	/**
	 * 得到获胜线路信息
	 * @param ws
	 * @param betnum 线路数
	 * @return
	 */
	private List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		//获取所有线路
		Line[] ts = ArrayUtils.subarray(YhddWinLineEnum.values(), 0, betinfo.getNum());
		for(Line line : ts){
			List<Window> lws = getLineWindows(ws, line);
			//正面
			WinLineInfo wl = getWinLineInfo(lws);
			if(wl != null && !wl.getIcon().isScatter()){
				wl.setOrder(1);
				wl.setId(String.valueOf(line.getId()));
				//reward(wl, betinfo);
				res.add(wl);
			}
		}
		return res;
	}
	
	@Override
	public WinLineInfo getWinLineInfo(List<Window> lineWindows) {
		int wail = 0;
		int num = 0;
		//是否scatter触发免费游戏
		Icon lineIcon = null;
		int size = lineWindows.size();
		for(int i=0;i<size;i++){
			Icon icon = lineWindows.get(i).getIcon();
			if(i > 0 && i != wail && !icon.isWild() && lineIcon != icon){
				break;
			}
			if(icon.isScatter()){
				lineIcon = icon;
			}else if(icon.isWild()){
				wail++;
			}else if(icon.isBar()){
				lineIcon = icon;
			}else{
				lineIcon = icon;
			}
			num++;
		}
		if(num > 0){
			if(lineIcon == null){
				lineIcon = lineWindows.get(0).getIcon();
			}
			double v = lineIcon.getTrs()[num-1];
			if(v > 0){
				WinLineInfo winfo = new WinLineInfo();
				winfo.setReward(v);
				winfo.setNum(num);
				winfo.setIcon(lineIcon);
				winfo.setWindows(new ArrayList<Window>(lineWindows.subList(0, num)));
				return winfo;
			}
		}
		return null;
	}

	@Override
	public void rewardWail(WinLineInfo wl, BetInfo betinfo) {
		return;
	}
}
