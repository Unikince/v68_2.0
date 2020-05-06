package com.dmg.lobbyserver.process.personalaccount;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.UserVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.ValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_EMAIL;
/**
 * @Description 修改邮箱
 * @Author jock
 * @Date 2019/6/21 0021
 * @Version V1.0
 **/
@Service
@Slf4j
public class UpdateemailProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return UPDATE_EMAIL;
    }

    @Autowired
    UserDao userDao;
    @Autowired
    ValidateCodeService validateCodeService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserVo userVo = params.toJavaObject(UserVo.class);
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getId, userid));
        if (!validateCodeService.validateSuccess(userVo.getNewEmail(), userVo.getEmailCode())) {
            result.setRes(ResultEnum.VALIDATE_CODE_ERROR.getCode());
            return;
        }
        if (!validateCodeService.expire(userVo.getNewEmail())) {
            result.setRes(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode());
            return;
        }
        Long countByEmail = userDao.getCountByEmail(userVo.getNewEmail());
        if (countByEmail > 0) {
            result.setRes(ResultEnum.EMAIL_ISEXIST.getCode());
        } else {
            if (userBean != null && StringUtils.equals(userBean.getEmail(), userVo.getOldEmail())) {
                //修改的邮箱是否与旧邮箱相同
                if(userVo.getNewEmail().equals(userVo.getOldEmail())){
                    result.setRes(ResultEnum.EMAIL_ISSAME_ASNEWEMAIL.getCode());
                    return;
                }
                userBean.setEmail(userVo.getNewEmail());
                userDao.updateById(userBean);
                result.setRes(ResultEnum.SUCCESS.getCode());
            } else {
                result.setRes(ResultEnum.EMAIL_ISSAME.getCode());
            }
        }
    }
}
