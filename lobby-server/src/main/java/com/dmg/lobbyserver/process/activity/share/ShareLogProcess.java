package com.dmg.lobbyserver.process.activity.share;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.config.MessageConfig;
import com.dmg.lobbyserver.dao.*;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.service.RedPointService;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static com.dmg.lobbyserver.config.MessageConfig.SHARE_LOG;
/**
 * @Description 用户分享日志
 * @Author jock
 * @Date 2019/7/5 0005
 * @Version V1.0
 **/
@Slf4j
@Service
public class ShareLogProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return SHARE_LOG;
    }

    @Autowired
    SysShareLogDao sysShareLogDao;
    @Autowired
    TaskConfigDao taskConfigDao;
    @Autowired
    SysRewardConfigDao sysRewardConfigDao;
    @Autowired
    SysRewardConfigDetailDao sysRewardConfigDetailDao;
    @Autowired
    UserService userService;
    @Autowired
    SysItemConfigDao sysItemConfigDao;
    @Autowired
    GiftDataService giftDataService ;
    @Autowired
    RedPointService redPointService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        //判断今日是否领取
        SysShareLogBean sysShareLogBean1 = sysShareLogDao.coutShare(Long.parseLong(userid));
        if(sysShareLogBean1!=null){
            result.setRes(ResultEnum.SHARE_LIMIT_DAY.getCode());
            return;
        }
        SysShareLogBean sysShareLogBean = new SysShareLogBean();
        sysShareLogBean.setCreateDate(new Date());
        sysShareLogBean.setShareType(1);
        sysShareLogBean.setUserId(Long.parseLong(userid));
        sysShareLogDao.insert(sysShareLogBean);
        List<TaskConfigBean> taskConfigBean1 = taskConfigDao.selectList(new LambdaQueryWrapper<TaskConfigBean>().
                eq(TaskConfigBean::getTaskType, 6).eq(TaskConfigBean::getTaskStatus, 1));
        //过滤 过期时间
        List<TaskConfigBean> taskConfigBeans = taskConfigBean1.stream().filter(x -> x.getTaskEndTime().getTime() >= new Date().getTime()).
                sorted(Comparator.comparing(TaskConfigBean::getTaskEndTime)).collect(Collectors.toList());
        List<GiftDataDTO> giftDataDTO = null;
        GiftDataDTO giftDataDTO1 =null;
        for (TaskConfigBean taskConfigBean : taskConfigBeans) {
            giftDataDTO=new ArrayList<>();
            giftDataDTO1=new GiftDataDTO() ;
            String taskRewardIds = taskConfigBean.getTaskRewardIds();
            List<SysRewardConfigBean> rewards = sysRewardConfigDao.getRewards(taskRewardIds);
            //系统奖励配置详情
            List<List<SysRewardConfigDetailBean>> lists = new ArrayList<>();
            for (SysRewardConfigBean reward : rewards) {
                List<SysRewardConfigDetailBean> sysRewards = sysRewardConfigDetailDao.getSysRewards(reward.getRewardDetailId());
                lists.add(sysRewards);
            }
            for (List<SysRewardConfigDetailBean> list : lists) {
                for (SysRewardConfigDetailBean sysRewardConfigDetailBean : list) {
                    SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectOne(new LambdaQueryWrapper<SysItemConfigBean>().
                            eq(SysItemConfigBean::getId, sysRewardConfigDetailBean.getItemId()));
                    giftDataDTO1.setSmallPicId(sysItemConfigBean.getSmallPicId());
                    giftDataDTO1.setRemark(sysItemConfigBean.getRemark());
                    giftDataDTO1.setItemNumber(String.valueOf(sysRewardConfigDetailBean.getItemNum()));
                    giftDataDTO1.setItemName(sysItemConfigBean.getItemName());
                    giftDataDTO.add(giftDataDTO1);
                    //添加积分到个人账户
                    UserBean userBean = userService.getUserById(Long.parseLong(userid));
                    userBean.setIntegral(userBean.getIntegral()+sysRewardConfigDetailBean.getItemNum());
                }
            }
            giftDataService.sendGiftData(Long.parseLong(userid),giftDataDTO);
        }
        result.setMsg(JSON.toJSON(giftDataDTO));
        redPointService.push(Long.parseLong(userid), MessageConfig.SHARE_NTC,false);
    }
}