/**
 *
 */
package com.zyhy.lhj_server.prcess.xywjs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.util.RoundIdUtils;
import com.google.common.collect.Lists;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.util.NumberTool;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.entity.SoltGameInfo;
import com.zyhy.lhj_server.bgmanagement.manager.CacheManager;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.xywjs.IconEnum;
import com.zyhy.lhj_server.game.xywjs.PlayerRecord;
import com.zyhy.lhj_server.game.xywjs.WinIconInfo;
import com.zyhy.lhj_server.game.xywjs.poi.impl.FruitTemplateService;
import com.zyhy.lhj_server.game.xywjs.poi.template.FruitOdds;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsBeginGameResult;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsLuckyGameResult;
import com.zyhy.lhj_server.service.xywjs.XywjsGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 开始游戏
 */
@Order
@Component
public class XywjsStartGameProcess extends AbstractHttpMsgProcess {
    @Autowired
    private BgManagementServiceImp bgManagementServiceImp;
    @Autowired
    private XywjsGameService gameService;
    @Autowired
    private FruitTemplateService templateService;
    @Autowired
    private UserService userService;
    private int id = 0; // 上一局使用图标序列id
    private static final Logger LOG = LoggerFactory.getLogger(XywjsStartGameProcess.class);
    @Override
    public int getMessageId() {
        return MessageConstants.XYWJS_START_GAME_INFO;
    }

