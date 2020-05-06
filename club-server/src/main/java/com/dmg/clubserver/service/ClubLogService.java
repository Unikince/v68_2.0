package com.dmg.clubserver.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.ClubLogType;
import com.dmg.clubserver.dao.ClubLogDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubLogBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/3 15:08
 * @Version V1.0
 **/
@Service
@Slf4j
public class ClubLogService {
    @Autowired
    private ClubLogDao clubLogDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;


    @Transactional
    public void saveLog(Integer operatorId,Integer beOperatorId,Integer operateType,Integer clubId,String remark){
        ClubLogBean clubLogBean = new ClubLogBean();
        clubLogBean.setOperatorId(operatorId);
        clubLogBean.setBeOperatorId(beOperatorId);
        clubLogBean.setOperateType(operateType);
        clubLogBean.setClubId(clubId);
        clubLogBean.setOperateType(operateType);
        clubLogBean.setOperateDate(new Date());
        clubLogBean.setRemark(remark);
        if (ClubLogType.QUIT_CLUB.getKey() == operateType){
            clubLogBean.setPosition(Integer.parseInt(remark));
        }else {
            RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>().eq(RClubPlayerBean::getClubId,clubId).eq(RClubPlayerBean::getRoleId,operatorId));
            clubLogBean.setPosition(clubPlayerBean.getPosition());
        }

        clubLogDao.insert(clubLogBean);
    }
}