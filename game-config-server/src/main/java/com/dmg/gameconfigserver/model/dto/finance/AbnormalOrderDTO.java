package com.dmg.gameconfigserver.model.dto.finance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/31 13:54
 * @Version V1.0
 **/
public class AbnormalOrderDTO {
    private Long id;
    private String orderId;
    private String  thirdOrderId;
    private BigDecimal rechargeAmount;
    private Date placeOrderDate;
    private Date payOrderDate;
    private int platformId;
    private BigDecimal payAmount;
    private int orderStatus;

}