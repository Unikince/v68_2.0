package com.dmg.lobbyserver.process.email;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.agentserviceapi.business.transferaccountrecord.feign.TransferAccountRecordFeign;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.server.common.enums.ItemTypeEnum;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.UserEmailDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.UserEmailBean;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.model.vo.SyncUserGoldVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static com.dmg.lobbyserver.config.MessageConfig.RECIEVE_ITEM;
import static com.dmg.lobbyserver.result.ResultEnum.*;

/**
 * @Author liubo
 * @Description 领取邮件附件//TODO
 * @Date 15:49 2020/3/23
 **/
@Slf4j
@Service
@Transactional
public class ReceiveEmailProcess implements AbstractMessageHandler {

    @Autowired
    private UserEmailDao userEmailDao;

    @Autowired
    private GiftDataService giftDataService;

    @Autowired
    private AbstractMessageHandler syncUserGoldProcess;

    @Autowired
    private SysItemConfigDao sysItemConfigDao;

    @Autowired
    private TransferAccountRecordFeign transferAccountRecordService;

    @Override
    public String getMessageId() {
        return RECIEVE_ITEM;
    }

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {
        Long emailId = params.getLong("emailId");
        if (emailId == null || emailId < 1) {
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        List<UserEmailBean> userEmailBeanList = userEmailDao.selectList(new LambdaQueryWrapper<UserEmailBean>()
                .ge(UserEmailBean::getExpireDate, new Date())
                .le(UserEmailBean::getSendDate, new Date())
                .eq(UserEmailBean::getId, emailId)
                .eq(UserEmailBean::getUserId, Long.parseLong(userId)));
        if (CollectionUtil.isEmpty(userEmailBeanList)) {
            result.setRes(EMAIL_BO_EXIT.getCode());
            return;
        }
        UserEmailBean userEmailBean = userEmailBeanList.get(0);
        if (userEmailBean.getReceive()) {
            result.setRes(CANT_RECIEVE_AGAIN.getCode());
            return;
        }
        if (userEmailBean.getTransferAccountId() != null) {
            //完成转账
            IdReq idReq = new IdReq();
            idReq.setId(userEmailBean.getTransferAccountId());
            if (!transferAccountRecordService.finish(idReq).isSuccess()) {
                result.setRes(EMAIL_FINISH.getCode());
                return;
            }
        }
        userEmailBean.setReceive(true);
        userEmailDao.updateById(userEmailBean);
        List<GiftDataDTO> giftDataDTOS = new ArrayList<>();
        // 1:金币 2:积分
        if (userEmailBean.getItemType() == ItemTypeEnum.CODE_GOLD.getCode()) {
            SyncUserGoldVO syncUserGoldVO = new SyncUserGoldVO();
            syncUserGoldVO.setGold(new BigDecimal(userEmailBean.getItemNum()));
            syncUserGoldVO.setUserId(Long.parseLong(userId));
            syncUserGoldVO.setType(AccountChangeTypeEnum.CODE_EMAIL.getCode());
            syncUserGoldProcess.messageHandler(userId, (JSONObject) JSONObject.toJSON(syncUserGoldVO), null);
            SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectOne(new LambdaQueryWrapper<SysItemConfigBean>().
                    eq(SysItemConfigBean::getId, userEmailBean.getItemType()));
            log.info("用户:{}领取礼品:{}:{}", userId, sysItemConfigBean.getId(), sysItemConfigBean.getItemName());
            GiftDataDTO giftDataDTO = new GiftDataDTO();
            giftDataDTO.setItemNumber(userEmailBean.getItemNum());
            giftDataDTO.setItemName(sysItemConfigBean.getItemName());
            giftDataDTO.setRemark(sysItemConfigBean.getRemark());
            giftDataDTO.setSmallPicId(sysItemConfigBean.getSmallPicId());
            giftDataDTOS.add(giftDataDTO);
        }
        giftDataService.sendGiftData(Long.parseLong(userId), giftDataDTOS);
        Map<String, Object> data = new HashMap<>();
        data.put("receive", true);
        data.put("isTips", false);
        result.setMsg(data);
    }
}