package com.dmg.lobbyserver.process.email;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.EmailDao;
import com.dmg.lobbyserver.dao.SysPromotionCodeDao;
import com.dmg.lobbyserver.dao.bean.EmailBean;
import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dmg.lobbyserver.config.MessageConfig.DELETE_EXPIRE_PROMOTIONCODE_EMAIL;

/**
 * @Description 删除已过期的优惠码
 * @Author mice
 * @Date 2019/6/20 11:04
 * @Version V1.0
 **/
@Service
public class DeleteExpirePromotionCodeProcess implements AbstractMessageHandler {
    @Autowired
    private EmailDao emailDao;
    @Autowired
    private SysPromotionCodeDao sysPromotionCodeDao;
    @Override
    public String getMessageId() {
        return DELETE_EXPIRE_PROMOTIONCODE_EMAIL;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Long emailId = params.getLong("emailId");
        if(emailId==null || emailId <1){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        EmailBean emailBean = emailDao.selectById(emailId);
        if (emailBean == null){
            return;
        }
        emailDao.deleteById(emailId);
        sysPromotionCodeDao.delete(new LambdaQueryWrapper<SysPromotionCodeBean>()
                .eq(SysPromotionCodeBean::getPromotionCode,emailBean.getPromotionCode()));
    }
}