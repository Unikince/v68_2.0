package com.dmg.lobbyserver.process.email;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.server.common.enums.ItemTypeEnum;
import com.dmg.lobbyserver.dao.EmailDao;
import com.dmg.lobbyserver.dao.SysPromotionCodeDao;
import com.dmg.lobbyserver.dao.UserRechargeLogDao;
import com.dmg.lobbyserver.dao.bean.EmailBean;
import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.model.constants.RecievePromotionCodeConditionType;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.model.vo.RecievePromotionCodeVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.GiftDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.RECIEVE_PROMOTIONCODE;
import static com.dmg.lobbyserver.result.ResultEnum.*;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 14:30
 * @Version V1.0
 **/
@Service
@Transactional
public class RecievePromotionCodeProcess implements AbstractMessageHandler {
    @Autowired
    private EmailDao emailDao;
    @Autowired
    private SysPromotionCodeDao sysPromotionCodeDao;
    @Autowired
    private UserRechargeLogDao userRechargeLogDao;
    @Autowired
    private GiftDataService giftDataService;

    @Override
    public String getMessageId() {
        return RECIEVE_PROMOTIONCODE;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        RecievePromotionCodeVO vo = params.toJavaObject(RecievePromotionCodeVO.class);
        EmailBean emailBean = emailDao.selectOne(new LambdaQueryWrapper<EmailBean>()
                .eq(EmailBean::getUserId,vo.getUserId())
                .eq(EmailBean::getPromotionCode,vo.getPromotionCode()));
        if (emailBean == null){
            result.setRes(PROMOTION_CODE_BO_EXIT.getCode());
            return;
        }
        if (emailBean.getReceive()==1){
            result.setRes(CANT_RECIEVE_AGAIN.getCode());
            return;
        }
        if (emailBean.getExpireDate().getTime()<new Date().getTime()){
            result.setRes(PROMOTION_CODE_HAS_EXPIRE.getCode());
            return;
        }
        SysPromotionCodeBean sysPromotionCodeBean = sysPromotionCodeDao.selectOne(new LambdaQueryWrapper<SysPromotionCodeBean>().eq(SysPromotionCodeBean::getPromotionCode,vo.getPromotionCode()));
        int receiveConditionType = sysPromotionCodeBean.getReceiveConditionType();
        // 按领取类型处理
        if (receiveConditionType==RecievePromotionCodeConditionType.NEW_DEPOSIT.getKey()){
            Integer rechargeNumber = userRechargeLogDao.countRechargeNumberFromDate(vo.getUserId(),emailBean.getSendDate());
            if (rechargeNumber==null || rechargeNumber < sysPromotionCodeBean.getReceiveConditionNumber()){
                result.setRes(RECIEVE_PROMOTION_CODE_CONDITION_ERROR.getCode());
                return;
            }
            emailBean.setReceive(1);
            sysPromotionCodeBean.setUserId(vo.getUserId());
            sysPromotionCodeBean.setHasReceive(1);
            emailDao.updateById(emailBean);
            sysPromotionCodeDao.updateById(sysPromotionCodeBean);
            // 优惠码展示信息
            List<GiftDataDTO> giftDataDTOS = new ArrayList<>();
            GiftDataDTO giftDataDTO = new GiftDataDTO();
            giftDataDTO.setItemNumber(String.valueOf(sysPromotionCodeBean.getDepositRebate()));
            giftDataDTO.setItemName(ItemTypeEnum.PROMO_CODE.getMsg());
            giftDataDTO.setRemark(ItemTypeEnum.PROMO_CODE.getMsg());
            giftDataDTO.setSmallPicId("4");
            giftDataDTOS.add(giftDataDTO);
            giftDataService.sendGiftData(Long.parseLong(userid),giftDataDTOS);
        }

    }
}