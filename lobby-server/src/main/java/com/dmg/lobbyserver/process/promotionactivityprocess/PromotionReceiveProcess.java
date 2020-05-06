package com.dmg.lobbyserver.process.promotionactivityprocess;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.SysPromotionCodeDao;
import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.PROMOTION_RECEIVE;
/**
 * @Description  优惠活动立即领取
 * @Author jock
 * @Date 2019/7/4 0004
 * @Version V1.0
 **/
@Service
@Slf4j
public class PromotionReceiveProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return PROMOTION_RECEIVE;
    }
    @Autowired
    UserService  userService;
    @Autowired
    SysPromotionCodeDao sysPromotionCodeDao ;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserBean userById = userService.getUserById(Long.parseLong(userid));
       if(StringUtils.isEmpty(userById.getRealName())||StringUtils.isEmpty(userById.getPhone())){
           result.setRes(ResultEnum.PROMOTION_RECEIVE_MESSAGE_ISNULL.getCode());
           return;
       }
        SysPromotionCodeBean  sysPromotionCodeBean =new SysPromotionCodeBean() ;
        sysPromotionCodeBean.setUserId(Long.parseLong(userid));
        sysPromotionCodeBean.setPromotionType(2);
        sysPromotionCodeBean.setPromotionCode("YHHD--22");
        sysPromotionCodeBean.setHasReceive(1);
        sysPromotionCodeDao.insert(sysPromotionCodeBean);
        result.setRes(ResultEnum.SUCCESS.getCode());

    }
}
