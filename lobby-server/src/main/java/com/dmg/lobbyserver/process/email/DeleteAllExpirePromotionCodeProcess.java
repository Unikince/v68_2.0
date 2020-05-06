package com.dmg.lobbyserver.process.email;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.EmailDao;
import com.dmg.lobbyserver.dao.SysPromotionCodeDao;
import com.dmg.lobbyserver.dao.bean.EmailBean;
import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dmg.lobbyserver.config.MessageConfig.DELETE_ALL_EXPIRE_PROMOTIONCODE_EMAIL;

/**
 * @Description 删除所有已过期的优惠码
 * @Author mice
 * @Date 2019/6/20 11:04
 * @Version V1.0
 **/
@Service
public class DeleteAllExpirePromotionCodeProcess implements AbstractMessageHandler {
    @Autowired
    private EmailDao emailDao;
    @Autowired
    private SysPromotionCodeDao sysPromotionCodeDao;
    @Override
    public String getMessageId() {
        return DELETE_ALL_EXPIRE_PROMOTIONCODE_EMAIL;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        emailDao.delete(new LambdaQueryWrapper<EmailBean>()
                .eq(EmailBean::getUserId,Long.parseLong(userid))
                .eq(EmailBean::getEmailType,2)
                .lt(EmailBean::getExpireDate,new Date()));
        sysPromotionCodeDao.delete(new LambdaQueryWrapper<SysPromotionCodeBean>()
                .eq(SysPromotionCodeBean::getUserId,Long.parseLong(userid))
                .le(SysPromotionCodeBean::getExpireDate,new Date()));
    }
}