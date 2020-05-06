package com.dmg.lobbyserver.controller.pay;

import com.dmg.common.core.util.OrderIdUtil;
import com.dmg.lobbyserver.common.constants.CommonConstants;
import com.dmg.lobbyserver.controller.pay.jqpay.JQPayErrorEnum;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.SysMallConfigBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.model.vo.CreateOrderVO;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.service.PlatformRechargeLogService;
import com.dmg.lobbyserver.service.SysMallConfigService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.server.common.enums.PayPlatformEnum;
import com.dmg.server.common.enums.PlatformRechargeStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/24 14:23
 * @Version V1.0
 **/
@RestController
@RequestMapping("platformRecharge")
@Slf4j
public class PlatformRechargeLogController {
    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;
    @Autowired
    private SysMallConfigService sysMallConfigService;
    @Autowired
    private UserService userService;

    @RequestMapping("createOrder")
    public Result createOrder(@RequestBody CreateOrderVO vo){
        if (vo.getItemId() != CommonConstants.SPECIAL_GOODS_ID){
            SysMallConfigBean sysMallConfigBean = sysMallConfigService.getSysMallConfigById(vo.getItemMallId());
            if (sysMallConfigBean == null) {
                // 商品不存在
                log.error("商品不存在,商品id:{}", vo.getItemMallId());
                throw new BusinessException(JQPayErrorEnum.ITEM_NO_EXIT.getCode(),JQPayErrorEnum.ITEM_NO_EXIT.getMsg());
            }
            if (vo.getOriginalPrice().compareTo(sysMallConfigBean.getOriginalPrice())!=0) {
                // 价格不匹配
                log.error("商品价格不匹配,商品id:{},请求价格为:reqDTO.getNum(),实际价格为:{}", vo.getItemMallId(),
                        vo.getOriginalPrice(), sysMallConfigBean.getOriginalPrice().doubleValue());
                throw new BusinessException(JQPayErrorEnum.PRICE_NO_MATCH.getCode(),JQPayErrorEnum.PRICE_NO_MATCH.getMsg());
            }
        }


        // 用户验证
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (userBean == null) {
            // 用户不存在
            log.error("购买用户不存在,用户id:{}", vo.getUserId());
            throw new BusinessException(JQPayErrorEnum.USER_NO_EXIT.getCode(),JQPayErrorEnum.USER_NO_EXIT.getMsg());
        }
        PlatformRechargeLogBean platformRechargeLogBean = PlatformRechargeLogBean.builder()
                .orderId(OrderIdUtil.platformOrderId(PayPlatformEnum.getCode(vo.getPlatformId())))
                .userId(vo.getUserId())
                .nickName(userBean.getUserName())
                .sysMallConfigId(vo.getItemMallId())
                .platformId(PayPlatformEnum.getCode(vo.getPlatformId()))
                .payAmount(vo.getOriginalPrice())
                .rechargeAmount(vo.getItemNum())
                .createDate(new Date())
                .orderStatus(PlatformRechargeStatusEnum.WAITE_PAY.getCode())
                .build();
        platformRechargeLogService.save(platformRechargeLogBean);
        return Result.success(platformRechargeLogBean.getOrderId());
    }

}