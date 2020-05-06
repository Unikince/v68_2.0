package com.dmg.lobbyserver.process.activity.sign;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SignConfigDao;
import com.dmg.lobbyserver.dao.SignLogDao;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.bean.SignConfigBean;
import com.dmg.lobbyserver.dao.bean.SignLogBean;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.model.dto.SignDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static com.dmg.lobbyserver.config.MessageConfig.SIGN_LIST;

/**
 * 签到列表
 * Author:刘将军
 * Time:2019/6/19 18:46
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class SignListProcess implements AbstractMessageHandler {

    @Autowired
    private SignConfigDao signConfigDao;
    @Autowired
    private SysItemConfigDao sysItemConfigDao;
    @Autowired
    private SignLogDao signLogDao;

    @Override
    public String getMessageId() {
        return SIGN_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        //签到配置
        List<SignConfigBean> signConfigBeanList = signConfigDao.findAll();
        //用户签到记录
        List<SignLogBean> beanList = signLogDao.findAllByUserId(Long.valueOf(userid));
        List<SignDataDTO> signDataDTOList = new LinkedList<>();
        for (SignConfigBean signConfigBean : signConfigBeanList) {
            SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectById(signConfigBean.getItemId());
            SignDataDTO signDataDTO = new SignDataDTO();
            signDataDTO.setImg(sysItemConfigBean.getSmallPicId());
            signDataDTO.setItemName(sysItemConfigBean.getItemName());
            signDataDTO.setNum(signConfigBean.getItemNumber());
            signDataDTO.setOrderNum(signConfigBean.getSignDay());
            signDataDTO.setSigned(false);
            for (SignLogBean signLogBean : beanList) {
                if (signLogBean.getSignDay().equals(signConfigBean.getSignDay())){
                    signDataDTO.setSigned(true);
                }
            }
            signDataDTOList.add(signDataDTO);
        }
        Map<String,Object> resultMap = new HashMap<>();
        int todaySign = signLogDao.selectCount(new LambdaQueryWrapper<SignLogBean>().eq(SignLogBean::getUserId,userid).eq(SignLogBean::getSignDate, DateUtil.today()));
        int signCount = signLogDao.selectCount(new LambdaQueryWrapper<SignLogBean>().eq(SignLogBean::getUserId,userid));
        if (todaySign>0){
            resultMap.put("todaySign",signCount);
        }else {
            resultMap.put("todaySign",signCount+1);
        }


        resultMap.put("signDataDTOList",signDataDTOList);
        result.setMsg(resultMap);
    }


}
