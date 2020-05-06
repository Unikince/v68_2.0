package com.dmg.lobbyserver.process.memberaccount;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SysVipPrivilegeConfigDao;
import com.dmg.lobbyserver.dao.bean.SysVipPrivilegeConfigBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import static com.dmg.lobbyserver.config.MessageConfig.VIP__PRIVILEGE;
/**
 * @Description  vip特权显示
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Slf4j
@Service
public class ViprecordProcess  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return VIP__PRIVILEGE;
    }
@Autowired
    SysVipPrivilegeConfigDao sysVipPrivilegeConfigDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<SysVipPrivilegeConfigBean> sysVipPrivilegeConfigBeans = sysVipPrivilegeConfigDao.selectList(new LambdaQueryWrapper<SysVipPrivilegeConfigBean>().le(SysVipPrivilegeConfigBean::getId,5));
            result.setMsg(JSON.toJSON(sysVipPrivilegeConfigBeans));
    }
}
