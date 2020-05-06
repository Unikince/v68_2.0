package com.dmg.clubserver.process.clubmanage;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.dto.ClubFreezeListDTO;
import com.dmg.clubserver.model.vo.ClubFreezeListVO;
import com.dmg.clubserver.model.vo.FreezeAndUnFreezeVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_FREEZE_LIST;

/**
 * @Description 冻结列表(含查询)
 * @Author mice
 * @Date 2019/5/28 19:30
 * @Version V1.0
 **/
@Service
public class ClubFreezeListProcess implements AbstractMessageHandler {
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;

    @Override
    public String getMessageId() {
        return CLUB_FREEZE_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ClubFreezeListVO vo = params.toJavaObject(ClubFreezeListVO.class);
        RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId, vo.getClubId())
                .eq(RClubPlayerBean::getRoleId, vo.getRoleId()));
        if (clubPlayerBean.getPosition() == 3) {
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }
        List<ClubFreezeListDTO> clubFreezeListDTOS = new ArrayList<>();
        if (vo.getFreezerId()!=null){
            RClubPlayerBean playerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                    .eq(RClubPlayerBean::getClubId, vo.getClubId())
                    .eq(RClubPlayerBean::getRoleId, vo.getFreezerId())
                    .eq(RClubPlayerBean::getStatus, 1));
            if (playerBean == null){
                result.setMsg(JSONObject.toJSON(clubFreezeListDTOS));
                return;
            }
            RoleInfoBean roleInfoBean = synchronousPlayerDataService.getOnePlayerInfo(vo.getFreezerId()+"");
            ClubFreezeListDTO clubFreezeListDTO = new ClubFreezeListDTO();
            BeanUtils.copyProperties(roleInfoBean, clubFreezeListDTO);
            clubFreezeListDTO.setPlayerStatus(1);
            clubFreezeListDTOS.add(clubFreezeListDTO);
            result.setMsg(JSONObject.toJSON(clubFreezeListDTOS));
            return;
        }
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId, vo.getClubId())
                .eq(RClubPlayerBean::getStatus, 1)
                .eq(clubPlayerBean.getPosition() == 2, RClubPlayerBean::getPosition, 3));
        if (CollectionUtil.isEmpty(clubPlayerBeanList)) {
            result.setMsg(JSONObject.toJSON(clubFreezeListDTOS));
            return;
        }
        List<Integer> roleIds = clubPlayerBeanList.stream().map(RClubPlayerBean::getRoleId).collect(Collectors.toList());
        List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(StringUtils.join(roleIds, ","));
        Map<Integer, RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId, Function.identity()));

        clubPlayerBeanList.forEach(c -> {
            ClubFreezeListDTO clubFreezeListDTO = new ClubFreezeListDTO();
            RoleInfoBean roleInfoBean = roleInfoBeanMap.get(c.getRoleId());
            BeanUtils.copyProperties(roleInfoBean, clubFreezeListDTO);
            clubFreezeListDTO.setPlayerStatus(1);
            clubFreezeListDTOS.add(clubFreezeListDTO);
        });
        result.setMsg(JSONObject.toJSON(clubFreezeListDTOS));

    }
}