package com.dmg.gameconfigserver.service.financing;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.bean.finance.WithdrawOrderBean;
import com.dmg.gameconfigserver.model.dto.finance.QueryAbnormalOrderDTO;
import com.dmg.gameconfigserver.model.dto.finance.QueryWithdrawOrderDTO;
import com.dmg.gameconfigserver.model.dto.finance.ReviewWithdrawOrderDTO;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/30 14:09
 * @Version V1.0
 **/
public interface WithdrawOrderService {

    IPage<WithdrawOrderBean> queryWithdrawOrderPage(QueryWithdrawOrderDTO queryWithdrawOrderDTO);

    IPage<WithdrawOrderBean> queryReviewWithdrawOrderPage(QueryWithdrawOrderDTO queryWithdrawOrderDTO);

    ReviewWithdrawOrderDTO applyReviewOrder(int reviewerId,long id);

    String reviewOrder(int reviewerId,Long id,int reviewStatus,String reviewRemark);

    IPage<WithdrawOrderBean> queryAbnormalOrder(QueryAbnormalOrderDTO queryAbnormalOrderDTO);

    /**
     * @description: 设置已完成
     * @param id
     * @param dealUserId
     * @return String
     * @author mice
     * @date 2019/12/31
     */
    String setFinish(Long id,Long dealUserId);

    /**
     * @description: 重新拨款
     * @param id
     * @param dealUserId
     * @return void
     * @author mice
     * @date 2019/12/31
    */
    void appropriateAgain(Long id,Long dealUserId);

    /**
     * @description: 拒绝提现
     * @param id
     * @param dealUserId
     * @param remark
     * @return String
     * @author mice
     * @date 2019/12/31
    */
    String refuse(Long id,Long dealUserId,String remark);
    
    /**
     * @Author liubo
     * @Description //TODO 
     * @Date 15:48 2020/3/17
     **/
    BigDecimal countTodayWithdraw();
}