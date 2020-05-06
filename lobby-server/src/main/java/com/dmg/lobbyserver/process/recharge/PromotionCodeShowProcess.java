package com.dmg.lobbyserver.process.recharge;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SysPromotionCodeDao;
import com.dmg.lobbyserver.dao.bean.SysPromotionCodeBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static com.dmg.lobbyserver.config.MessageConfig.PROMOTION_CODE_SHOW;
/**
 * @Description  用户优惠代码显示
 * @Author jock
 * @Date 2019/7/3 0003
 * @Version V1.0
 **/
@Slf4j
@Service
public class PromotionCodeShowProcess  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return PROMOTION_CODE_SHOW;
    }
    @Autowired
    SysPromotionCodeDao sysPromotionCodeDao ;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        List<SysPromotionCodeBean> sysPromotionCodeBeans = sysPromotionCodeDao.selectList(new LambdaQueryWrapper<SysPromotionCodeBean>().
                eq(SysPromotionCodeBean::getUserId, userid).eq(SysPromotionCodeBean::getHasReceive,1).eq(SysPromotionCodeBean::getPromotionType,1));
        //过滤过期时间
        List<SysPromotionCodeBean> collect = sysPromotionCodeBeans.stream().filter(x -> x.getExpireDate().getTime() >=
                new Date().getTime()).collect(Collectors.toList());
        result.setMsg(collect);
    }
}
