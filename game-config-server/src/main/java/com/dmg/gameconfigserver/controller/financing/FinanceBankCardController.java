package com.dmg.gameconfigserver.controller.financing;

import com.dmg.gameconfigserver.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.finance.FinanceBankCardBean;
import com.dmg.gameconfigserver.model.vo.finance.FinanceBackCardPayMoneyRecv;
import com.dmg.gameconfigserver.model.vo.finance.FinanceBankCardListRecv;
import com.dmg.gameconfigserver.service.financing.FinanceBankCardService;

/**
 * 财务银行卡
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "finance/config/backCard")
public class FinanceBankCardController extends BaseController {
    @Autowired
    private FinanceBankCardService service;

    /** 新增 */
    @PostMapping("/saveOne")
    @SuppressWarnings("rawtypes")
    public Result saveOne(@RequestBody FinanceBankCardBean bean) {
        this.service.saveOne(bean);
        return Result.success();
    }

    /** 删除 */
    @DeleteMapping("/delete/{id}")
    @SuppressWarnings("rawtypes")
    public Result vipLeveDel(@PathVariable("id") Long id) {
        this.service.delete(id);
        return Result.success();
    }

    /** 修改 */
//    @PostMapping("/updateOne")
    @SuppressWarnings("rawtypes")
    public Result updateOne(@RequestBody FinanceBankCardBean bean) {
        this.service.updateOne(bean);
        return Result.success();
    }

    /** 获取 */
    @GetMapping("/get/{id}")
    @SuppressWarnings("rawtypes")
    public Result get(@PathVariable("id") Long id) {
        FinanceBankCardBean bean = this.service.get(id);
        return Result.success(bean);
    }

    /** 获取列表 */
    @PostMapping("/getList")
    @SuppressWarnings("rawtypes")
    public Result getList(@RequestBody FinanceBankCardListRecv reqVo) {
        IPage<FinanceBankCardBean> resVos = this.service.getList(reqVo);
        return Result.success(resVos);
    }

    /** 充值 */
    @PostMapping("/payMoney")
    @SuppressWarnings("rawtypes")
    public Result payMoney(@RequestBody FinanceBackCardPayMoneyRecv recv) {
        this.service.payMoney(recv.getId(), recv.getMoney());
        return Result.success();
    }

}
