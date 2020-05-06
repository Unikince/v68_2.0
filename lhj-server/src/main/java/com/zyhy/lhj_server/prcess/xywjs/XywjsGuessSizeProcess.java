/**
 *
 */
package com.zyhy.lhj_server.prcess.xywjs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.util.RoundIdUtils;
import com.google.common.collect.Lists;
import com.zyhy.common_lhj.Player;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_lhj.process.AbstractHttpMsgProcess;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.common_server.util.RandomUtil;
import com.zyhy.lhj_server.bgmanagement.config.MessageIdEnum;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.constants.Constants;
import com.zyhy.lhj_server.constants.MessageConstants;
import com.zyhy.lhj_server.game.xywjs.IconEnum;
import com.zyhy.lhj_server.game.xywjs.PlayerRecord;
import com.zyhy.lhj_server.game.xywjs.WinIconInfo;
import com.zyhy.lhj_server.game.xywjs.poi.impl.FruitTemplateService;
import com.zyhy.lhj_server.game.xywjs.poi.template.FruitOdds;
import com.zyhy.lhj_server.prcess.result.xywjs.XywjsGuessSizeResult;
import com.zyhy.lhj_server.service.xywjs.XywjsGameService;
import com.zyhy.lhj_server.userservice.UserService;

/**
 * @author linanjun
 * 比大小
 */
@Order
@Component
public class XywjsGuessSizeProcess extends AbstractHttpMsgProcess {
    @Autowired
    private BgManagementServiceImp bgManagementServiceImp;
    @Autowired
    private UserService userService;
    @Autowired
    private XywjsGameService gameService;
    @Autowired
    private JackPoolManager jackPoolManager;
    @Autowired
    private FruitTemplateService templateService;

    @Override
    public int getMessageId() {
        return MessageConstants.XYWJS_GUESS_GAME_INFO;
    }

    @Override
    public HttpMessageResult handler(String uuid, Map<String, String> body)
            throws Throwable {
        XywjsGuessSizeResult result = new XywjsGuessSizeResult();
        String roleid = body.get("roleid");
        // 猜大小 0=小,1=大
        int type = Integer.parseInt(body.get("type").toString());
        // 用户游戏币
        double coin = Double.valueOf(body.get("gold").toString());
        // 下注游戏币
        double gamecoin = Double.valueOf(body.get("gamegold").toString());

        if (coin < 0 || gamecoin < 0) {
            result.setRet(2);
            result.setMsg("参数错误");
            return result;
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
        if ((coin + gamecoin) != usercoin) {
            result.setRet(2);
            result.setMsg("下注游戏币错误");
            return result;
        }

        // 回合id
        String roundId = MessageIdEnum.XYWJS.getGameId() + "|" + userService.getOrderId();

        // 下注
        boolean betResult = userService.bet(roleid, gamecoin, roundId, uuid, Constants.BETTYPE1,MessageIdEnum.XYWJS.getBgId());
        if (!betResult) {
            result.setRet(2);
            result.setMsg("下注失败");
            return result;
        }
        // 开启类型
        int opentype = -1;
        int randomnum = RandomUtil.getRandom(0, 100);
        if (randomnum % 2 == 0) {
            //i为大
            opentype = 1;
        } else {
            //i为小
            opentype = 0;
        }
        double payout = 0;
        if (opentype == type) {
            double wincoin = gamecoin;
            // 胜利,领取金币
            userService.payout(roleid, wincoin * 2, roundId, uuid,MessageIdEnum.XYWJS.getBgId());
            payout = wincoin * 2;
            result.setSizeresult(1);
            result.setGamegold(wincoin * 2);
            //result.setPoolcoin(p.getCurrentnum());
        } else {
            // 失败
            //result.setPoolcoin(p.getCurrentnum());
            result.setSizeresult(0);
            result.setGamegold(0);
        }

        // 发送日志
        List<PlayerRecord> list = Lists.newArrayList(getPlayerWinLoseInfo(roleid, gamecoin, opentype, result));
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
                .afterGameGold(BigDecimal.valueOf(startbalance - result.getGamegold()))
                .betsGold(BigDecimal.valueOf(gamecoin))
                .winLosGold(BigDecimal.valueOf(result.getGamegold()))
                .serviceCharge(new BigDecimal(0))
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
    private PlayerRecord getPlayerWinLoseInfo(String roleId, double gamecoin, int opentype, XywjsGuessSizeResult result) {
        PlayerRecord playerRecord = new PlayerRecord();
        playerRecord.setGameType(2);
        List<WinIconInfo> winIconInfoList = new ArrayList<>();
        List<WinIconInfo> winLoseInfoList = new ArrayList<>();

        // 正常奖励图标
        WinIconInfo winIconInfo = new WinIconInfo();
        winIconInfo.setId(opentype);
        winIconInfo.setName("猜大小");
        winIconInfo.setBet(gamecoin);
        winIconInfo.setWinLoseGold(result.getGamegold());
        winIconInfoList.add(winIconInfo);
        // 输赢的信息
        // 用户水果机信息
        List<FruitOdds> baseinfos = templateService.getList(FruitOdds.class);
        for (IconEnum e : IconEnum.values()) {
            WinIconInfo wi = new WinIconInfo();
            wi.setId(e.getId()); // 图标对应的下注id
            wi.setName(e.getName());
            wi.setWinLoseGold(0);
            wi.setBet(0);
            FruitOdds iconLvByBetId = getIconLvByBetId(e.getId(), baseinfos);
            wi.setLv(iconLvByBetId.getMultiple());
            winLoseInfoList.add(wi);
        }
        playerRecord.setWinIconInfoList(winIconInfoList);
        playerRecord.setWinLoseInfoList(winLoseInfoList);
        return playerRecord;
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
