package com.dmg.lobbyserver.process.personalaccount;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.SysSettingsVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.dmg.lobbyserver.config.MessageConfig.SYS_SETTING;

/**
 * @Description 设置 保存指纹 面部 手势key
 * @Author mice
 * @Date 2019/6/21 17:20
 * @Version V1.0
 **/
@Service
public class SysSettingsProcess implements AbstractMessageHandler {
    @Autowired
    private UserService userService;
    @Override
    public String getMessageId() {
        return SYS_SETTING;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        SysSettingsVO vo = params.toJavaObject(SysSettingsVO.class);
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (vo.getKeyType() == 1){
            userBean.setFingerprintKey(vo.getKey());
        }else if (vo.getKeyType() == 2){
            userBean.setGestureKey(vo.getKey());
        }else if (vo.getKeyType() == 3){
            userBean.setFaceKey(vo.getKey());
        }
        userService.updateUserById(userBean);
    }
}