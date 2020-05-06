package com.dmg.lobbyserver.process.activity.share;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDetailDao;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigDetailBean;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.process.activity.UserProessService;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dmg.lobbyserver.config.MessageConfig.SHARE_GET;

/**
 * 分享送礼
 * Author:刘将军
 * Time:2019/6/19 18:46
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class ShareGetProcess implements AbstractMessageHandler {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SysItemConfigDao sysItemConfigDao;

    @Autowired
    private UserProessService userProessService;

    @Autowired
    private SysRewardConfigDao sysRewardConfigDao;

    @Autowired
    private SysRewardConfigDetailDao sysRewardConfigDetailDao;

    @Autowired
    private GiftDataService giftDataService;

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    @Override
    public String getMessageId() {
        return SHARE_GET;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("reward_type", 2);
        List<SysRewardConfigBean> sysRewardConfigBeans = sysRewardConfigDao.selectByMap(columnMap);
        if (sysRewardConfigBeans.size() > 1) {
            result.setRes(ResultEnum.DATA_CONVERSION.getCode());
            result.setMsg("数据格式有误！");
            return;
        }
        SysRewardConfigBean sysRewardConfigBean = sysRewardConfigBeans.get(0);

        //sysRewardConfigBean.getRewardDetailId() 包含的是sysRewardConfigDetail的ID
        String[] sysRewardConfigDetailIdArray = sysRewardConfigBean.getRewardDetailId().split(",");
        List<SysRewardConfigDetailBean> sysRewardConfigDetailBeans = sysRewardConfigDetailDao.
                selectBatchIds(Arrays.asList(sysRewardConfigDetailIdArray));
        List<GiftDataDTO> giftDataDTOList = new LinkedList<>();
        for (SysRewardConfigDetailBean sysRewardConfigDetailBean : sysRewardConfigDetailBeans) {
            SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectById(sysRewardConfigDetailBean.getItemId());
            userProessService.getItems(sysItemConfigBean.getItemType(), sysRewardConfigDetailBean.getItemNum(), userid);

            GiftDataDTO giftDataDTO = new GiftDataDTO();
            giftDataDTO.setItemNumber(String.valueOf(sysRewardConfigDetailBean.getItemNum()));
            BeanUtils.copyProperties(sysItemConfigBean, giftDataDTO);
            giftDataDTOList.add(giftDataDTO);
        }
        giftDataService.sendGiftData(Long.parseLong(userid), giftDataDTOList);
    }
}
