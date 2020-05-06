package com.dmg.gameconfigserver.controller.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.gameconfigserver.annotation.NoNeedLoginMapping;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.model.bean.config.UserControlListBean;
import com.dmg.gameconfigserver.model.dto.config.UserControlListDTO;
import com.dmg.gameconfigserver.service.api.UserControlApiService;
import com.dmg.server.common.model.dto.UserControlInfoDTO;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/10 10:27
 * @Version V1.0
 **/
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "game/config/userControl")
public class UserControlApiController {
    @Autowired
    private UserControlApiService userControlApiService;
    
    /**
     * 获取点控对象信息
     * @param userId
     * @return
     */
    @NoNeedLoginMapping
    @GetMapping("/getUserControlInfo/{userId}")
    public UserControlListDTO getUserControlInfo(@PathVariable("userId") Long userId){
    	UserControlListDTO userControlListDTO = userControlApiService.getUserControlInfo(userId);
		return userControlListDTO;
    }
    
    /**
     * 获取点控状态模型
     * @param userId
     * @return
     */
    @NoNeedLoginMapping
    @GetMapping("/getPointControlModel/{userId}")
    public int getPointControlModel(@PathVariable("userId") Long userId){
    	int model = 0;
    	UserControlListDTO userControlListDTO = userControlApiService.getUserControlInfo(userId);
    	if (userControlListDTO != null) {
    		model = userControlListDTO.getControlModel();
		}
		return model;
    }
    
    /**
     * 获取模型信息(没有被点控,就查询自控)
     * @param userId
     * @return
     */
    @NoNeedLoginMapping
    @PostMapping("/getModel")
    public Map<Long, UserControlInfoDTO> getModel(@RequestBody List<Long> userIds){
		return userControlApiService.getModlel(userIds);
    }
    
    /**
     * 更新点控状态的当前分数
     * @param vo
     */
    @NoNeedLoginMapping
    @PostMapping("/updateCurrentScore")
    public void updateCurrentScore(@RequestParam("userId") Long userId, @RequestParam("score") BigDecimal score){
    	UserControlListDTO userControlListDTO = userControlApiService.getUserControlInfo(userId);
    	userControlListDTO.setCurrentScore(userControlListDTO.getCurrentScore().add(score));
    	UserControlListBean userControlListBean = new UserControlListBean();
    	BeanUtils.copyProperties(userControlListDTO, userControlListBean);
    	userControlApiService.updateCurrentScore(userControlListBean);
    }
    
    /**
     * 获取自控状态模型
     * @param userId
     * @return
     */
    @NoNeedLoginMapping
    @GetMapping("/getAutoControlModel/{userId}")
    public int getAutoControlModel(@PathVariable("userId") Long userId){
    	return userControlApiService.getAutoControlModel(userId);
    }
    
}