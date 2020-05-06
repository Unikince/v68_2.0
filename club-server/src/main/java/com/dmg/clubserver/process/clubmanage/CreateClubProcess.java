package com.dmg.clubserver.process.clubmanage;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.dto.ClubLoginDTO;
import com.dmg.clubserver.model.vo.CreateClubVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.IdGeneratorService;
import com.dmg.clubserver.service.ValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CREATE_CLUB;

/**
 * @Description 创建俱乐部
 * @Author mice
 * @Date 2019/5/27 11:17
 * @Version V1.0
 **/
@Slf4j
@Service
public class CreateClubProcess implements AbstractMessageHandler{

    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private IdGeneratorService idGeneratorService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ValidateCodeService validateCodeService;

    @Override
    public String getMessageId() {
        return CREATE_CLUB;
    }

    @Override
    public void messageHandler(String userid, JSONObject params,MessageResult result) {
        CreateClubVO vo = params.toJavaObject(CreateClubVO.class);
        // 验证创建俱乐部数量
        int clubCount = clubDao.selectCount(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getCreatorId,vo.getCreatorId()));
        if (clubCount>=3){
            result.setRes(ResultEnum.CLUB_NUMBER_LIMIT.getCode());
            return;
        }
        clubCount = clubDao.selectCount(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getName,vo.getName()));
        if (clubCount>0){
            result.setRes(ResultEnum.CLUB_NAME_EXIST.getCode());
            return;
        }
        /*if (!validateCodeService.validateSuccess(vo.getPhone(),vo.getValidateCode())){
            result.setRes(ResultEnum.VALIDATE_CODE_ERROR.getCode());
            return;
        }
        if (!validateCodeService.timeOut(vo.getPhone())){
            result.setRes(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode());
            return;
        }*/
        ClubBean clubBean = new ClubBean();
        clubBean.setName(vo.getName());
        clubBean.setRemark(vo.getRemark());
        clubBean.setClubId(idGeneratorService.getClubId());
        clubBean.setCreatorId(vo.getCreatorId());
        // 当前设计无等级经验 默认200人
        clubBean.setMemberNumLimit(200);
        clubBean.setCreateDate(new Date());
        clubBean.setRoomNumLimit(50);
        clubBean.setRoomCard(100);
        clubDao.insert(clubBean);
        log.info("俱乐部创建成功");
        RClubPlayerBean clubPlayerBean = new RClubPlayerBean();
        clubPlayerBean.setClubId(clubBean.getClubId());
        clubPlayerBean.setJoinDate(new Date());
        clubPlayerBean.setPosition(1);
        clubPlayerBean.setRoleId(clubBean.getCreatorId());
        clubPlayerDao.insert(clubPlayerBean);

        ClubLoginDTO clubLoginDTO = new ClubLoginDTO();
        List<ClubLoginDTO.HasJoinClubDTO> hasJoinClubDTOS = new ArrayList<>();
        clubLoginDTO.setHasJoinClubList(hasJoinClubDTOS);
        List<Integer> hasJoinClubIds ;
        List<RClubPlayerBean> clubPlayerBeanList = clubPlayerDao.selectList(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getRoleId,vo.getCreatorId()));
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