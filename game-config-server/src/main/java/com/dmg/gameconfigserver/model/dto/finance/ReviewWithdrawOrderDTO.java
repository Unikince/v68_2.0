package com.dmg.gameconfigserver.model.dto.finance;

import com.dmg.gameconfigserver.model.vo.user.UserDetailVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/30 20:33
 * @Version V1.0
 **/
@Data
public class ReviewWithdrawOrderDTO {

    private UserDetailVO userInfo;
    private WithdrawOrderOrderInfo orderInfo;

    @Data
    public static class WithdrawOrderOrderInfo{
        private Long id;
        /**
         * 申请时间
         */
        private Date applyDate;
        /**
         * 提现金额
         */
        private BigDecimal withdrawAmount;
        /**
         * 服务费
         */
        private BigDecimal serviceCharges;
        /**
         * 到账金额
         */
        private BigDecimal account;
    }
}
