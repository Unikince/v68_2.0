/**
 * 
 */
package com.zyhy.lhj_server.service.nnyy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.game.alsj.poi.template.AlsjOdds;
import com.zyhy.lhj_server.game.nnyy.NnyyIconEnum;
import com.zyhy.lhj_server.game.nnyy.NnyyRollerWeightEnum;
import com.zyhy.lhj_server.game.nnyy.NnyyWinLineEnum;
import com.zyhy.lhj_server.game.nnyy.poi.impl.NnyyTemplateService;
import com.zyhy.lhj_server.game.nnyy.poi.template.NnyyOdds;
import com.zyhy.lhj_server.prcess.result.nnyy.NnyyGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class NnyyGameService extends BaseLogic {
	@Autowired
	private NnyyTemplateService templateService;
	public NnyyGameBetResult doGameProcess(String roleid, BetInfo betinfo, Player userinfo, String uuid, int iconId) {
		NnyyGameBetResult res = new NnyyGameBetResult();
		// 获取图标
		List<NnyyOdds> baseinfos = templateService.getList(NnyyOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<NnyyOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			NnyyOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 窗口信息
		List<Window> ws = new ArrayList<Window>();
		for (int i = 1; i <= 5; i++) {
			ws.addAll(NnyyRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		// 获胜线路信息
		List<WinLineInfo> wins = getWininfo(ws, betinfo);
		
		//计算总奖励
		if(wins != null && wins.size() > 0){
			//总的奖励数
			double reward = 0;
			for(WinLineInfo w : wins){
				reward += w.getReward() ;
			}
			res.setRewardcoin(reward);
		}
		res.setWinrouteinfos(wins);
		return res;
	}

	/**
	 * 检测scatter
	 * 
	 * @param list
	 * @return
	 */
	/*
	public boolean chickFree(List<WindowInfo> list) {
		for (WindowInfo w : list) {
			if (w.getIcon() == NnyyIconEnum.SCATTER) {
				return true;
			}
		}
		return false;
	}*/

	/**
	 * 得到获胜线路信息
	 * 
	 * @param ws
	 * @param betnum
	 *            线路数
	 * @return
	 */
	private List<WinLineInfo> getWininfo(List<Window> ws, BetInfo betinfo) {
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		// 获取所有线路
		Line[] ts = ArrayUtils.subarray(NnyyWinLineEnum.values(), 0, betinfo.getNum());
		for (Line line : ts) {
			List<Window> lws = getLineWindows(ws, line);
			// 正面
			WinLineInfo wl = getWinLineInfo(lws);
			if (wl != null && !wl.getIcon().isScatter()) {
				wl.setOrder(1);
				wl.setWindows(new ArrayList<Window>(lws.subList(0, wl.getNum())));
				wl.setId(String.valueOf(line.getId()));
				reward(wl, betinfo);
				res.add(wl);
			}
			// 5个连续时，不再获得反面奖励
			if (wl != null && wl.getNum() == 5) {
				continue;
			}
			// 反面
			List<Window> lwsreverse = new ArrayList<Window>(lws);
			Collections.reverse(lwsreverse);
			WinLineInfo wl2 = getWinLineInfo(lwsreverse);
			if (wl2 != null && !wl2.getIcon().isScatter()) {
				wl2.setOrder(2);
				ArrayList<Window> arrayList = new ArrayList<Window>(lwsreverse.subList(0, wl2.getNum()));
				Collections.reverse(arrayList);
				wl2.setWindows(arrayList);
				wl2.setId(String.valueOf(line.getId()));
				reward(wl2, betinfo);
				res.add(wl2);
			}
		}
		// 单独计算scatter
		List<WinLineInfo> checkScatter = checkScatter(ws , betinfo);
		if(checkScatter.size() > 0){
			res.addAll(checkScatter);
		}
		return res;
	}
	
	public void reward(WinLineInfo wl, BetInfo betinfo) {
		if(wl != null){
			rewardWail(wl);
			wl.setReward(NumberTool.multiply(wl.getReward(), betinfo.getGold()).doubleValue());
		}
	}
	
	/**
	 * 计算wild奖励
	 */
	public void rewardWail(WinLineInfo wl) {
		if(wl.validWailNum() > 0){
			wl.setReward(wl.getReward()*2);
		}
	}
	
	/**
	 * 检查scatter奖励
	 */
	public List<WinLineInfo>  checkScatter(List<Window> ws , BetInfo betinfo){
		List<WinLineInfo> res = new ArrayList<WinLineInfo>();
		List<Window> scatter = new ArrayList<>();
		for (Window w : ws) {
			if(w.getIcon().isScatter() ||w.getIcon().isWild()){
				scatter.add(w);
			}
		}
		
		if (scatter.size() > 0) {
			// 判断正面
			List<Window> list1 = new ArrayList<>();
			
			int id = scatter.get(0).getId();
			if(id == 1){
				list1.add(scatter.get(0));
				for (int i = 1; i < scatter.size(); i++) {
					if(scatter.get(i).getId() == list1.get(list1.size() -1).getId()){
						if(scatter.get(i).getIcon() != list1.get(list1.size() -1).getIcon()){
							if(!list1.get(list1.size() -1).getIcon().isScatter()){
								list1.set(list1.size() -1, scatter.get(i));
							}
						} 
						continue;
					}
					if((scatter.get(i).getId() - 1) == id ){
						list1.add(scatter.get(i));
						id = scatter.get(i).getId();
					}
				}
			}
			if(list1.size() > 1){
				WinLineInfo wl = new WinLineInfo();
				wl.setIcon(list1.get(0).getIcon());
				wl.setId("Scatter");
				wl.setNum(list1.size());
				wl.setOrder(1);
				wl.setReward(NumberTool.multiply(NnyyIconEnum.SCATTER.getTrs()[list1.size() - 1], betinfo.total()).doubleValue());
				wl.setWindows(list1);
				//System.out.println("正面的wl======" + wl);
				res.add(wl);
			}
			
			if (list1.size() == 5) {
				return res;
			}
			
			// 判断反面
			List<Window> list2 = new ArrayList<>();
			List<Window> scatter1 = new ArrayList<>(scatter);
			Collections.reverse(scatter1);
			int id1 = scatter1.get(0).getId();
			if(id1 == 5){
				list2.add(scatter1.get(0));
				for (int i = 1; i < scatter1.size(); i++) {
					if(scatter1.get(i).getId() == list2.get(list2.size() -1).getId() ){
						if(scatter1.get(i).getIcon() != list2.get(list2.size() -1).getIcon()){
							if(!list2.get(list2.size() -1).getIcon().isScatter()){
								list2.set(list2.size() -1, scatter1.get(i));
							}
						} 
						continue;
					}
					
					if((scatter1.get(i).getId() + 1) == id1 ){
						list2.add(scatter1.get(i));
						id1 = scatter1.get(i).getId();
					}
				}
			}
			if(list2.size() > 1){
				WinLineInfo wl = new WinLineInfo();
				wl.setIcon(list2.get(0).getIcon());
				wl.setId("Scatter");
				wl.setNum(list2.size());
				wl.setOrder(2);
				wl.setReward(NumberTool.multiply(NnyyIconEnum.SCATTER.getTrs()[list2.size() - 1], betinfo.total()).doubleValue());
				wl.setWindows(list2);
				//System.out.println("反面的wl======" + wl);
				res.add(wl);
			}
		}
		return res;
	}
	
	/**
	 * 转换为双list格式
	 */
	public List<List<WindowInfo>> formatDouble(List<Window> ws) {
		List<List<WindowInfo>> allList = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			List<WindowInfo> list = new ArrayList<>();
			for (Window w : ws) {
				WindowInfo wi1 = (WindowInfo) w;
				if (wi1.getId() == i) {
					list.add(wi1);
				}
			}
			allList.add(list);
		}
		return allList;
	}

	/**
	 * 转换为单list格式
	 */
	public List<Window> formatSingle(List<List<WindowInfo>> wi) {
		List<Window> formatSingle = new ArrayList<>();
		for (List<WindowInfo> list : wi) {
			for (WindowInfo windowInfo : list) {
				formatSingle.add(windowInfo);
			}
		}
		return formatSingle;
	}
}
