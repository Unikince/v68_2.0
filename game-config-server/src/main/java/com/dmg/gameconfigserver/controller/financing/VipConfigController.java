package com.dmg.gameconfigserver.controller.financing;

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
import com.dmg.gameconfigserver.annotation.NoAuthMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.model.bean.finance.VipConfigBean;
import com.dmg.gameconfigserver.model.vo.finance.VipListRecv;
import com.dmg.gameconfigserver.service.financing.VipConfigService;

/**
 * VIP配置
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "finance/vip-config")
public class VipConfigController extends BaseController {
    @Autowired
    private VipConfigService service;

    /** 新增 */
    @PostMapping("/saveOne")
    @SuppressWarnings("rawtypes")
    public Result saveOne(@RequestBody VipConfigBean bean) {
        Long loginId = Long.parseLong(this.getRequest().getAttribute(Constant.ATTRIBUTE_USER_ID_KEY).toString());
        this.service.saveOne(bean, loginId);
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
    @PostMapping("/updateOne")
    @SuppressWarnings("rawtypes")
    public Result updateOne(@RequestBody VipConfigBean bean) {
        Long loginId = Long.parseLong(this.getRequest().getAttribute(Constant.ATTRIBUTE_USER_ID_KEY).toString());
        this.service.updateOne(bean, loginId);
        return Result.success();
    }

    /** 获取 */
//    @GetMapping("/get/{id}")
    @SuppressWarnings("rawtypes")
    public Result get(@PathVariable("id") Long id) {
        VipConfigBean bean = this.service.get(id);
        return Result.success(bean);
    }

    /** 获取列表 */
    @PostMapping("/getList")
    @SuppressWarnings("rawtypes")
    public Result getList(@RequestBody VipListRecv reqVo) {
        IPage<VipConfigBean> resVos = this.service.getList(reqVo);
        return Result.success(resVos);
    }

    /** 所有渠道，用于下拉框选择 */
    @GetMapping("/vipSum")
    @SuppressWarnings("rawtypes")
    @NoAuthMapping
    public Result vipSum() {
        int num = this.service.vipSum();
        return Result.success(num);
    }

}
