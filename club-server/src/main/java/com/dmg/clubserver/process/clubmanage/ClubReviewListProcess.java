package com.dmg.clubserver.process.clubmanage;

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
import com.dmg.clubserver.model.dto.ClubReviewListDTO;
import com.dmg.clubserver.model.vo.ClubReviewListVO;
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

import static com.dmg.clubserver.config.MessageConfig.CLUB_REVIEW_LIST;
import static java.util.function.Function.identity;

/**
 * @Description 获取俱乐部内审核列表
 * @Author mice
 * @Date 2019/5/28 15:01
 * @Version V1.0
 **/
@Service
public class ClubReviewListProcess implements AbstractMessageHandler {
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
        return CLUB_REVIEW_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ClubReviewListVO vo = params.toJavaObject(ClubReviewListVO.class);
        // 权限检查
        RClubPlayerBean manager = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
        if (manager.getPosition()==3){
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }
        List<ClubReviewListDTO> clubReviewListDTOS = new ArrayList<>();

        if (vo.getRequestorId() != null){
            ClubJoinRequestBean requestBean = clubJoinRequestDao.selectOne(new LambdaQueryWrapper<ClubJoinRequestBean>()
                    .eq(ClubJoinRequestBean::getClubId,vo.getClubId())
                    .eq(ClubJoinRequestBean::getRequestorId,vo.getRequestorId()));
            if (requestBean == null){
                result.setMsg(JSONObject.toJSON(clubReviewListDTOS));
                return;
            }
            RoleInfoBean roleInfoBean = synchronousPlayerDataService.getOnePlayerInfo(vo.getRequestorId()+"");
            ClubReviewListDTO clubReviewListDTO = new ClubReviewListDTO();
            clubReviewListDTO.setClubId(requestBean.getClubId());
            clubReviewListDTO.setHeadImage(roleInfoBean.getHeadImage());
            clubReviewListDTO.setLevel(roleInfoBean.getLevel());
            clubReviewListDTO.setNickName(roleInfoBean.getNickName());
            clubReviewListDTO.setHeadImage(roleInfoBean.getHeadImage());
            clubReviewListDTO.setRoleId(roleInfoBean.getRoleId());
            clubReviewListDTOS.add(clubReviewListDTO);
            result.setMsg(JSONObject.toJSON(clubReviewListDTOS));
            return;
        }
        List<ClubJoinRequestBean> requestBeans = clubJoinRequestDao.selectList(new LambdaQueryWrapper<ClubJoinRequestBean>()
                .eq(ClubJoinRequestBean::getClubId,vo.getClubId())
                .isNull(ClubJoinRequestBean::getReviewerId));
        if (CollectionUtil.isEmpty(requestBeans)){
            result.setMsg(JSONObject.toJSON(clubReviewListDTOS));
            return;
        }
        List<Integer> requestorIds = requestBeans.stream().map(ClubJoinRequestBean::getRequestorId).collect(Collectors.toList());
        List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(StringUtils.join(requestorIds,","));
        Map<Integer,RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId,identity()));
        requestBeans.forEach(requestBean -> {
            RoleInfoBean roleInfoBean = roleInfoBeanMap.get(requestBean.getRequestorId());
            ClubReviewListDTO clubReviewListDTO = new ClubReviewListDTO();
            clubReviewListDTO.setClubId(requestBean.getClubId());
            clubReviewListDTO.setHeadImage(roleInfoBean.getHeadImage());
            clubReviewListDTO.setLevel(roleInfoBean.getLevel());
            clubReviewListDTO.setNickName(roleInfoBean.getNickName());
            clubReviewListDTO.setHeadImage(roleInfoBean.getHeadImage());
            clubReviewListDTO.setRoleId(roleInfoBean.getRoleId());
            clubReviewListDTOS.add(clubReviewListDTO);
        });
        result.setMsg(JSONObject.toJSON(clubReviewListDTOS));
    }
}