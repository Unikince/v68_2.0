package com.dmg.clubserver.process.clublog;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubLogDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubLogBean;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.dto.ClubLogListDTO;
import com.dmg.clubserver.model.vo.ClubLogListVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_LOG_LIST;


/**
 * @Description 俱乐部日志列表(含查询)
 * @Author mice
 * @Date 2019/6/3 13:59
 * @Version V1.0
 **/
@Service
public class ClubLogListProcess implements AbstractMessageHandler {
    @Autowired
    private ClubLogDao clubLogDao;
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;

    @Override
    public String getMessageId() {
        return CLUB_LOG_LIST;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ClubLogListVO vo = params.toJavaObject(ClubLogListVO.class);
        List<ClubLogBean> clubLogBeans;
        if (vo.getSearchId()!=null){
            clubLogBeans = clubLogDao.selectList(new LambdaQueryWrapper<ClubLogBean>()
                    .eq(ClubLogBean::getClubId,vo.getClubId())
                    .eq(ClubLogBean::getOperatorId,vo.getSearchId())
                    .ge(ClubLogBean::getOperateDate,System.currentTimeMillis()-1000*60*60*24*7));
        }else {
            clubLogBeans = clubLogDao.selectList(new LambdaQueryWrapper<ClubLogBean>()
                    .eq(ClubLogBean::getClubId,vo.getClubId())
                    .ge(ClubLogBean::getOperateDate,System.currentTimeMillis()-1000*60*60*24*7));
        }
        List<ClubLogListDTO> clubLogListDTOS = new ArrayList<>();
        if (CollectionUtil.isEmpty(clubLogBeans)){
            result.setMsg(JSONObject.toJSON(clubLogListDTOS));
            return;
        }
        Set<Integer> roleIds = clubLogBeans.stream().map(ClubLogBean::getOperatorId).collect(Collectors.toSet());
        Set<Integer> roleIds2 = clubLogBeans.stream().map(ClubLogBean::getBeOperatorId).collect(Collectors.toSet());
        roleIds.addAll(roleIds2);
        List<RoleInfoBean> roleInfoBeanList = synchronousPlayerDataService.synchronousPlayerData(StringUtils.join(roleIds,","));
        Map<Integer,RoleInfoBean> roleInfoBeanMap = roleInfoBeanList.stream().collect(Collectors.toMap(RoleInfoBean::getRoleId, Function.identity()));
        clubLogBeans.forEach(clubLogBean -> {
            RoleInfoBean roleInfoBean = roleInfoBeanMap.get(clubLogBean.getOperatorId());
            ClubLogListDTO clubLogListDTO = new ClubLogListDTO();
            clubLogListDTO.setOperatorId(clubLogBean.getOperatorId());
            clubLogListDTO.setOperatorNickName(roleInfoBean.getNickName());
            clubLogListDTO.setHeadImage(roleInfoBean.getHeadImage());
            clubLogListDTO.setPosition(clubLogBean.getPosition());
            clubLogListDTO.setOperateDate(clubLogBean.getOperateDate());
            clubLogListDTO.setLogType(clubLogBean.getOperateType());
            clubLogListDTO.setRemark(clubLogBean.getRemark());
            if (clubLogBean.getBeOperatorId()!=null){
                clubLogListDTO.setBeOperatorId(clubLogBean.getBeOperatorId());
                clubLogListDTO.setBeOperatorNickName(roleInfoBeanMap.get(clubLogBean.getBeOperatorId()).getNickName());
            }
            clubLogListDTOS.add(clubLogListDTO);
        });
        result.setMsg(JSONObject.toJSON(clubLogListDTOS));
    }
}