package com.dmg.lobbyserver.process.activity.turn;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.config.MessageConfig;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.TurnTableConfigDao;
import com.dmg.lobbyserver.dao.TurnTableWinLogDao;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.TurnTableConfigBean;
import com.dmg.lobbyserver.dao.bean.TurnTableWinLogBean;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.model.dto.TurnTableDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.process.activity.UserProessService;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.service.RedPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.dmg.lobbyserver.config.MessageConfig.TURN_TABLE_EXTRACT;

/**
 * 转盘抽奖
 * Author:刘将军
 * Time:2019/6/19 14:52
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class TurnTableExtractProcess implements AbstractMessageHandler {

    @Autowired
    private SysItemConfigDao sysItemConfigDao;
    @Autowired
    private UserProessService userProessService;
    @Autowired
    private TurnTableConfigDao turnTableConfigDao;
    @Autowired
    private TurnTableWinLogDao turnTableWinLogDao;
    @Autowired
    private GiftDataService giftDataService;
    @Autowired
    private RedPointService redPointService;


    @Override
    public String getMessageId() {
        return TURN_TABLE_EXTRACT;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        //只能抽一次
        //Integer countToday = turnTableWinLogDao.selectCountToday(Long.valueOf(userid));
        int hasturn = turnTableWinLogDao.selectCount(new LambdaQueryWrapper<TurnTableWinLogBean>().eq(TurnTableWinLogBean::getWinUserId, Long.valueOf(userid)));
        if (hasturn != 0) {
            result.setRes(ResultEnum.EXTRACT_OUT.getCode());
            result.setMsg(ResultEnum.EXTRACT_OUT.getMsg());
            return;
        }
        //奖品列表
        List<TurnTableConfigBean> turnTableConfigBeans = turnTableConfigDao.selectList(null);
        //抽奖算法
        Integer sign = 0;//概率
        for (TurnTableConfigBean prizeInfo : turnTableConfigBeans) {
            sign += prizeInfo.getProbability();
        }
        Random random = new Random();
        int sb = random.nextInt(sign);
        Integer sign_2 = 0;//概率
        TurnTableConfigBean prizeInfo_2 = new TurnTableConfigBean();
        for (TurnTableConfigBean prizeInfo : turnTableConfigBeans) {
            sign_2 += prizeInfo.getProbability();
            if (sb <= sign_2) {
                prizeInfo_2 = prizeInfo;
                break;
            }
        }
        //抽中的物品
        SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectById(prizeInfo_2.getItemId());
        //中奖记录
        TurnTableWinLogBean turnTableWinLogBean = new TurnTableWinLogBean();
        turnTableWinLogBean.setItemId(prizeInfo_2.getItemId());
        turnTableWinLogBean.setItemNumber(prizeInfo_2.getItemNumber());
        turnTableWinLogBean.setItemOrder(prizeInfo_2.getItemOrder());
        turnTableWinLogBean.setWinDate(new Date());
        turnTableWinLogBean.setWinUserId(Long.valueOf(userid));
        turnTableWinLogDao.insert(turnTableWinLogBean);
        //加到用户的账户上去
        userProessService.getItems(sysItemConfigBean.getItemType(), prizeInfo_2.getItemNumber(), userid);
        //返回抽中的奖品
        if (sysItemConfigBean.getItemType()!=3){
            GiftDataDTO giftDataDTO = new GiftDataDTO();
            BeanUtils.copyProperties(sysItemConfigBean, giftDataDTO);
            giftDataDTO.setItemNumber(String.valueOf(prizeInfo_2.getItemNumber()));
            List<GiftDataDTO> turnTableDataDTOS = new ArrayList<>();
            turnTableDataDTOS.add(giftDataDTO);
            giftDataService.sendGiftData(Long.parseLong(userid),turnTableDataDTOS);
        }
        //返回抽中的奖品
        TurnTableDataDTO turnTableDataDTO = new TurnTableDataDTO();
        turnTableDataDTO.setItemOrder(prizeInfo_2.getItemOrder());
        BeanUtils.copyProperties(sysItemConfigBean, turnTableDataDTO);
        turnTableDataDTO.setItemNumber(String.valueOf(prizeInfo_2.getItemNumber()));
        result.setMsg(turnTableDataDTO);
        redPointService.push(Long.parseLong(userid), MessageConfig.TURN_TABLE_NTC,false);
    }
}
