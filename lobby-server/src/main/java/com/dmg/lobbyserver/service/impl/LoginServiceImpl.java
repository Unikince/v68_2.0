package com.dmg.lobbyserver.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.manager.CacheConfigManager;
import com.dmg.lobbyserver.manager.VersionManager;
import com.dmg.lobbyserver.model.bean.SNSUserInfo;
import com.dmg.lobbyserver.model.bean.WeixinOauth2Token;
import com.dmg.lobbyserver.model.dto.HttpLoginDTO;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.*;
import com.dmg.lobbyserver.common.util.weChat.AdvancedUtil;
import com.dmg.server.common.enums.TaskTypeEnum;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.dmg.lobbyserver.result.ResultEnum.ACCOUNT_VALIDATE_ERROR;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/18 11:31
 * @Version V1.0
 **/
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${server.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;

    @Value("${md5.salt}")
    private String salt;

    public HttpLoginDTO touristLogin(String deviceCode, Long userId,String ip) {
        UserBean userBean;
        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);

        userBean = userService.getUserByDeviceCode(deviceCode);
        if (userBean == null){
            if (userId == null || userId <= 0) {
                userBean = initUser(1, deviceCode,ip);
                userService.insert(userBean);
            } else {
                userBean = userService.getUserById(userId);
                if (userBean == null) {
                    userBean = initUser(1, deviceCode,ip);
                    userService.insert(userBean);
                } else {
                    userBean.setDeviceCode(deviceCode);
                }
            }
        }
        userBean.setLoginDate(new Date());
        userService.updateUserById(userBean);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));
        this.userTaskReceive(userBean.getId());
        return httpLoginDTO;
    }

    private UserBean initUser(Integer tourist, String deviceCode,String ip) {
        UserBean userBean = new UserBean();
        userBean.setUserCode(idGeneratorService.getUserCode());
        if (tourist == 0) {
            userBean.setUserName("user" + userBean.getUserCode());
        } else {
            userBean.setUserName("visitor" + userBean.getUserCode());
        }
        userBean.setRegisterIp(ip);
        userBean.setLoginDate(new Date());
        userBean.setAccountBalance(BigDecimal.valueOf(10000));
        userBean.setHeadImage("1");
        userBean.setTourist(tourist);
        userBean.setSex(1);
        userBean.setVipLevel(1);
        userBean.setIntegral(0L);
        userBean.setNewUser(1);
        userBean.setStrongboxBalance(BigDecimal.ZERO);
        userBean.setDeviceCode(deviceCode);
        userBean.setCreateDate(new Date());
        return userBean;
    }

    @Override
    public HttpLoginDTO weChatLogin(String deviceCode, String code, int deviceType) {
        if ("authdeny".equals(code) || code == null) {
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMsg());
        }
        // 1.通过code,appId,appSecret获取授权token
        WeixinOauth2Token authToken = AdvancedUtil.getOauth2AccessToken(appId, appSecret, code);
        if (authToken == null) {
            // code码错误或者该code码已经使用过了
            throw new BusinessException(ResultEnum.WECHAT_CODE_HAS_USE_OR_ERROR.getCode(), ResultEnum.WECHAT_CODE_HAS_USE_OR_ERROR.getMsg());
        }
        // 2.通过授权和唯一openId获取用户信息
        SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(authToken.getAccessToken(), authToken.getOpenId());
        if (snsUserInfo == null) {
            // openId无效或者其他
            throw new BusinessException(ResultEnum.OPENID_INVALID.getCode(), ResultEnum.OPENID_INVALID.getMsg());
        }
        // 3.通过openId验证玩家是否存在。不存在添加新玩家，存在验证玩家头像或昵称是否有更新
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getOpenId, snsUserInfo.getOpenId()));
        if (userBean == null) {
            userBean = new UserBean();
            userBean.setTourist(0);
            userBean.setUserCode(idGeneratorService.getUserCode());
            userBean.setUserName(snsUserInfo.getNickname());
            userBean.setOpenId(snsUserInfo.getOpenId());
            userBean.setHeadImage(snsUserInfo.getHeadImgUrl() + "?aa=aa.jpg");
            userBean.setAccountBalance(BigDecimal.valueOf(1000000.0));
            userBean.setVipLevel(1);
            userBean.setIntegral(0L);
            userBean.setNewUser(1);
            userBean.setSex(snsUserInfo.getSex());
            userBean.setStrongboxBalance(BigDecimal.ZERO);
            userBean.setDeviceCode(deviceCode);
            userService.insert(userBean);
        }
        userBean.setDeviceCode(deviceCode);
        userService.updateUserById(userBean);
        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(deviceType));
        this.userTaskReceive(userBean.getId());
        return httpLoginDTO;
    }

    @Override
    public HttpLoginDTO userNameLogin(String deviceCode, String userName, String password) {
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getUserName, userName));
        if (userBean == null) {
            throw new BusinessException(ResultEnum.ACCOUNT_NOT_EXIST.getCode(), ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
        }
        if (!DigestUtil.md5Hex(password + salt).equals(userBean.getPassword())) {
            throw new BusinessException(ResultEnum.ACCOUNT_ERROR.getCode(), ResultEnum.ACCOUNT_ERROR.getMsg());
        }
        userBean.setLoginDate(new Date());
        userService.updateUserById(userBean);
        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));
        this.userTaskReceive(userBean.getId());
        return httpLoginDTO;
    }

    @Override
    public HttpLoginDTO userNameRegiste(String deviceCode, String userName, String password,String ip) {
        UserBean userBean = userService.getUserByDeviceCode(deviceCode);
        if (userBean!=null){
            throw new BusinessException(ResultEnum.ACCOUNT_HAS_EXIST.getCode(), ResultEnum.ACCOUNT_HAS_EXIST.getMsg());
        }
        // 敏感字符
        boolean hasSensitive = CacheConfigManager.instance().isContaintSensitiveWord(userName);
        if (hasSensitive) {
            throw new BusinessException(ResultEnum.SENSITIVE_CHARACTER.getCode(), ResultEnum.SENSITIVE_CHARACTER.getMsg());
        }
        userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getUserName, userName));
        if (userBean != null) {
            throw new BusinessException(ResultEnum.ACCOUNT_HAS_EXIST.getCode(), ResultEnum.ACCOUNT_HAS_EXIST.getMsg());
        }
        userBean = initUser(0, deviceCode,ip);
        userBean.setUserName(userName);
        userBean.setPassword(DigestUtil.md5Hex(password + salt));
        userService.insert(userBean);

        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));
        return httpLoginDTO;
    }

    @Override
    public HttpLoginDTO oneClickRegiste(String phone, String deviceCode,String ip) {
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getPhone, phone));
        if (userBean != null) {
            throw new BusinessException(ResultEnum.PHONE_HAS_REGISTE.getCode(), ResultEnum.PHONE_HAS_REGISTE.getMsg());
        }
        userBean = userService.getUserByDeviceCode(deviceCode);
        if (userBean!=null){
            throw new BusinessException(ResultEnum.ACCOUNT_HAS_EXIST.getCode(), ResultEnum.ACCOUNT_HAS_EXIST.getMsg());
        }
        userBean = initUser(0, deviceCode,ip);
        userBean.setPhone(phone);
        userService.insert(userBean);

        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));
        return httpLoginDTO;
    }

    @Override
    public HttpLoginDTO oneClickLogin(String phone, String deviceCode,String ip) {
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getPhone, phone));
        if (userBean == null) {
            throw new BusinessException(ResultEnum.PHONE_NO_REGISTE.getCode(), ResultEnum.PHONE_NO_REGISTE.getMsg());
        }

        if (!deviceCode.equals(userBean.getDeviceCode())) {
            throw new BusinessException(ResultEnum.DEVICE_CODE_DIFF.getCode(), ResultEnum.DEVICE_CODE_DIFF.getMsg());
        }

        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));
        this.userTaskReceive(userBean.getId());
        return httpLoginDTO;
    }

    @Override
    public HttpLoginDTO validateCodeLogin(String deviceCode, String phone, String validateCode,String ip) {
        if (!validateCodeService.validateSuccess(phone, validateCode)) {
            throw new BusinessException(ResultEnum.VALIDATE_CODE_ERROR.getCode(), ResultEnum.VALIDATE_CODE_ERROR.getMsg());
        }
        if (!validateCodeService.expire(phone)) {
            throw new BusinessException(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode(), ResultEnum.VALIDATE_CODE_TIME_OUT.getMsg());
        }

        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getPhone, phone));
        if (userBean == null) {
            userBean = initUser(0, deviceCode,ip);
            userBean.setPhone(phone);
            userService.insert(userBean);
        }
        userBean.setLoginDate(new Date());
        userBean.setDeviceCode(deviceCode);
        userService.updateUserById(userBean);
        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));

        this.userTaskReceive(userBean.getId());
        return httpLoginDTO;
    }

    @Override
    public HttpLoginDTO keyLogin(String deviceCode, Long userId, String sign) {
        if (!sign.equals(DigestUtil.md5Hex(userId + salt))) {
            throw new BusinessException(ACCOUNT_VALIDATE_ERROR.getCode(), ACCOUNT_VALIDATE_ERROR.getMsg());
        }
        UserBean userBean = userService.getUserById(userId);
        userBean.setLoginDate(new Date());
        userService.updateUserById(userBean);
        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();
        httpLoginDTO.setIp(host);
        httpLoginDTO.setPort(port);
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));
        this.userTaskReceive(userBean.getId());
        return httpLoginDTO;
    }

    @Override
    public Boolean checkPhoneUserName(String phone, String userName, String validateCode) {
        if (!validateCodeService.validateSuccess(phone, validateCode)) {
            throw new BusinessException(ResultEnum.VALIDATE_CODE_ERROR.getCode(), ResultEnum.VALIDATE_CODE_ERROR.getMsg());
        }
        if (!validateCodeService.expire(phone)) {
            throw new BusinessException(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode(), ResultEnum.VALIDATE_CODE_TIME_OUT.getMsg());
        }
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getUserName, userName));
        if (userBean == null) {
            throw new BusinessException(ResultEnum.ACCOUNT_NOT_EXIST.getCode(), ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
        }
        if (StringUtils.isEmpty(userBean.getPhone())) {
            throw new BusinessException(ResultEnum.NO_BIND_PHONE.getCode(), ResultEnum.NO_BIND_PHONE.getMsg());
        }
        if (!phone.equals(userBean.getPhone())) {
            throw new BusinessException(ResultEnum.BIND_PHONE_DIFF_USERNAME.getCode(), ResultEnum.BIND_PHONE_DIFF_USERNAME.getMsg());
        }
        return true;
    }

    @Override
    public boolean changePassword(String userName, String password) {
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getUserName, userName));
        if (userBean == null) {
            throw new BusinessException(ResultEnum.ACCOUNT_NOT_EXIST.getCode(), ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
        }
        userBean.setPassword(DigestUtil.md5Hex(password + salt));
        userDao.updateById(userBean);
        return true;
    }

    private void userTaskReceive(Long userId) {
        try {
            Object object = redisUtil.get(RedisKey.USER_LGOGIN.concat(":").concat(DateUtils.getDate("yyyyMMdd"))
                    .concat(":").concat(String.valueOf(userId)));
            if (object == null) {
                redisUtil.set(RedisKey.USER_LGOGIN.concat(":").concat(DateUtils.getDate("yyyyMMdd"))
                        .concat(":").concat(String.valueOf(userId)), "1", 1, TimeUnit.DAYS);
                userTaskProgressService.userTaskChange(userId, TaskTypeEnum.CODE_LOGIN.getCode(), 1, null);
            }
        } catch (Exception e) {
            log.error("任务进度变更出现异常:{}", e);
        }
    }
}