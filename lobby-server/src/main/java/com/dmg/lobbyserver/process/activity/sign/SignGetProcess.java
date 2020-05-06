package com.dmg.lobbyserver.process.activity.sign;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SignConfigDao;
import com.dmg.lobbyserver.dao.SignLogDao;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.SignConfigBean;
import com.dmg.lobbyserver.dao.bean.SignLogBean;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.process.activity.UserProessService;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.SIGN_GET;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_INFO_NTC;

/**
 * 绑定手机送礼
 * Author:刘将军
 * Time:2019/6/19 18:46
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class SignGetProcess implements AbstractMessageHandler {

    @Autowired
    private UserDao userDao;
    @Autowired
    private SignLogDao signLogDao;
    @Autowired
    private SignConfigDao signConfigDao;
    @Autowired
    private SysItemConfigDao sysItemConfigDao;
    @Autowired
    private UserProessService userProessService;
    @Autowired
    private GiftDataService giftDataService;

    @Override
    public String getMessageId() {
        return SIGN_GET;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        int todaySign = signLogDao.selectCount(new LambdaQueryWrapper<SignLogBean>().eq(SignLogBean::getUserId,userid).eq(SignLogBean::getSignDate, DateUtil.today()));
        if (todaySign > 0) {
            result.setRes(ResultEnum.SIGN_REPEAT.getCode());
            result.setMsg(ResultEnum.SIGN_REPEAT.getMsg());
            return;
        }
        int signCount = signLogDao.selectCount(new LambdaQueryWrapper<SignLogBean>().eq(SignLogBean::getUserId,userid));
        // 保存签到记录
        SignLogBean signLogBean = new SignLogBean();
        signLogBean.setSignDay(signCount+1);
        signLogBean.setSignDate(new Date());
        signLogBean.setUserId(Long.valueOf(userid));
        signLogDao.insert(signLogBean);

        SignConfigBean signConfigBean = signConfigDao.findByDay(signCount+1);
        SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectById(signConfigBean.getItemId());
        userProessService.getItems(sysItemConfigBean.getItemType(), signConfigBean.getItemNumber(), userid);
        GiftDataDTO giftDataDTO = new GiftDataDTO();
        BeanUtils.copyProperties(sysItemConfigBean,giftDataDTO);
        giftDataDTO.setItemNumber(String.valueOf(signConfigBean.getItemNumber()));
        List<GiftDataDTO> giftDataDTOList = new ArrayList<>();
        giftDataDTOList.add(giftDataDTO);
        giftDataService.sendGiftData(Long.parseLong(userid),giftDataDTOList);
    }
}
