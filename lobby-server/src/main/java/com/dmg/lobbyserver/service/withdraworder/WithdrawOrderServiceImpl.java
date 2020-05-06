package com.dmg.lobbyserver.service.withdraworder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.WithdrawOrderDao;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.WithdrawOrderBean;
import com.dmg.lobbyserver.model.dto.WithdrawOrderDTO;
import com.dmg.lobbyserver.service.WithdrawOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2020/1/10 14:17
 * @Version V1.0
 **/
@Service
public class WithdrawOrderServiceImpl implements WithdrawOrderService {

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;

    @Override
    public void save(WithdrawOrderBean withdrawOrderBean) {
        withdrawOrderDao.insert(withdrawOrderBean);
    }

    @Override
    public void update(WithdrawOrderBean withdrawOrderBean) {
        withdrawOrderDao.updateById(withdrawOrderBean);
    }

    @Override
    public WithdrawOrderBean getByOrderId(String orderId) {
        return withdrawOrderDao.selectOne(new LambdaQueryWrapper<WithdrawOrderBean>().eq(WithdrawOrderBean::getOrderId,orderId));
    }

    @Override
    public List<WithdrawOrderDTO> getOrderLimit(Long userId,Integer limit) {
        return withdrawOrderDao.selectOrder(userId,limit);
    }

    @Override
    public BigDecimal getWithdrawTotalToday(Long userId) {
        BigDecimal all = withdrawOrderDao.sumWithdrawTotalToday(userId);
        return all==null? BigDecimal.ZERO:all;
    }

    @Override
    public Integer getWithdrawalTimeToday(Long userId) {
        Integer withdrawalTimeToday = withdrawOrderDao.countWithdrawalTimeToday(userId);
        return withdrawalTimeToday==null? 0:withdrawalTimeToday;
    }
}