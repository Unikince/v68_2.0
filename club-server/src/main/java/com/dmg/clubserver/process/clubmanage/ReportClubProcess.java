package com.dmg.clubserver.process.clubmanage;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.dao.ClubReportInfoDao;
import com.dmg.clubserver.dao.bean.ClubReportInfoBean;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dmg.clubserver.config.MessageConfig.REPORT_CLUB;

/**
 * @Description 举报俱乐部
 * @Author mice
 * @Date 2019/6/5 10:02
 * @Version V1.0
 **/
@Service
public class ReportClubProcess implements AbstractMessageHandler {
    @Autowired
    private ClubReportInfoDao clubReportInfoDao;

    @Override
    public String getMessageId() {
        return REPORT_CLUB;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ClubReportInfoBean clubReportInfoBean = params.toJavaObject(ClubReportInfoBean.class);
        clubReportInfoBean.setReportDate(new Date());
        clubReportInfoDao.insert(clubReportInfoBean);
    }
}