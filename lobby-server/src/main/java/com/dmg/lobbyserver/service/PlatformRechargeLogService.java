package com.dmg.lobbyserver.service;


import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 15:58
 * @Version V1.0
 **/
public interface PlatformRechargeLogService {

    /**
     * @param id
     * @return String
     * @description: 幂等验证
     * @author mice
     * @date 2019/11/26
     */
    String idempotentValidate(String id);

    PlatformRechargeLogBean save(PlatformRechargeLogBean platformRechargeLogBean);

    PlatformRechargeLogBean updateById(PlatformRechargeLogBean platformRechargeLogBean);

    PlatformRechargeLogBean getByOrderId(String orderId);

    boolean playerHasRecharge(Long userId);


}