package com.dmg.lobbyserver.controller.v68game;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.HttpLoginDTO;
import com.dmg.lobbyserver.model.vo.LoginV68GameVO;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.impl.GiftDataServiceImpl;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.dmg.lobbyserver.config.MessageConfig.GIFT_DATA;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_INFO_NTC;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/26 13:59
 * @Version V1.0
 **/
@RestController
public class LoginGameServerController {
    @Autowired
    private UserService userService;
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        LoginGameServerController.locationManager = locationManager;
    }

    @Value("${gameserver.niuniu.host}")
    private String gameServerHost;
    @Value("${gameserver.niuniu.http_port}")
    private String gameServerHttpPort;


    /**
     * @param userId
     * @return com.dmg.lobbyserver.result.Result
     * @description: 登录炸金花大厅
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/loginZjh")
    public Result loginZjh(Long userId) {
        UserBean userBean = userService.getUserById(userId);
        LoginV68GameVO vo = new LoginV68GameVO();
        BeanUtils.copyProperties(userBean, vo);
        String voStr = JSONObject.toJSONString(vo);
        voStr = voStr.replace("{", "%7B");
        voStr = voStr.replace("}", "%7D");
        voStr = voStr.replace("\"", "%22");
        String result = HttpUtil.get(gameServerHost + ":" + gameServerHttpPort + "/v68login?userInfo=" + voStr);
        return Result.success(JSON.parseObject(result).getJSONObject("data"));
    }

    /**
     * @param userId
     * @return com.dmg.lobbyserver.result.Result
     * @description: 登录牛牛大厅
     * @author mice
     * @date 2019/6/18
     */
    @PostMapping("/loginNN")
    public Result loginNN(Long userId) {
        UserBean userBean = userService.getUserById(userId);
        LoginV68GameVO vo = new LoginV68GameVO();
        BeanUtils.copyProperties(userBean, vo);
        String voStr = JSONObject.toJSONString(vo);
        voStr = voStr.replace("{", "%7B");
        voStr = voStr.replace("}", "%7D");
        String result = HttpUtil.get(gameServerHost + ":" + gameServerHttpPort + "/v68login?userInfo=" + voStr);
        return Result.success(result);
    }

    /**
     * @param userId
     * @return com.dmg.lobbyserver.result.Result
     * @description: 同步金币
     * @author mice
     * @date 2019/6/18
     */
    @GetMapping("/syncGold")
    public Result syncGold(@RequestParam("userId") Long userId, @RequestParam("changeGold") Long changeGold) {
        UserBean userBean = userService.getUserById(userId);
        userBean.setAccountBalance(userBean.getAccountBalance().add(BigDecimal.valueOf(changeGold)));
        userService.updateUserById(userBean);
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(1, "", UPDATE_USER_INFO_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }
        return Result.success("");
    }
}