    @Override
    public HttpMessageResult handler(String uuid, Map<String, String> body)
            throws Throwable {
        XywjsBeginGameResult result = new XywjsBeginGameResult();

        double exchangeRate = CacheManager.instance().getExchangeRate(); // 当前汇率
        if (exchangeRate <= 0d) {
            result.setRet(2);
            result.setMsg("exchangeRate error!");
            return result;
        }

        String roleid = body.get("roleid");
        // 下注信息(例:0,0,0,0,0,0,0,0)
        String xiazhu = body.get("xiazhu").toString();
        if ("0,0,0,0,0,0,0,0".equals(xiazhu)) {
            result.setRet(2);
            result.setMsg("请下注");
            return result;
        }

        // 验证下注游戏币是否超额
        double totalBet = 0;  // 总投注
        Map<Integer, Double> betMap = new HashMap<>(); // 下注信息
        String[] xiazhustr = xiazhu.split(",");
        for (int i = 0; i < xiazhustr.length; i++) {
            double num = Double.valueOf(xiazhustr[i]);
            if (num > Constants.MAX_PUT_GAME * exchangeRate) {
                result.setRet(2);
                result.setMsg("单项下注上限=" + Constants.MAX_PUT_GAME * exchangeRate);
                return result;
            }
            totalBet += num;
            if (num > 0) {
                betMap.put(i, num);
            }
        }

        // 用户信息
        Player userinfo = userService.getUserInfo(roleid, uuid);
        if (userinfo == null) {
            result.setRet(2);
            result.setMsg("登录错误");
            return result;
        }
        // 玩家游戏币
        double usercoin = userinfo.getGold();
        // 下注前金币
        double startbalance = usercoin;
        if (totalBet > usercoin) {
            result.setRet(4);
            result.setMsg("游戏币不足");
            return result;
        }
        // 回合id
        String roundId = MessageIdEnum.XYWJS.getGameId() + "|" + userService.getOrderId();
        // 下注
        boolean betResult = userService.bet(roleid, totalBet, roundId, uuid, Constants.BETTYPE1,MessageIdEnum.XYWJS.getBgId());
        if (!betResult) {
            result.setRet(2);
            result.setMsg("下注失败");
            return result;
        }
        
        // 本局游戏使用的图标id
        int iconId = 0;
        // 点控
        boolean pointControl = false; 
        int pointControlModel = bgManagementServiceImp.getPointControlModel(roleid);
        if (pointControlModel > 0) {
        	pointControl = true;
        	iconId = pointControlModel;
        	LOG.info("本局为点控状态,PointControlModel =====>" + pointControlModel);
		}
        
        // 自控
        if (iconId == 0) {
        	int autoControlModel = bgManagementServiceImp.getAutoControlModel(roleid);
        	if (autoControlModel > 0) {
        		iconId = autoControlModel;
             	LOG.info("本局为自控状态,AutoControlModel =====>" + autoControlModel);
			}
		}
    	 
        // 库存控制
    	boolean InventoryControl = false; 
    	if (iconId == 0) {
    		int inventoryControlModel = bgManagementServiceImp.getInventoryControlModel(MessageIdEnum.XYWJS.getGameId(), roleid);
    		if (inventoryControlModel > 0) {
    			InventoryControl = true;
        		iconId = inventoryControlModel;
        		LOG.info("本局为库存控制状态,inventoryControlModel =====>" + inventoryControlModel);
			}
		}
    	
    	 // 系统自控
    	if (iconId == 0) {
    		int sgjSystemControlModel = bgManagementServiceImp.getSgjSystemControlModel(MessageIdEnum.XYWJS.getRedisName(), id);
    		if (sgjSystemControlModel > 0) {
    			iconId = sgjSystemControlModel;
    			LOG.info("本局为系统控制状态,sgjSystemControlModel =====>" + sgjSystemControlModel);
			}
		}
        
        // 保存本局使用的图标id
        id = iconId;
        
        /*// 查询当前赔率
    	 SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(SoltGameNameEnum.XYWJS.getRedisName());
    	 LOG.info("GameInfo =====>" + gameInfo);
        double odds = bgManagementServiceImp.queryCurrentOdds(gameInfo);
        iconId = XywjsOddsEnum.getIdByOdds(odds);
        
        // 如果图标是升序,就继续使用上一局的图标
        if (id > 0 && iconId > id && iconId < 4) {
            iconId = id;
        }
        
        // 如果没有取到图标,就继续使用上一局图标
        if (iconId == 0 && id > 0) {
            iconId = id;
        } else if (iconId == 0 && id == 0) { // 如果都没有取到图标,就使用第4套图标
            iconId = 4;
        }*/
        
        // 用户水果机信息
        List<FruitOdds> baseinfos = templateService.getList(FruitOdds.class);

        // 爆奖模式概率
        SoltGameInfo gameInfo = bgManagementServiceImp.queryGameInfo(MessageIdEnum.XYWJS.getRedisName());
        double bigRewardOdds = gameInfo.getBigRewardOdds();
        int random = RandomUtil.getRandom(1, 100);
        boolean isBigReward = false;  // 是否为爆奖模式
        if (random <= bigRewardOdds * 100) { // 爆奖模式
        	LOG.info("本局开奖模式为 =====> 爆奖模式,使用iconId = " + 10);
            isBigReward = true;
            List<FruitOdds> bigRewardBaseinfos = new ArrayList<>();
            for (FruitOdds fruitOdds : baseinfos) {
            	// 第10套图标为爆奖模式
                if (fruitOdds.getType() == 10) {
                    bigRewardBaseinfos.add(fruitOdds);
                }
            }
            // 执行水果机逻辑
            result = gameService.computerGameProcess(bigRewardBaseinfos, betMap);
            // 验证大奖金额
            if (result.getWinnumber() > 0) {
                double checkAmount = gameInfo.getCheckAmount(); // 验证大奖金额倍率
                if (result.getWinnumber() / totalBet >= checkAmount && checkAmount > 0) {
                    isBigReward = bgManagementServiceImp.checkBigReward(gameInfo, result.getWinnumber());
                }
            }
			
			/*if (isBigReward) {
				// 当前使用那套图标
				result.setIcon(iconId);
				// 当前的赔率
				System.out.println("总派彩" + gameInfo.getTotalPay());
				System.out.println("总投注" + gameInfo.getTotalBet());
				double odds1 = gameInfo.getTotalPay()/gameInfo.getTotalBet();
				result.setOdds(odds1);
				System.out.println("爆奖模式============================================================================>总赔率" + odds1);
			}*/

        }

        if (!isBigReward) { // 小奖模式与正常模式
            // 获取指定赔率图标
            Iterator<FruitOdds> iterator = baseinfos.iterator();
            while (iterator.hasNext()) {
                FruitOdds next = iterator.next();
                if (next.getType() != iconId) {
                    iterator.remove();
                }
            }
            //System.out.println("加载进来的图标有:" + baseinfos);

            // 小奖模式概率
            double smallRewardOdds = 0.09;
            if (random <= (smallRewardOdds + bigRewardOdds) * 100  // 小奖模式
                    && random > bigRewardOdds * 100) {
            	LOG.info("本局开奖模式为 =====> 小奖模式,使用iconId = " + iconId);
                result = gameService.smallRewardGameProcess(baseinfos, betMap);
                // 验证大奖金额
                if (result.getWinnumber() > 0) {
                    double checkAmount = gameInfo.getCheckAmount(); // 验证大奖金额倍率
                    if (result.getWinnumber() / totalBet >= checkAmount && checkAmount > 0) {
                        boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo, result.getWinnumber());
                        if (!checkBigReward) { // 大奖判断未过就判断lose
                            result = gameService.getLuckyLoseModel();
                        }
                    }
                }
				
				/*// 当前使用那套图标
				result.setIcon(iconId);
				// 当前的赔率
				System.out.println("总派彩" + gameInfo.getTotalPay());
				System.out.println("总投注" + gameInfo.getTotalBet());
				double odds1 = gameInfo.getTotalPay()/gameInfo.getTotalBet();
				result.setOdds(odds1);
				System.out.println("总赔率" + odds1);*/

            } else { // 正常模式
            	LOG.info("本局开奖模式为 =====> 正常模式,使用iconId = " + iconId);
                // 执行水果机逻辑
                result = gameService.computerGameProcess(baseinfos, betMap);
                // 验证大奖金额
                if (result.getWinnumber() > 0) {
                    double checkAmount = gameInfo.getCheckAmount(); // 验证大奖金额倍率
                    if (result.getWinnumber() / totalBet >= checkAmount && checkAmount > 0) {
                        boolean checkBigReward = bgManagementServiceImp.checkBigReward(gameInfo, result.getWinnumber());
                        if (!checkBigReward) {
                            int count = 0;
                            while (true) {
                                count++;
                                result = gameService.computerGameProcess(baseinfos, betMap);
                                if (result.getWinnumber() / totalBet < checkAmount) break;
                                if (count == 50) {
                                    result = gameService.getLuckyLoseModel();
                                    break;
                                }
                            }
                        }
                    }
                }
				
				/*// 当前使用那套图标
				result.setIcon(iconId);
				// 当前的赔率
				System.out.println("总派彩" + gameInfo.getTotalPay());
				System.out.println("总投注" + gameInfo.getTotalBet());
				double odds1 = gameInfo.getTotalPay()/gameInfo.getTotalBet();
				result.setOdds(odds1);
				System.out.println("总赔率" + odds1);*/

            }
        }
        
