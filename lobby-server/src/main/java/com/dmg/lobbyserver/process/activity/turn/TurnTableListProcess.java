package com.dmg.lobbyserver.process.activity.turn;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.TurnTableConfigDao;
import com.dmg.lobbyserver.dao.TurnTableWinLogDao;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.TurnTableConfigBean;
import com.dmg.lobbyserver.model.dto.TurnTableDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.TURN_TABLE_LIST;

/**
 * 转盘奖品列表
 * Author:刘将军
 * Time:2019/6/19 14:52
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class TurnTableListProcess implements AbstractMessageHandler {

    @Autowired
    private TurnTableConfigDao turnTableConfigDao;
    @Autowired
    private SysItemConfigDao sysItemConfigDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TurnTableWinLogDao turnTableWinLogDao;


    @Override
    public String getMessageId() {
        return TURN_TABLE_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<TurnTableConfigBean> turnTableConfigBeans = turnTableConfigDao.selectList(null);
        List<TurnTableDataDTO> turnTableDataDTOList = new LinkedList<>();
        for (TurnTableConfigBean turnTableConfigBean : turnTableConfigBeans) {
            SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectById(turnTableConfigBean.getItemId());
            TurnTableDataDTO turnTableDataDTO = new TurnTableDataDTO();
            BeanUtils.copyProperties(sysItemConfigBean, turnTableDataDTO);
            BeanUtils.copyProperties(turnTableConfigBean, turnTableDataDTO);
            turnTableDataDTOList.add(turnTableDataDTO);
        }
        result.setMsg(turnTableDataDTOList);
    }
}
