package com.dmg.lobbyserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.model.dto.pay.VnnPayReqDTO;
import com.dmg.lobbyserver.dao.VnnnRechargeLogDao;
import com.dmg.lobbyserver.dao.bean.VnnnRechargeLogBean;
import com.dmg.lobbyserver.service.VnnnRechargeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 18:00
 * @Version V1.0
 **/
@Service
public class VnnnRechargeLogServiceImpl implements VnnnRechargeLogService {
    @Autowired
    private VnnnRechargeLogDao vnnnRechargeLogDao;

    @Override
    public String idempotentValidate(String id) {
        VnnnRechargeLogBean vnnnRechargeLogBean = vnnnRechargeLogDao.selectById(id);
        if (vnnnRechargeLogBean == null){
            return null;
        }
        vnnnRechargeLogBean.setRequestTime(vnnnRechargeLogBean.getRequestTime()+1);
        vnnnRechargeLogDao.updateById(vnnnRechargeLogBean);
        return vnnnRechargeLogBean.getResponseBody();
    }

    public void save(VnnPayReqDTO reqDTO, String resp, int orderStatus){
        // 保存记录
        VnnnRechargeLogBean vnnnRechargeLogBean = VnnnRechargeLogBean.builder()
                .id(reqDTO.getId())
                .action(reqDTO.getAction())
                .appid(reqDTO.getAppId())
                .userCode(reqDTO.getTargetId())
                .sysMallConfigId(Long.parseLong(reqDTO.getGiftId()))
                .itemName(reqDTO.getGiftName())
                .itemPrice(reqDTO.getNum())
                .createDate(new Date())
                .requestBody(JSONObject.toJSONString(reqDTO))
                .responseBody(resp)
                .requestTime(1)
                .orderStatus(orderStatus)
                .build();

        vnnnRechargeLogDao.insert(vnnnRechargeLogBean);
    }

}