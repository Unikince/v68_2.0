package com.dmg.clubserver.process.clubrequest;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubJoinRequestDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.dto.ClubLobbyReviewListDTO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_LOBBY_REVIEW_LIST;
import static java.util.function.Function.identity;

/**
 * @Description 获取俱乐部大厅审核列表
 * @Author mice
 * @Date 2019/5/28 15:01
 * @Version V1.0
 **/
@Service
public class ClubLobbyReviewListProcess implements AbstractMessageHandler {
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Autowired
    private RClubPlayerDao clubPlayerDao;


    @Override
    public String getMessageId() {
        return CLUB_LOBBY_REVIEW_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Integer roleId = Integer.parseInt(userid);
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getRoleId,roleId)
                .in(RClubPlayerBean::getPosition, Arrays.asList(1,2)));
        if (CollectionUtil.isEmpty(clubPlayerBeanList)){
            result.setRes(ResultEnum.NO_CLUB_OPERATE_AUTH.getCode());
            return;
        }
        List<ClubLobbyReviewListDTO> clubLobbyReviewListDTOS = new ArrayList<>();
        List<Integer> clubIds = clubPlayerBeanList.stream().map(RClubPlayerBean::getClubId).collect(Collectors.toList());
        List<ClubJoinRequestBean> requestBeans = clubJoinRequestDao.selectList(new LambdaQueryWrapper<ClubJoinRequestBean>().isNull(ClubJoinRequestBean::getReviewerId).in(ClubJoinRequestBean::getClubId,clubIds));
        if (CollectionUtil.isEmpty(requestBeans)){
            result.setMsg(JSONObject.toJSON(clubLobbyReviewListDTOS));
            return;
        }
        List<ClubBean> clubBeanList = clubDao.selectList(new LambdaQueryWrapper<ClubBean>().in(ClubBean::getClubId,clubIds));
        Map<Integer,ClubBean> clubBeanMap = clubBeanList.stream().collect(Collectors.toMap(ClubBean::getClubId,identity()));
        List<Integer> requestorIds = requestBeans.stream().map(ClubJoinRequestBean::getRequestorId).collect(Collectors.toList());
        List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(StringUtils.join(requestorIds,","));
        Map<Integer,RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId,identity()));
        requestBeans.forEach(requestBean -> {
            ClubBean clubBean = clubBeanMap.get(requestBean.getClubId());
            RoleInfoBean roleInfoBean = roleInfoBeanMap.get(requestBean.getRequestorId());
            ClubLobbyReviewListDTO clubLobbyReviewListDTO = new ClubLobbyReviewListDTO();
            clubLobbyReviewListDTO.setClubId(clubBean.getClubId());
            clubLobbyReviewListDTO.setClubName(clubBean.getName());
            clubLobbyReviewListDTO.setCurrentMemberNum(clubBean.getCurrentMemberNum());
            clubLobbyReviewListDTO.setMemberNumLimit(clubBean.getMemberNumLimit());
            clubLobbyReviewListDTO.setRequestorId(roleInfoBean.getRoleId());
            clubLobbyReviewListDTO.setNickName(roleInfoBean.getNickName());
            clubLobbyReviewListDTO.setHeadImage(roleInfoBean.getHeadImage());
            clubLobbyReviewListDTO.setRequestDate(requestBean.getRequestDate());
            clubLobbyReviewListDTOS.add(clubLobbyReviewListDTO);
        });
        result.setMsg(JSONObject.toJSON(clubLobbyReviewListDTOS));
    }
}