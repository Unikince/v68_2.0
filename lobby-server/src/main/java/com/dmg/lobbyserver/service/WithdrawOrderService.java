package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.dao.bean.WithdrawOrderBean;
import com.dmg.lobbyserver.model.dto.WithdrawOrderDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/30 14:09
 * @Version V1.0
 **/
public interface WithdrawOrderService {

    void save(WithdrawOrderBean withdrawOrderBean);

    void update(WithdrawOrderBean withdrawOrderBean);

    WithdrawOrderBean getByOrderId(String orderId);

    List<WithdrawOrderDTO> getOrderLimit(Long userId,Integer limit);

    BigDecimal getWithdrawTotalToday(Long userId);

    Integer getWithdrawalTimeToday(Long userId);

}