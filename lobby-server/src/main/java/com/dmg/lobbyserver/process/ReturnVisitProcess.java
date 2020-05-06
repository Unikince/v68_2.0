package com.dmg.lobbyserver.process;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.ReturnVisitDao;
import com.dmg.lobbyserver.dao.bean.ReturnVisitBean;
import com.dmg.lobbyserver.model.vo.ReturnVisitVO;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.dmg.lobbyserver.config.MessageConfig.RETURN_VISIT;

/**
 * @Description 保存回访电话
 * @Author mice
 * @Date 2019/6/19 17:49
 * @Version V1.0
 **/
@Service
public class ReturnVisitProcess implements AbstractMessageHandler{
    @Autowired
    private ReturnVisitDao returnVisitDao;
    @Autowired
    private ValidateCodeService validateCodeService;
    @Override
    public String getMessageId() {
        return RETURN_VISIT;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ReturnVisitVO vo = params.toJavaObject(ReturnVisitVO.class);

        if(!validateCodeService.validateSuccess(vo.getUserId()+"",vo.getValidateCode())){
            result.setRes(ResultEnum.VALIDATE_CODE_ERROR.getCode());
            return;
        }
        if (!validateCodeService.expire(vo.getUserId()+"")){
            result.setRes(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode());
            return;
        }
        ReturnVisitBean returnVisitBean = returnVisitDao.selectOne(new LambdaQueryWrapper<ReturnVisitBean>().eq(ReturnVisitBean::getUserId,vo.getUserId()));
        if (returnVisitBean == null){
            returnVisitBean = new ReturnVisitBean();
            returnVisitBean.setUserId(vo.getUserId());
            returnVisitBean.setRequestDate(new Date());
            returnVisitBean.setPhone(vo.getPhone());
            returnVisitDao.insert(returnVisitBean);
        }else {
            if (System.currentTimeMillis()-returnVisitBean.getRequestDate().getTime()<1000*60*30){
                result.setRes(ResultEnum.RETURN_VISIT_ERROR.getCode());
                return;
            }
            returnVisitBean.setRequestDate(new Date());
            returnVisitBean.setPhone(vo.getPhone());
            returnVisitDao.updateById(returnVisitBean);
        }

    }
}