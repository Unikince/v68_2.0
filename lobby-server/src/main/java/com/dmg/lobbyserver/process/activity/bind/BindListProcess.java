package com.dmg.lobbyserver.process.activity.bind;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDetailDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigDetailBean;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dmg.lobbyserver.config.MessageConfig.BIND_LIST;

/**
 * 绑定
 * Author:刘将军
 * Time:2019/6/19 18:46
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class BindListProcess implements AbstractMessageHandler {
    @Autowired
    private SysRewardConfigDao sysRewardConfigDao;
    @Autowired
    private SysItemConfigDao sysItemConfigDao;
    @Autowired
    private SysRewardConfigDetailDao sysRewardConfigDetailDao;
    @Autowired
    private GiftDataService giftDataService;

    @Override
    public String getMessageId() {
        return BIND_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("reward_type",3);
        List<SysRewardConfigBean> sysRewardConfigBeans = sysRewardConfigDao.selectByMap(columnMap);
        if (sysRewardConfigBeans.size() > 1){
            result.setRes(ResultEnum.DATA_CONVERSION.getCode());
            result.setMsg("数据格式有误！");
            return;
        }
        SysRewardConfigBean sysRewardConfigBean = sysRewardConfigBeans.get(0);
        List<GiftDataDTO> giftDataDTOList = new LinkedList<>();
        //sysRewardConfigBean.getRewardDetailId() 包含的是sysRewardConfigDetail的ID
        String[] sysRewardConfigDetailIdArray = sysRewardConfigBean.getRewardDetailId().split(",");
        List<SysRewardConfigDetailBean> sysRewardConfigDetailBeans = sysRewardConfigDetailDao.
                selectBatchIds(Arrays.asList(sysRewardConfigDetailIdArray));
        for (SysRewardConfigDetailBean sysRewardConfigDetailBean : sysRewardConfigDetailBeans) {
            GiftDataDTO giftDataDTO = new GiftDataDTO();
            SysItemConfigBean sysItemConfigBean = sysItemConfigDao.
                    selectById(sysRewardConfigDetailBean.getItemId());
            BeanUtils.copyProperties(sysItemConfigBean,giftDataDTO);
            giftDataDTO.setItemNumber(String.valueOf(sysRewardConfigDetailBean.getItemNum()));
            giftDataDTOList.add(giftDataDTO);
        }
        giftDataService.sendGiftData(Long.parseLong(userid),giftDataDTOList);
    }
}
