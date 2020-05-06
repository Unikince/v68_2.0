package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.UpdateClubRemarkVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.clubserver.config.MessageConfig.UPDATE_CLUB_REMARK;

/**
 * @Description 更新俱乐部公告
 * @Author mice
 * @Date 2019/5/27 19:17
 * @Version V1.0
 **/
@Slf4j
@Service
public class UpdateClubRemarkProcess implements AbstractMessageHandler{

    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;

    @Override
    public String getMessageId() {
        return UPDATE_CLUB_REMARK;
    }

    @Override
    public void messageHandler(String userid, JSONObject params,MessageResult result) {
        UpdateClubRemarkVO vo = params.toJavaObject(UpdateClubRemarkVO.class);
        ClubBean clubBean = clubDao.selectOne(new LambdaQueryWrapper<ClubBean>().eq(ClubBean::getClubId,vo.getClubId()));
        if (clubBean == null){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,Integer.parseInt(userid)));
        if (clubPlayerBean.getPosition()==3){
            result.setRes(ResultEnum.NO_AUTH.getCode());
            return;
        }
        clubBean.setRemark(vo.getRemark());
        clubDao.updateById(clubBean);
    }
}