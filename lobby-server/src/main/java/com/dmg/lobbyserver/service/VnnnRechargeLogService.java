package com.dmg.lobbyserver.service;


import com.dmg.lobbyserver.model.dto.pay.VnnPayReqDTO;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 15:58
 * @Version V1.0
 **/
public interface VnnnRechargeLogService {

    /**
     * @param id
     * @return String
     * @description: 幂等验证
     * @author mice
     * @date 2019/11/26
     */
    String idempotentValidate(String id);

    void save(VnnPayReqDTO reqDTO, String resp, int orderStatus);

}