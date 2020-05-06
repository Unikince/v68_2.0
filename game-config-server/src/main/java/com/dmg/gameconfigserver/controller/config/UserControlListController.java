package com.dmg.gameconfigserver.controller.config;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.common.constant.UserControlConstant;
import com.dmg.gameconfigserver.common.enums.ResultEnum;
import com.dmg.gameconfigserver.controller.BaseController;
import com.dmg.gameconfigserver.dao.sys.SysUserDao;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.bean.sys.SysUserBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;
import com.dmg.gameconfigserver.model.vo.config.UserControlListVO;
import com.dmg.gameconfigserver.service.config.UserControlListService;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/27 10:14
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/userControlList")
public class UserControlListController extends BaseController{

    @Autowired
    private UserControlListService userControlListService;
    @Autowired
    private SysUserDao sysUserDao;

    @PostMapping("/getAll")
    public Result getUserControlConfigList(@RequestBody @Validated UserControlListVO vo){
        IPage<UserControlListDTO> userControlDTOList = userControlListService.getUserControlList(vo);
        return Result.success(userControlDTOList);
    }
    
    @PostMapping("/updateOne")
    public Result updateUserControlConfig(@RequestBody @Validated UserControlListVO vo){
    	// 控制分数不能为0
    	if (vo.getControlScore().compareTo(BigDecimal.ZERO) == 0) {
    		 return Result.error(BaseResultEnum.PARAM_ERROR);
		}
    	UserControlListBean userControlListBean = new UserControlListBean();
        BeanUtils.copyProperties(vo, userControlListBean);
        // 控制状态
        double controlScore = userControlListBean.getControlScore().doubleValue();
        if (controlScore > 0) {
        	// 点控赢
    		userControlListBean.setControlState(UserControlConstant.CONTROL_WIN1);
		} else if (controlScore < 0){
			// 点控输
			userControlListBean.setControlState(UserControlConstant.CONTROL_LOSE1);
		}
        // 控制时间
        Date operatingTime = new Date();
        userControlListBean.setOperatingTime(operatingTime); 
        // 控制人
        SysUserBean user = sysUserDao.selectById(getUserId());
    	userControlListBean.setOperator(user.getUserName());
        userControlListService.updateWithActionLog(userControlListBean,getIpAddr(),getUserId());
        return Result.success();
    }
}