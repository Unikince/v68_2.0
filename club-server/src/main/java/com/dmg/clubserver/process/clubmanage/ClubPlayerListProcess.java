package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.dto.ClubPlayerListDTO;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.vo.ClubRememberListVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_PLAYER_LIST;
import static java.util.function.Function.identity;

/**
 * @Description 俱乐部成员列表(含查询)
 * @Author mice
 * @Date 2019/5/27 19:17
 * @Version V1.0
 **/
@Slf4j
@Service
public class ClubPlayerListProcess implements AbstractMessageHandler{

    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Override
    public String getMessageId() {
        return CLUB_PLAYER_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params,MessageResult result) {
        ClubRememberListVO vo = params.toJavaObject(ClubRememberListVO.class);
        List<ClubPlayerListDTO> playerList = new ArrayList<>();
        // 有查询条件
        if (vo.getSearchId() != null){
            RoleInfoBean roleInfoBean = synchronousPlayerDataService.getOnePlayerInfo(vo.getSearchId()+"");
            if (roleInfoBean == null){
                result.setMsg(JSONObject.toJSON(playerList));
                return;
            }
            ClubPlayerListDTO clubPlayerListDTO = new ClubPlayerListDTO();
            BeanUtils.copyProperties(roleInfoBean,clubPlayerListDTO);
            RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                    .eq(RClubPlayerBean::getClubId,vo.getClubId())
                    .eq(RClubPlayerBean::getRoleId,vo.getSearchId()));
            if (clubPlayerBean != null){
                clubPlayerListDTO.setPlayerStatus(clubPlayerBean.getStatus());
                clubPlayerListDTO.setPosition(clubPlayerBean.getPosition());
                clubPlayerListDTO.setClubPlayer(1);
            }else {
                clubPlayerListDTO.setClubPlayer(0);
            }
            playerList.add(clubPlayerListDTO);
            result.setMsg(JSONObject.toJSON(playerList));
            return;
        }
        // 无查询条件
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .orderByAsc(RClubPlayerBean::getPosition));
        List<Integer> roleIdList = clubPlayerBeanList.stream().map(RClubPlayerBean::getRoleId).collect(Collectors.toList());
        String  roleIds = StringUtils.join(roleIdList.toArray(), ",");
        List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(roleIds);
        Map<Integer,RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId, identity()));
        RoleInfoBean roleInfoBean = null;
        for (RClubPlayerBean clubPlayerBean : clubPlayerBeanList){
            roleInfoBean = roleInfoBeanMap.get(clubPlayerBean.getRoleId());
            ClubPlayerListDTO clubPlayerListDTO = new ClubPlayerListDTO();
            BeanUtils.copyProperties(roleInfoBean,clubPlayerListDTO);
            clubPlayerListDTO.setPlayerStatus(clubPlayerBean.getStatus());
            clubPlayerListDTO.setClubPlayer(1);
            clubPlayerListDTO.setPosition(clubPlayerBean.getPosition());
            playerList.add(clubPlayerListDTO);
        }
        result.setMsg(JSONObject.toJSON(playerList));
    }
}