package com.dmg.clubserver.process.clubinvitation;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubInvitationDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubInvitationBean;
import com.dmg.clubserver.model.dto.InvitationListDTO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_INVITATION_LIST;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/31 17:48
 * @Version V1.0
 **/
@Service
public class InvitationListProcess implements AbstractMessageHandler {
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private ClubInvitationDao clubInvitationDao;
    @Override
    public String getMessageId() {
        return CLUB_INVITATION_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<ClubInvitationBean> clubInvitationBeans = clubInvitationDao.selectList(new LambdaQueryWrapper<ClubInvitationBean>().eq(ClubInvitationBean::getBeInvitorId,Integer.parseInt(userid)));
        if (CollectionUtil.isEmpty(clubInvitationBeans)){
            result.setMsg(new ArrayList<>());
            return;
        }
        List<Integer> clubIds = clubInvitationBeans.stream().map(ClubInvitationBean::getClubId).collect(Collectors.toList());
        List<ClubBean> clubBeanList = clubDao.selectList(new LambdaQueryWrapper<ClubBean>().in(ClubBean::getClubId,clubIds));
        Map<Integer,ClubBean> clubBeanMap = clubBeanList.stream().collect(Collectors.toMap(ClubBean::getClubId, Function.identity()));
        List<InvitationListDTO> invitationListDTOS = new ArrayList<>();
        clubInvitationBeans.forEach(clubInvitationBean -> {
            ClubBean clubBean = clubBeanMap.get(clubInvitationBean.getClubId());
            InvitationListDTO invitationListDTO = new InvitationListDTO();
            BeanUtils.copyProperties(clubBean,invitationListDTO);
            invitationListDTOS.add(invitationListDTO);
        });
        result.setMsg(JSONObject.toJSON(invitationListDTOS));
    }
}