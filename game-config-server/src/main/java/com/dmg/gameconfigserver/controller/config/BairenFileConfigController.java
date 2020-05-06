package com.dmg.gameconfigserver.controller.config;

import com.dmg.common.core.web.BusinessException;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.FileBaseConfigBean;
import com.dmg.gameconfigserver.model.bean.config.bairen.BairenFileConfigBean;
import com.dmg.gameconfigserver.model.dto.config.BairenFileConfigDTO;
import com.dmg.gameconfigserver.model.vo.config.bairen.BairenFileConfigVO;
import com.dmg.gameconfigserver.service.config.BairenFileConfigService;
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
@RequestMapping(Constant.CONTEXT_PATH + "game/config/bairenFileConfig")
public class BairenFileConfigController {

    @Autowired
    private BairenFileConfigService bairenFileConfigService;

    @GetMapping("/getOne/{fileBaseConfigId}")
    public Result getBairenControlConfig(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
        BairenFileConfigDTO bairenFileConfigDTO = bairenFileConfigService.getOne(fileBaseConfigId);
        return Result.success(bairenFileConfigDTO);
    }

    @PostMapping("/saveOne")
    public Result saveBairenControlConfig(@RequestBody @Validated BairenFileConfigVO vo){
        FileBaseConfigBean fileBaseConfigBean = new FileBaseConfigBean();
        BeanUtils.copyProperties(vo, fileBaseConfigBean);
        BairenFileConfigBean bairenFileConfigBean = new BairenFileConfigBean();
        BeanUtils.copyProperties(vo, bairenFileConfigBean);
        bairenFileConfigService.insert(fileBaseConfigBean,bairenFileConfigBean);
        return Result.success("");
    }

    @PostMapping("/updateOne")
    public Result updateBairenControlConfig(@RequestBody @Validated BairenFileConfigVO vo){
        if (vo.getFileBaseConfigId()==null){
            throw new BusinessException(ResultEnum.PARAM_ERROR+"","fileBaseConfigId不能为空");
        }
        FileBaseConfigBean fileBaseConfigBean = new FileBaseConfigBean();
        BeanUtils.copyProperties(vo, fileBaseConfigBean);
        BairenFileConfigBean bairenFileConfigBean = new BairenFileConfigBean();
        BeanUtils.copyProperties(vo, bairenFileConfigBean);
        bairenFileConfigService.update(fileBaseConfigBean,bairenFileConfigBean);
        return Result.success();
    }

    @GetMapping("/delete/{fileBaseConfigId}")
    public Result delete(@PathVariable("fileBaseConfigId") Integer fileBaseConfigId){
        bairenFileConfigService.delete(fileBaseConfigId);
        return Result.success();
    }
}