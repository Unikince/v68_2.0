package com.dmg.gameconfigserver.controller.financing;

import java.util.List;

import com.dmg.gameconfigserver.common.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.finance.PayChannelBean;
import com.dmg.gameconfigserver.model.vo.finance.PayChannelListRecv;
import com.dmg.gameconfigserver.service.financing.PayChannelService;

/**
 * 支付渠道
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "finance/pay-channel")
public class PayChannelController extends BaseController {
    @Autowired
    private PayChannelService service;

    /** 新增 */
//    @PostMapping("/saveOne")
    @SuppressWarnings("rawtypes")
    public Result saveOne(@RequestBody PayChannelBean bean) {
        this.service.saveOne(bean);
        return Result.success();
    }

    /** 删除 */
//    @DeleteMapping("/delete/{id}")
    @SuppressWarnings("rawtypes")
    public Result vipLeveDel(@PathVariable("id") Long id) {
        this.service.delete(id);
        return Result.success();
    }

    /** 修改 */
    @PostMapping("/updateOne")
    @SuppressWarnings("rawtypes")
    public Result updateOne(@RequestBody PayChannelBean bean) {
        this.service.updateOne(bean);
        return Result.success();
    }

    /** 获取 */
//    @GetMapping("/get/{id}")
    @SuppressWarnings("rawtypes")
    public Result get(@PathVariable("id") Long id) {
        PayChannelBean bean = this.service.get(id);
        return Result.success(bean);
    }

    /** 获取列表 */
    @PostMapping("/getList")
    @SuppressWarnings("rawtypes")
    public Result getList(@RequestBody PayChannelListRecv reqVo) {
        IPage<PayChannelBean> resVos = this.service.getList(reqVo);
        return Result.success(resVos);
    }

    /** 所有渠道，用于下拉框选择 */
    @GetMapping("/channelGroup")
    @SuppressWarnings("rawtypes")
    @NoAuthMapping
    public Result allChannel() {
        List<String> resVos = this.service.channelGroup();
        return Result.success(resVos);
    }

}
