package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenControlConfigBean;
import com.dmg.gameconfigserver.model.dto.config.BairenControlConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.bairen.BairenControlConfigVO;
import com.dmg.gameconfigserver.service.config.BairenControlConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/bairenControlConfig")
public class BairenControlConfigController {

    @Autowired
    private BairenControlConfigService bairenControlConfigService;

    @GetMapping("/getOne/{fileBaseConfigId}")
    public Result getBairenControlConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
        BairenControlConfigDTO bairenControlConfigDTO = bairenControlConfigService.getOne(fileBaseConfigId);
        return Result.success(bairenControlConfigDTO);
    }

    @PostMapping("/saveOne")
    public Result saveBairenControlConfig(@RequestBody @Validated BairenControlConfigVO vo){
        BairenControlConfigBean bairenControlConfigBean = new BairenControlConfigBean();
        BeanUtils.copyProperties(vo, bairenControlConfigBean);
        bairenControlConfigBean.setCardTypeMultiple(vo.getCardTypeMultiple().toJSONString());
        bairenControlConfigService.insert(bairenControlConfigBean);
        return Result.success("");
    }

    @PostMapping("/updateOne")
    public Result updateBairenControlConfig(@RequestBody @Validated BairenControlConfigVO vo){
        BairenControlConfigBean bairenControlConfigBean = new BairenControlConfigBean();
        BeanUtils.copyProperties(vo, bairenControlConfigBean);
        bairenControlConfigBean.setCardTypeMultiple(vo.getCardTypeMultiple().toJSONString());
        bairenControlConfigService.update(bairenControlConfigBean);
        return Result.success("");
    }

    @GetMapping("/delete/{fileBaseConfigId}")
    public Result delete(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
        bairenControlConfigService.delete(fileBaseConfigId);
        return Result.success("");
    }
}