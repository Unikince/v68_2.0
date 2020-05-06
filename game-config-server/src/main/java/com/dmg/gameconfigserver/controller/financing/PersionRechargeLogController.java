package com.dmg.gameconfigserver.controller.financing;

import java.util.List;

import com.dmg.gameconfigserver.common.enums.PersionRechargeStatusEnum;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.finance.PersionRechargeLogBean;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditListRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditOneSend;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogAuditUpdateRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogCreateRecv;
import com.dmg.gameconfigserver.model.vo.finance.PersionRechargeLogOrderListRecv;
import com.dmg.gameconfigserver.service.financing.PersionRechargeLogService;

/**
 * 人工充值
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH)
public class PersionRechargeLogController extends BaseController {
    @Autowired
    private PersionRechargeLogService service;

    /** 创建订单 */
    @PostMapping("member/user/create-persion-recharge")
    @SuppressWarnings("rawtypes")
    public Result create(@RequestBody PersionRechargeLogCreateRecv vo) {
        Long loginId = Long.parseLong(this.getRequest().getAttribute(Constant.ATTRIBUTE_USER_ID_KEY).toString());
        this.service.create(vo, loginId);
        return Result.success();
    }

    /** 人工充值订单列表 */
    @PostMapping("finance/persion-recharge-order/list")
    @SuppressWarnings("rawtypes")
    public Result persionRechargeOrderList(@RequestBody PersionRechargeLogOrderListRecv vo) {
        IPage<PersionRechargeLogBean> resVos = this.service.persionRechargeOrderList(vo);
        return Result.success(resVos);
    }

    /** 充值审核人员列表 */
    @GetMapping("finance/persion-recharge-audit/userList")
    @SuppressWarnings("rawtypes")
    public Result persionRechargeAuditUserList() {
        List<String> resVos = this.service.persionRechargeAuditUserList();
        return Result.success(resVos);
    }

    /** 充值审核订单列表 */
    @PostMapping("finance/persion-recharge-audit/list")
    @SuppressWarnings("rawtypes")
    public Result persionRechargeAuditList(@RequestBody PersionRechargeLogAuditListRecv vo) {
        IPage<PersionRechargeLogBean> resVos = this.service.persionRechargeAuditList(vo);
        return Result.success(resVos);
    }

    /** 充值审核订单单个 */
    @GetMapping("finance/persion-recharge-audit/one/{id}")
    @SuppressWarnings("rawtypes")
    public Result persionRechargeAuditOne(@PathVariable("id") Long id) {
        PersionRechargeLogAuditOneSend resVo = this.service.persionRechargeAuditOne(id);
        return Result.success(resVo);
    }

    /** 充值审核订单更新 */
    @PostMapping("finance/persion-recharge-audit/update")
    @SuppressWarnings("rawtypes")
    public Result persionRechargeAuditUpdate(@RequestBody PersionRechargeLogAuditUpdateRecv vo) {
        if(vo != null && vo.getStatus() != null) {
            Boolean flag = true;
            for (PersionRechargeStatusEnum persionRechargeStatusEnum : PersionRechargeStatusEnum.values()) {
                if(vo.getStatus().intValue() == persionRechargeStatusEnum.getCode().intValue()){
                    flag = false;
                    break;
                }
            }
            if(flag){
                return Result.error(ResultEnum.PERSION_RECHARGE_STATUS_ERROR.getCode().toString(), ResultEnum.PERSION_RECHARGE_STATUS_ERROR.getMsg());
            }
        }
        this.service.persionRechargeAuditUpdate(vo, getUserId());
        return Result.success();
    }

    /** 自动清理订单 */
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void autoPersionRechargeAuditDeny() {
        this.service.autoPersionRechargeAuditDeny();
    }
}
