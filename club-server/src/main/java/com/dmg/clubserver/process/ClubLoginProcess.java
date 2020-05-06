package com.dmg.clubserver.process;

import static com.dmg.clubserver.config.MessageConfig.CLUB_LOGIN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dmg.clubserver.config.RedPointType;
import com.dmg.clubserver.dao.ClubInvitationDao;
import com.dmg.clubserver.dao.ClubJoinRequestDao;
import com.dmg.clubserver.dao.bean.ClubInvitationBean;
import com.dmg.clubserver.dao.bean.ClubJoinRequestBean;
import com.dmg.clubserver.model.dto.ClubLobbyReviewListDTO;
import com.dmg.clubserver.service.RedPointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.dto.ClubLoginDTO;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.SynchronousPlayerDataService;

/**
 * @Description 俱乐部登录
 * @Author mice
 * @Date 2019/5/25 16:00
 * @Version V1.0
 **/
@Service
public class ClubLoginProcess implements AbstractMessageHandler{
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubJoinRequestDao clubJoinRequestDao;
    @Autowired
    private RedPointService redPointService;
    @Autowired
    private ClubInvitationDao clubInvitationDao;

    @Override
    public String getMessageId() {
        return CLUB_LOGIN;
    }


    @Override
    public void messageHandler(String userid,JSONObject params,MessageResult result) {
        RoleInfoBean roleInfoBean = synchronousPlayerDataService.getOnePlayerInfo(userid);
        if (roleInfoBean == null){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        ClubLoginDTO clubLoginDTO = new ClubLoginDTO();
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getRoleId,roleInfoBean.getRoleId())
                .in(RClubPlayerBean::getPosition, Arrays.asList(1,2)));
        if (CollectionUtil.isEmpty(clubPlayerBeanList)){
            clubLoginDTO.setReview(false);
        }else {
            clubLoginDTO.setReview(true);
            // 审核列表红点推送
            List<Integer> clubIds = clubPlayerBeanList.stream().map(RClubPlayerBean::getClubId).collect(Collectors.toList());
            Integer hasRequests = clubJoinRequestDao.selectCount(new LambdaQueryWrapper<ClubJoinRequestBean>().in(ClubJoinRequestBean::getClubId,clubIds).isNull(ClubJoinRequestBean::getReviewerId));
            if (hasRequests>0){
                redPointService.push(roleInfoBean.getRoleId(), RedPointType.REVIEW.getKey());
            }
        }
        // 加入红点推送
        Integer hasInvitations = clubInvitationDao.selectCount(new LambdaQueryWrapper<ClubInvitationBean>().eq(ClubInvitationBean::getBeInvitorId,Integer.parseInt(userid)));
        if (hasInvitations>0){
            redPointService.push(roleInfoBean.getRoleId(), RedPointType.JOIN.getKey());
        }

        // TODO  返回字段有删减 逻辑未优化
        List<ClubLoginDTO.HasJoinClubDTO> hasJoinClubDTOS = new ArrayList<>();
        clubLoginDTO.setHasJoinClubList(hasJoinClubDTOS);
        List<Integer> hasJoinClubIds ;
        clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getRoleId,roleInfoBean.getRoleId()));
        if (CollectionUtil.isEmpty(clubPlayerBeanList)){
            result.setMsg(JSONObject.toJSON(clubLoginDTO));
            return;
        }
        hasJoinClubIds = clubPlayerBeanList.stream().map(RClubPlayerBean::getClubId).collect(Collectors.toList());
        List<RClubPlayerBean> clubManagerList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>().in(RClubPlayerBean::getClubId,hasJoinClubIds).in(RClubPlayerBean::getPosition, Arrays.asList(1,2)));
        Map<Integer,List<RClubPlayerBean>> clubPlayerMap = clubManagerList.stream().collect(Collectors.groupingBy(RClubPlayerBean::getClubId));

        List<ClubBean> clubBeans = clubDao.selectList(new LambdaQueryWrapper<ClubBean>().in(ClubBean::getClubId,hasJoinClubIds));
        clubBeans.forEach(c -> {
            ClubLoginDTO.HasJoinClubDTO hasJoinClubDTO = new ClubLoginDTO.HasJoinClubDTO();
            BeanUtils.copyProperties(c,hasJoinClubDTO);
            List<RClubPlayerBean> clubPlayers = clubPlayerMap.get(c.getClubId());
            clubPlayers.forEach(rClubPlayerBean -> {
                if (rClubPlayerBean.getPosition()==1){
                    hasJoinClubDTO.setCreatorId(rClubPlayerBean.getRoleId());
                }
            });
            hasJoinClubDTOS.add(hasJoinClubDTO);
        });
        result.setMsg(JSONObject.toJSON(clubLoginDTO));
    }
}