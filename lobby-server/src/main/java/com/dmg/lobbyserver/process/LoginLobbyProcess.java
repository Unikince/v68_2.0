package com.dmg.lobbyserver.process;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.LoginLogDao;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.LoginLogBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.UserDTO;
import com.dmg.lobbyserver.model.vo.LoginLobbyVO;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dmg.lobbyserver.config.MessageConfig.LOGIN_LOBBY;

/**
 * @Description 登录大厅
 * @Author mice
 * @Date 2019/6/19 10:55
 * @Version V1.0
 **/
@Service
public class LoginLobbyProcess implements AbstractMessageHandler{
    @Value("${md5.salt}")
    private String salt;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogDao loginLogDao;
    @Override
    public String getMessageId() {
        return LOGIN_LOBBY;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        LoginLobbyVO vo = params.toJavaObject(LoginLobbyVO.class);
        /*if (!vo.getSign().equals(DigestUtil.md5Hex(vo.getUserId()+salt))){
            result.setRes(ResultEnum.LOGIN_LOBBY_VALIDATE_FAIL.getCode());
            return;
        }*/
        UserBean userBean = userService.getUserById(vo.getUserId());
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userBean,userDTO);
        if (StringUtils.isNotEmpty(userBean.getStrongboxPassword())){
            userDTO.setStrongboxPassword(true);
        }else {
            userDTO.setStrongboxPassword(false);
        }
        if (StringUtils.isEmpty(userBean.getPassword())){
            userDTO.setPassword(0+"");
        }else {
            userDTO.setPassword(1+"");
        }
        LoginLogBean loginLogBean = loginLogDao.selectOne(new LambdaQueryWrapper<LoginLogBean>()
                .eq(LoginLogBean::getUserId,vo.getUserId())
                .eq(LoginLogBean::getLoginDate, DateUtil.today()));
        if (loginLogBean == null){
            loginLogBean = new LoginLogBean();
            loginLogBean.setUserId(vo.getUserId());
            loginLogBean.setLoginDate(new Date());
            loginLogBean.setLoginDatetime(new Date());
            loginLogDao.insert(loginLogBean);
        }
        result.setMsg(userDTO);
    }
}