package com.dmg.lobbyserver.process.recharge;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.TaskConfigDao;
import com.dmg.lobbyserver.dao.bean.TaskConfigBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static com.dmg.lobbyserver.config.MessageConfig.PROMOTION_ALL_SHOW;

/**
 * @Description  用户优惠活动显示
 * @Author jock
 * @Date 2019/7/4 0004
 * @Version V1.0
 **/
@Service
public class PromotionActivityProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return PROMOTION_ALL_SHOW;
    }
    @Autowired
    TaskConfigDao taskConfigDao ;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<TaskConfigBean> taskConfigBeans = taskConfigDao.selectList(new LambdaQueryWrapper<TaskConfigBean>().
                eq(TaskConfigBean::getTaskType, 5));
        List<TaskConfigBean> collect1 = taskConfigBeans.stream().filter(x -> x.getTaskEndTime().getTime() >= new Date().getTime())
               .collect(Collectors.toList());
        result.setMsg(collect1);
    }
}
