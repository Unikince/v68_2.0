package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.config.ClubLogType;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.ClubGameRecordDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.vo.FreezeAndUnFreezeVO;
import com.dmg.clubserver.model.vo.QuitClubVO;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.ClubLogService;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dmg.clubserver.config.MessageConfig.FREEZE_AND_UNFREEZE_NTC;
import static com.dmg.clubserver.config.MessageConfig.QUIT_CLUB;

/**
 * @Description 退出俱乐部
 * @Author mice
 * @Date 2019/5/28 16:56
 * @Version V1.0
 **/
@Service
public class QuitClubProcess implements AbstractMessageHandler {
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private ClubLogService clubLogService;
    @Autowired
    private ClubDao clubDao;

    @Override
    public String getMessageId() {
        return QUIT_CLUB;
    }

    @Override
    @Transactional
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        QuitClubVO vo = params.toJavaObject(QuitClubVO.class);
        RClubPlayerBean clubPlayerBean = clubPlayerDao.selectOne(new LambdaQueryWrapper<RClubPlayerBean>()
                .eq(RClubPlayerBean::getClubId,vo.getClubId())
                .eq(RClubPlayerBean::getRoleId,vo.getRoleId()));
        if (clubPlayerBean == null){
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        if (clubPlayerBean.getPosition()==1){
            result.setRes(ResultEnum.CREAROR_CANNT_QUIT.getCode());
            return;
        }
        clubDao.subClubMember(vo.getClubId());
        clubPlayerDao.deleteById(clubPlayerBean.getId());
        clubLogService.saveLog(clubPlayerBean.getRoleId(),null,
                ClubLogType.QUIT_CLUB.getKey(),clubPlayerBean.getClubId(),clubPlayerBean.getPosition()+"");

    }
}