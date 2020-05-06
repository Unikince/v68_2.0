package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.BusinessException;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenWaterPoolConfigBean;
import com.dmg.gameconfigserver.model.vo.config.bairen.BairenWaterPoolConfigVO;
import com.dmg.gameconfigserver.service.config.BairenWaterPoolConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/bairenWaterPoolConfig")
public class BairenWaterPoolConfigController {

    @Autowired
    private BairenWaterPoolConfigService bairenWaterPoolConfigService;


    @GetMapping("/getList/{fileBaseConfigId}")
    public Result getBairenGameConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
        List<BairenWaterPoolConfigBean> bairenWaterPoolConfigBeans = bairenWaterPoolConfigService.getList(fileBaseConfigId);
        return Result.success(bairenWaterPoolConfigBeans);
    }

    @PostMapping("/saveOne")
    public Result saveBairenWaterPoolConfig(@RequestBody @Validated BairenWaterPoolConfigVO vo){
        BairenWaterPoolConfigBean bairenWaterPoolConfigBean = new BairenWaterPoolConfigBean();
        BeanUtils.copyProperties(vo,bairenWaterPoolConfigBean);
        bairenWaterPoolConfigService.insert(bairenWaterPoolConfigBean);
        return Result.success("");
    }

    @PostMapping("/updateOne")
    public Result updateBairenWaterPoolConfig(@RequestBody @Validated BairenWaterPoolConfigVO vo){
        if (vo.getId()==null){
            throw new BusinessException(ResultEnum.PARAM_ERROR+"","id不能为空");
        }
        BairenWaterPoolConfigBean bairenWaterPoolConfigBean = new BairenWaterPoolConfigBean();
        BeanUtils.copyProperties(vo,bairenWaterPoolConfigBean);
        bairenWaterPoolConfigService.update(bairenWaterPoolConfigBean);
        return Result.success("");
    }

    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id){
        bairenWaterPoolConfigService.delete(id);
        return Result.success("");
    }
}