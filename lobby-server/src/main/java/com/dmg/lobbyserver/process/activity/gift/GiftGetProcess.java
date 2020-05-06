package com.dmg.lobbyserver.process.activity.gift;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDetailDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigDetailBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.process.activity.UserProessService;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dmg.lobbyserver.config.MessageConfig.GIFT_GET;
import static com.dmg.lobbyserver.result.ResultEnum.CANT_RECIEVE_AGAIN;

/**
 * 新手大礼包的物品列表
 * Author:刘将军
 * Time:2019/6/19 18:46
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class GiftGetProcess implements AbstractMessageHandler {

    @Autowired
    private UserService userService;
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

    @Override
    public String getMessageId() {
        return GIFT_GET;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserBean userBean = userService.getUserById(Long.valueOf(userid));
        if (userBean.getNewUser()==0){
            result.setRes(CANT_RECIEVE_AGAIN.getCode());
            return;
        }
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("reward_type",1);
        List<SysRewardConfigBean> sysRewardConfigBeans = sysRewardConfigDao.selectByMap(columnMap);
        if (sysRewardConfigBeans.size() > 1){
            result.setRes(ResultEnum.DATA_CONVERSION.getCode());
            result.setMsg("数据格式有误！");
            return;
        }
        //领完新手礼包就不是新用户了
        userBean.setNewUser(0);
        userService.updateUserById(userBean);
        SysRewardConfigBean sysRewardConfigBean = sysRewardConfigBeans.get(0);

        //sysRewardConfigBean.getRewardDetailId() 包含的是sysRewardConfigDetail的ID
        String[] sysRewardConfigDetailIdArray = sysRewardConfigBean.getRewardDetailId().split(",");
        List<SysRewardConfigDetailBean> sysRewardConfigDetailBeans = sysRewardConfigDetailDao.
                selectBatchIds(Arrays.asList(sysRewardConfigDetailIdArray));
        List<GiftDataDTO> giftDataDTOList = new LinkedList<>();
        for (SysRewardConfigDetailBean sysRewardConfigDetailBean : sysRewardConfigDetailBeans) {
            SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectById(sysRewardConfigDetailBean.getItemId());
            userProessService.getItems(sysItemConfigBean.getItemType(),sysRewardConfigDetailBean.getItemNum(),userid);

            GiftDataDTO giftDataDTO = new GiftDataDTO();
            giftDataDTO.setItemNumber(String.valueOf(sysRewardConfigDetailBean.getItemNum()));
            BeanUtils.copyProperties(sysItemConfigBean,giftDataDTO);
            giftDataDTOList.add(giftDataDTO);
        }
        giftDataService.sendGiftData(Long.parseLong(userid),giftDataDTOList);
    }
}
