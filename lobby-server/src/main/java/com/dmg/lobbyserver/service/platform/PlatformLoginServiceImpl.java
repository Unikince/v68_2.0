package com.dmg.lobbyserver.service.platform;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.manager.VersionManager;
import com.dmg.lobbyserver.model.dto.HttpLoginDTO;
import com.dmg.lobbyserver.model.vo.JQLoginVO;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.PlatformLoginService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import com.dmg.server.common.enums.TaskTypeEnum;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 16:30
 * @Version V1.0
 **/
@Service
@Slf4j
public class PlatformLoginServiceImpl implements PlatformLoginService {
    @Value("${md5.salt}")
    private String salt;
    @Autowired
    private UserService userService;

    @Autowired
    private UserTaskProgressService userTaskProgressService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public HttpLoginDTO jqLogin(String deviceCode, JQLoginVO vo,String ip) {
        HttpLoginDTO httpLoginDTO = new HttpLoginDTO();

        UserBean userBean = userService.getUserById(vo.getUserId());
        if (userBean == null){
            log.info("==> {}", JSONObject.toJSONString(vo));
            userBean = new UserBean();
            userBean.setId(vo.getUserId());
            userBean.setUserCode(vo.getUserId());
            userBean.setUserName(vo.getNickName());
            userBean.setRegisterIp(ip);
            userBean.setLoginDate(new Date());
            userBean.setHeadImage(vo.getHeadImg());
            userBean.setSex(vo.getSex());
            userBean.setVipLevel(1);
            userBean.setIntegral(0L);
            userBean.setNewUser(1);
            userBean.setPhone(vo.getPhone());
            userBean.setStrongboxBalance(BigDecimal.ZERO);
            userBean.setDeviceCode(deviceCode);
            userBean.setAccountBalance(new BigDecimal(1000));
            userBean.setCreateDate(new Date());
            userBean.setChannelCode(vo.getChannelCode());
            userService.insert(userBean);
        }else {
            userBean.setChannelCode(vo.getChannelCode());
            userBean.setLoginDate(new Date());
            userBean.setUserName(vo.getNickName());
            userBean.setPhone(vo.getPhone());
            userBean.setHeadImage(vo.getHeadImg());
            userService.updateUserById(userBean);
            if (userBean.getAccountStatus() == 0){
                throw new BusinessException(ResultEnum.ACCOUNT_DISABLE.getCode(),ResultEnum.ACCOUNT_DISABLE.getMsg());

            }
        }
        httpLoginDTO.setUserId(userBean.getId());
        httpLoginDTO.setSign(DigestUtil.md5Hex(userBean.getId() + salt));
        httpLoginDTO.setUserName(userBean.getUserName());
        httpLoginDTO.setVersionInfo(VersionManager.instance().getVersionInfo(0));
        this.userTaskReceive(userBean.getId());
        return httpLoginDTO;
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