        // 派彩
        double payout = 0;
        if (result.getWinnumber() > 0) {
            // 返奖额度增加10倍
            //result.setWinnumber(result.getWinnumber() * 10); 调整为真钱1比1
            result.setWinnumber(result.getWinnumber());
            userService.payout(roleid, result.getWinnumber(), roundId, uuid,MessageIdEnum.XYWJS.getBgId());
            payout = result.getWinnumber();
            // 保存大奖
            //gameService.saveBigReward(Constants.FKWJS_SERVER_NAME, betInfo, result.getWinnumber(),roleid);
        }
        //金币变化
        double change = result.getWinnumber() - totalBet;
        result.setGold(startbalance + change);
        // 如果是库存控制,就更新本局金币变化
        if (InventoryControl) {
        	bgManagementServiceImp.updateInventoryControlInfo(change, MessageIdEnum.XYWJS.getGameId());
		}

        // 发送日志
        List<PlayerRecord> list = Lists.newArrayList(getPlayerWinLoseInfo(roleid, betMap, baseinfos, result));
        GameRecordDTO<PlayerRecord> record = GameRecordDTO.<PlayerRecord>builder()
                .gameDate(new Date(System.currentTimeMillis()))
                .userId(Long.parseLong(roleid))
                .userName(userinfo.getNickname())
                .gameId(MessageIdEnum.XYWJS.getBgId())
                .gameName(Constants.XYWJS_GAME_NAME)
                .fileId(1)
                .fileName("平民场")
                .gameResult(list)
                .beforeGameGold(BigDecimal.valueOf(startbalance))
                .afterGameGold(BigDecimal.valueOf(result.getGold()))
                .betsGold(BigDecimal.valueOf(totalBet))
                .winLosGold(BigDecimal.valueOf(change))
                .serviceCharge(new BigDecimal(0))
                .controlState(pointControl)
                .isRobot(false)
                .roundCode(RoundIdUtils.getGameRecordId(String.valueOf(MessageIdEnum.XYWJS.getBgId()), roleid))
                .build();
        bgManagementServiceImp.updaeInventory(record); // 更新库存
        userService.sendLog(record);
        return result;
    }

    /**
     *  获取玩家输赢信息
     */
    /**
     * @param roleId 角色id
     * @param room 房间
     * @param winInfo 胜利信息
     * @param pumpRate 抽水比例
     * @return
     */
    private PlayerRecord getPlayerWinLoseInfo(String roleId, Map<Integer, Double> betMap, List<FruitOdds> baseinfos, XywjsBeginGameResult result) {
        PlayerRecord playerRecord = new PlayerRecord();
        playerRecord.setGameType(1);
        List<WinIconInfo> winIconInfoList = new ArrayList<>();
        List<WinIconInfo> winLoseInfoList = new ArrayList<>();

        int zhuanpanid = result.getZhuanpanid();
        List<XywjsLuckyGameResult> luckyresults = result.getLuckyresults();
        // 正常奖励图标
        WinIconInfo winIconInfo = getWinIcon(betMap, baseinfos, zhuanpanid);
        winIconInfoList.add(winIconInfo);
        // lucky奖励图标
        if (!luckyresults.isEmpty()) {
            for (XywjsLuckyGameResult r : luckyresults) {
                WinIconInfo wi = getWinIcon(betMap, baseinfos, r.getZhuanpanid());
                winIconInfoList.add(wi);
            }
        }

        // 输赢的信息
        for (IconEnum e : IconEnum.values()) {
            WinIconInfo wi = new WinIconInfo();
            wi.setId(e.getId()); // 图标对应的下注id
            wi.setName(e.getName());
            wi.setWinLoseGold(0);
            wi.setBet(0);
            FruitOdds iconLvByBetId = getIconLvByBetId(e.getId(), baseinfos);
            wi.setLv(iconLvByBetId.getMultiple());
            for (Integer id : betMap.keySet()) {
                if (id == e.getId()) {
                    wi.setBet(betMap.get(id));
                }
            }
            for (WinIconInfo w : winIconInfoList) {
                if (e.getId() == w.getId()) {
                    wi.setWinLoseGold(w.getWinLoseGold());
                    break;
                }
            }
            winLoseInfoList.add(wi);
        }
        playerRecord.setWinIconInfoList(winIconInfoList);
        playerRecord.setWinLoseInfoList(winLoseInfoList);
        return playerRecord;
    }

    private WinIconInfo getWinIcon(Map<Integer, Double> betMap, List<FruitOdds> baseinfos, int zhuanpanid) {
        // 获胜的图标
        FruitOdds fruitOdds = getIconNameById(zhuanpanid, baseinfos);
        WinIconInfo winIconInfo = new WinIconInfo();
        if (zhuanpanid == 9 || zhuanpanid == 21) {
            winIconInfo.setName("LUCKY");
            winIconInfo.setLv(0);
            winIconInfo.setBet(0);
            winIconInfo.setWinLoseGold(0);
            winIconInfo.setId(-1);  // 图标对应的下注id
        } else {
            winIconInfo.setName(IconEnum.getIconEnumById(zhuanpanid).getName());
            winIconInfo.setLv(fruitOdds.getMultiple());
            winIconInfo.setBet(0);
            winIconInfo.setWinLoseGold(0);
            int betIdByicon = IconEnum.getBetIdByicon(fruitOdds.getSign());
            winIconInfo.setId(betIdByicon);  // 图标对应的下注id
            for (Integer id : betMap.keySet()) {
                if (id == betIdByicon) {
                    winIconInfo.setBet(betMap.get(id));
                    winIconInfo.setWinLoseGold(NumberTool.multiply(betMap.get(id), winIconInfo.getLv()).doubleValue());
                    break;
                }
            }
        }
        return winIconInfo;
    }

    /**
     * 通过id获取图标
     * @return
     */
    private FruitOdds getIconNameById(int id, List<FruitOdds> baseinfos) {
        for (FruitOdds fruitOdds : baseinfos) {
            if (fruitOdds.getSign() == id) {
                return fruitOdds;
            }
        }
        return null;
    }

    /**
     * 通过下注id获取图标{3,15,19,7,13,6,12,4}
     * @return
     */
    private FruitOdds getIconLvByBetId(int id, List<FruitOdds> baseinfos) {
        int[] icons = {3, 15, 19, 7, 13, 6, 12, 4};
        List<FruitOdds> temp = new ArrayList<>();
        for (FruitOdds fruitOdds : baseinfos) {
            if (fruitOdds.getBet() == id) {
                temp.add(fruitOdds);
            }
        }

        for (int icon : icons) {
            for (FruitOdds fruitOdds : temp) {
                if (icon == fruitOdds.getSign()) {
                    return fruitOdds;
                }
            }
        }
        return null;
    }
}
