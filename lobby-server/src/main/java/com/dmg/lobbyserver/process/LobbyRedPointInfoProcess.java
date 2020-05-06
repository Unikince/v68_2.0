package com.dmg.lobbyserver.process;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.*;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.*;

/**
 * @Description 获取大厅红点信息
 * @Author mice
 * @Date 2019/6/19 17:49
 * @Version V1.0
 **/
@Service
public class LobbyRedPointInfoProcess implements AbstractMessageHandler {

    @Autowired
    private TurnTableWinLogDao turnTableWinLogDao;

    @Autowired
    private UserEmailDao userEmailDao;

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    @Autowired
    private SysShareLogDao sysShareLogDao;
    @Autowired
    private PlatformRechargeLogDao platformRechargeLogDao;

    @Override
    public String getMessageId() {
        return LOBBY_RED_POINT_INFO;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Long userId = Long.parseLong(userid);
        List<String> redPointNTCS = new ArrayList<>();
        result.setMsg(redPointNTCS);
        //int hasturn = turnTableWinLogDao.selectCount(new LambdaQueryWrapper<TurnTableWinLogBean>().eq(TurnTableWinLogBean::getWinUserId, userId).ge(TurnTableWinLogBean::getWinDate, DateUtil.today()));
        // 只能转一次
        int hasturn = turnTableWinLogDao.selectCount(new LambdaQueryWrapper<TurnTableWinLogBean>().eq(TurnTableWinLogBean::getWinUserId, userId));
        if (hasturn == 0) {
            Long rechangeTotal = platformRechargeLogDao.sumPlayerRecharge(userId);
            if (rechangeTotal!=null && rechangeTotal>=10000){
                redPointNTCS.add(TURN_TABLE_NTC);
            }
        }
        int hasEmail = userEmailDao.selectCount(new LambdaQueryWrapper<UserEmailBean>().eq(UserEmailBean::getUserId, userId).eq(UserEmailBean::getHasRead, false));
        if (hasEmail > 0) {
            redPointNTCS.add(MESSAGE_NTC);
        }
        boolean hasTask = userTaskProgressService.getHasFinishAndNoRecieveAwardTask(userId);
        if (hasTask) {
            redPointNTCS.add(TASK_NTC);
        }
        //判断今日是否领取
        SysShareLogBean sysShareLogBean1 = sysShareLogDao.coutShare(Long.parseLong(userid));
        if (sysShareLogBean1 == null) {
            redPointNTCS.add(SHARE_NTC);
        }
    }
}