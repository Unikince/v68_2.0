package com.dmg.lobbyserver.process.email;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.UserEmailDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.UserEmailBean;
import com.dmg.lobbyserver.model.dto.UserEmailDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.EMAIL_LIST;

/**
 * @Description 获取邮件列表
 * @Author mice
 * @Date 2019/6/20 11:04
 * @Version V1.0
 **/
@Service
public class EmailListProcess implements AbstractMessageHandler {

    @Autowired
    private UserEmailDao userEmailDao;

    @Autowired
    private SysItemConfigDao sysItemConfigDao;

    @Override
    public String getMessageId() {
        return EMAIL_LIST;
    }

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {
        List<UserEmailBean> userEmailBeanList = userEmailDao.selectList(new LambdaQueryWrapper<UserEmailBean>()
                .ge(UserEmailBean::getExpireDate, new Date())
                .le(UserEmailBean::getSendDate, new Date())
                .eq(UserEmailBean::getUserId, Long.parseLong(userId))
                .orderByAsc(UserEmailBean::getHasRead)
                .orderByDesc(UserEmailBean::getSendDate));
        if (CollectionUtil.isEmpty(userEmailBeanList)) {
            return;
        }
        List<UserEmailDTO> userEmailDTOList = new ArrayList<>();
        userEmailBeanList.forEach(userEmailBean -> {
            UserEmailDTO userEmailDTO = new UserEmailDTO();
            BeanUtils.copyProperties(userEmailBean, userEmailDTO);
            if (userEmailDTO.getItemType() != null) {
                SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectOne(new LambdaQueryWrapper<SysItemConfigBean>().
                        eq(SysItemConfigBean::getId, userEmailBean.getItemType()));
                if (sysItemConfigBean != null) {
                    userEmailDTO.setItemNumber(userEmailBean.getItemNum());
                    userEmailDTO.setItemName(sysItemConfigBean.getItemName());
                    userEmailDTO.setRemark(sysItemConfigBean.getRemark());
                    userEmailDTO.setSmallPicId(sysItemConfigBean.getSmallPicId());
                }
            }
            userEmailDTOList.add(userEmailDTO);
        });
        result.setMsg(userEmailDTOList);
    }
}