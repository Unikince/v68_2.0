package com.dmg.lobbyserver.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.VnnnRechargeLogDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.dao.bean.VnnnRechargeLogBean;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/20 17:44
 * @Version V1.0
 **/
@RestController
public class GameServerInfoController {

    @Autowired
    UserDao userDao;
    @Autowired
    private VnnnRechargeLogDao vnnnRechargeLogDao;

    @RequestMapping("gameServerInfo")
    public JSONObject getGameServerInfo(){
        // 新增用户
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long newUser = userDao.countTodayNewUser();
        //所有用户
        long allUser = userDao.selectCount(new LambdaQueryWrapper<UserBean>());
        //今日登录用户
        long loginUser = userDao.countTodayLoginUser();
        //当日充值人数
        long rechargePlayer = vnnnRechargeLogDao.rechargePlayer();
        //当日充值金额
        Long rechargeValue = vnnnRechargeLogDao.rechargeValue();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("新增用户",newUser);
        jsonObject.put("所有用户",allUser);
        jsonObject.put("今日登录用户",loginUser);
        jsonObject.put("当日充值人数",rechargePlayer);
        jsonObject.put("当日充值金额",rechargeValue==null?0:rechargeValue);
        return jsonObject;
    }
}