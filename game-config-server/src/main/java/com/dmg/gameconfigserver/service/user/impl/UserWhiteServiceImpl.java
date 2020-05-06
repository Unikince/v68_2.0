package com.dmg.gameconfigserver.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.gameconfigserver.dao.user.UserDao;
import com.dmg.gameconfigserver.dao.user.UserWhiteDao;
import com.dmg.gameconfigserver.model.bean.user.UserWhiteBean;
import com.dmg.gameconfigserver.model.dto.user.UserWhiteDTO;
import com.dmg.gameconfigserver.model.vo.user.UserWhiteVO;
import com.dmg.gameconfigserver.service.user.UserWhiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:26 2020/3/16
 */
@Slf4j
@Service
public class UserWhiteServiceImpl implements UserWhiteService {

    @Autowired
    private UserWhiteDao userWhiteDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDao userDao;

    private static String sessionKey = "session:";

    @Override
    public IPage<UserWhiteVO> getUserWhitePage(UserWhiteDTO userWhiteDTO) {
        Page page = new Page(userWhiteDTO.getCurrent(), userWhiteDTO.getSize());
        IPage<UserWhiteVO> result = userWhiteDao.getUserWhitePage(page, userWhiteDTO.getUserId(), userWhiteDTO.getUserName());
        if (CollectionUtils.isEmpty(result.getRecords())) {
            return result;
        }
        result.getRecords().forEach(userWhiteVO -> {
            if (redisUtil.hasKey(sessionKey.concat(String.valueOf(userWhiteVO.getId())))) {
                userWhiteVO.setIsOnline(true);
            }
        });
        return result;
    }

    @Override
    public String insert(String[] userIds, Long sysUserId) {
        StringBuilder result = new StringBuilder();
        for (String userId : userIds) {
            if (userDao.selectById(userId) == null) {
                result.append(userId + ":查询无该用户;");
                continue;
            }
            if (userWhiteDao.selectOne(new LambdaQueryWrapper<UserWhiteBean>()
                    .eq(UserWhiteBean::getUserId, userId)) != null) {
                result.append(userId + ":该用户已在白名单;");
                continue;
            }
            UserWhiteBean userWhiteBean = new UserWhiteBean();
            userWhiteBean.setUserId(Long.parseLong(userId));
            userWhiteBean.setCreateDate(new Date());
            userWhiteBean.setModifyDate(new Date());
            userWhiteBean.setCreateUser(sysUserId);
            userWhiteBean.setModifyUser(sysUserId);
            userWhiteDao.insert(userWhiteBean);
        }
        this.refreshDeviceCode();
        return result.length() == 0 ? "ok" : result.toString();
    }

    @Override
    public void deleteById(Long id) {
        userWhiteDao.deleteById(id);
        this.refreshDeviceCode();
    }

    private void refreshDeviceCode() {
        try {
            List<String> deviceCodeList = userWhiteDao.getUserWhiteDeviceCode();
            redisUtil.del(redisUtil.keys(RedisRegionConfig.USER_WHITE_DEVICE_CODE + ":" + "*"));
            if (deviceCodeList != null && deviceCodeList.size() > 0) {
                deviceCodeList.forEach(deviceCode -> {
                    redisUtil.set(RedisRegionConfig.USER_WHITE_DEVICE_CODE + ":" + deviceCode, true);
                });
            }
        } catch (Exception e) {
            log.error("刷新白名单设备号数据异常", e);
        }
    }
}
