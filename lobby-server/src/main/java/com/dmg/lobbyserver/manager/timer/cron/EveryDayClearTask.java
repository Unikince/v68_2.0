package com.dmg.lobbyserver.manager.timer.cron;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.config.MessageConfig;
import com.dmg.lobbyserver.dao.SignLogDao;
import com.dmg.lobbyserver.dao.TaskConfigDao;
import com.dmg.lobbyserver.dao.UserTaskProgressDao;
import com.dmg.lobbyserver.dao.bean.TaskConfigBean;
import com.dmg.lobbyserver.dao.bean.UserTaskProgressBean;
import com.dmg.lobbyserver.process.TestProcess;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/21 14:06
 * @Version V1.0
 **/
@EnableScheduling
@Component
@Slf4j
public class EveryDayClearTask {
    @Autowired
    private TaskConfigDao taskConfigDao;
    @Autowired
    private UserTaskProgressDao userTaskProgressDao;
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        EveryDayClearTask.locationManager = locationManager;
    }
    /**
     * @description: 清除每日任务进度
     * @param
     * @return void
     * @author mice
     * @date 2019/6/21
    */
    @Scheduled(cron = "1 0 0 * * ?")
    public void clearSignLog(){
        log.info("定时任务==>清除每日任务进度");
        List<TaskConfigBean> taskConfigBeanList = taskConfigDao.selectList(new LambdaQueryWrapper<TaskConfigBean>().eq(TaskConfigBean::getClearType,1));
        List<Long> taskIds = taskConfigBeanList.stream().map(TaskConfigBean::getId).collect(Collectors.toList());
        userTaskProgressDao.delete(new LambdaQueryWrapper<UserTaskProgressBean>().in(UserTaskProgressBean::getTaskId,taskIds));

        List<MyWebSocket> myWebSockets = locationManager.getWebSocketAll();
        if (CollectionUtils.isEmpty(myWebSockets)){
            return;
        }
        MessageResult messageResult = new MessageResult(MessageConfig.RED_POINT_NTC);
        myWebSockets.forEach(myWebSocket -> myWebSocket.sendMessage(JSONObject.toJSONString(messageResult,WriteNullStringAsEmpty,WriteNullListAsEmpty)));


    }


}