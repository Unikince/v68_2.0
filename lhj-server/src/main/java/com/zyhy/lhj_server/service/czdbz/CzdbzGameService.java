/**
 * 
 */
package com.zyhy.lhj_server.service.czdbz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Line;
import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_lhj.logic.BaseLogic;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.lhj_server.game.czdbz.CzdbzIconEnum;
import com.zyhy.lhj_server.game.czdbz.CzdbzRollerWeightEnum;
import com.zyhy.lhj_server.game.czdbz.CzdbzWinLineEnum;
import com.zyhy.lhj_server.game.czdbz.poi.impl.CzdbzTemplateService;
import com.zyhy.lhj_server.game.czdbz.poi.template.CzdbzOdds;
import com.zyhy.lhj_server.prcess.result.czdbz.CzdbzGameBetResult;

/**
 * @author linanjun
 *
 */
@Service
public class CzdbzGameService extends BaseLogic{

	@Autowired
	private CzdbzTemplateService templateService;
	/**
	 * 
	 * @param iconId 
	 * @param jetton 档位
	 * @param betnum 下注的线路数
	 * @return
	 */
	public CzdbzGameBetResult doGameProcess(String roleid, BetInfo betinfo, int iconId) {
		CzdbzGameBetResult res = new CzdbzGameBetResult();
		// 获取图标
		List<CzdbzOdds> baseinfos = templateService.getList(CzdbzOdds.class);
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		// 获取指定赔率图标
		Iterator<CzdbzOdds> iterator = baseinfos.iterator();
		while (iterator.hasNext()) {
			CzdbzOdds next = iterator.next();
			if (next.getType() != iconId) {
				iterator.remove();
			}
		}
		//System.out.println("加载进来的图标有:" + baseinfos);
		
		//窗口信息
		List<Window> ws = new ArrayList<Window>();
		for(int i=1; i<=5; i++){
			ws.addAll(CzdbzRollerWeightEnum.windowInfo(i,baseinfos));
		}
		res.setWindowinfos(ws);
		//获胜线路信息 不包含scatter
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
	 * 
	 * @param ws
	 * @param betinfo
	 * @return
	 */
	private WinLineInfo getScatterWininfo(List<Window> ws, BetInfo betinfo) {
		int[] arr = new int[5];
		List<Window> wws = new ArrayList<Window>();
		for(Window w : ws){
			if(w.getIcon().isScatter() || w.getIcon().isWild()){
				arr[w.getId() - 1] = 1;
				wws.add(w);
			}
		}
		//有效个数
		int n = 0;
		for(int a : arr){
			if(a==1){
				n++;
			}else{
				break;
			}
		}
		//反序有效个数
		int num = 0;
		for(int i=4;i>=0;i--){
			if(arr[i] > 0){
				num++;
			}else{
				break;
			}
		}
		if(num > n){
			n = num;
			Collections.reverse(wws);
		}
		if(n > 0){
			double[] trs = CzdbzIconEnum.SCATTER.getTrs();
			if(n > trs.length){
				n = trs.length;
			}
			double tr = trs[n-1];
			if(tr > 0){
				WinLineInfo winfo = new WinLineInfo();
				winfo.setReward(tr);
				winfo.setNum(n);
				winfo.setIcon(CzdbzIconEnum.SCATTER);
				winfo.setId("Scatter");
				Iterator<Window> iterator = wws.iterator();
				while (iterator.hasNext()) {
					Window w = iterator.next();
					if (!w.getIcon().isScatter() && !w.getIcon().isWild()) {
						iterator.remove();
					}
				}
				winfo.setWindows(wws);
				reward(winfo, betinfo);
				return winfo;
			}
		}
		return null;
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
		Line[] ts = ArrayUtils.subarray(CzdbzWinLineEnum.values(), 0, betinfo.getNum());
		for(Line line : ts){
			List<Window> lws = getLineWindows(ws, line);
			//正面
			WinLineInfo wl = getWinLineInfo(lws);
			if(wl != null && !wl.getIcon().isScatter()){
				wl.setOrder(1);
				wl.setWindows(new ArrayList<Window>(lws.subList(0, wl.getNum())));
				wl.setId(String.valueOf(line.getId()));
				reward(wl, betinfo);
				res.add(wl);
			}
			//5个连续时，不再获得反面奖励
			if(wl != null && wl.getNum() == 5){
				continue;
			}
			//反面
			List<Window> lwsreverse = new ArrayList<Window>(lws);
			Collections.reverse(lwsreverse);
			WinLineInfo wl2 = getWinLineInfo(lwsreverse);
			if(wl2 != null && !wl2.getIcon().isScatter()){
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
				wl.setReward(NumberTool.multiply(CzdbzIconEnum.SCATTER.getTrs()[list1.size() - 1], betinfo.total()).doubleValue());
				wl.setWindows(list1);
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
				wl.setReward(NumberTool.multiply(CzdbzIconEnum.SCATTER.getTrs()[list2.size() - 1], betinfo.total()).doubleValue());
				wl.setWindows(list2);
				res.add(wl);
			}
		}
		return res;
	}
	
}
