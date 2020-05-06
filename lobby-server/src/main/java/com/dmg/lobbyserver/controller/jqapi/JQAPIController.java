package com.dmg.lobbyserver.controller.jqapi;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.exception.BusinessException;
import com.dmg.lobbyserver.model.vo.YYChangeMobileVO;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeMap;

import static com.dmg.common.core.util.MD5Util.stringToMD5;
import static com.dmg.lobbyserver.common.constants.CommonConstants.SECRETKEY;

/**
 * @Description
 * @Author mice
 * @Date 2020/2/6 9:47
 * @Version V1.0
 **/
@RequestMapping("jqapi")
@RestController
@Slf4j
public class JQAPIController {
    private static String WITHDRAW_CHANNEL_URL = "http://gmapi.bigfun.io/api/paypassage/stranspassage?channelCode=%s&outUserId=%s&sign=%s";
    private static String RECHARGE_CHANNEL_URL = "http://gmapi.bigfun.io/api/paypassage/spaypassage?channelCode=%s&sign=%s";
    private static String CHANGE_MOBILE_URL = "http://gmapi.bigfun.io/api/auth/schangemobile";
    private static String CUSTOMSERVICE_URL = "http://gmapi.bigfun.io/api/paypassage/scustomservice?channelCode=%s&sign=%s";
    private static String GET_SHARE_URL = "http://gmapi.bigfun.io/api/auth/sgetShareUrl?channelCode=%s&outUserId=%s&sign=%s";

    //可用提现通道查询
    @RequestMapping("withdrawChannel")
    public Result withdrawChannel(@RequestParam("userId") String userId,@RequestParam("channelCode")String channelCode){
        if (StringUtils.isEmpty(userId)){
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        TreeMap<String, String> paramMap = new TreeMap<>();
        paramMap.put("channelCode",channelCode);
        paramMap.put("outUserId",userId);
        String sign = sign(paramMap);
        String url = String.format(WITHDRAW_CHANNEL_URL,channelCode,userId ,sign);
        log.info("==>可用提现通道查询url:{}",url);
        String result = HttpUtil.get(url);
        log.info("==>可用提现通道查询 返回信息:{}",result);
        JSONObject resultJSON = JSONObject.parseObject(result);
        if (!"0".equals(resultJSON.getString("code"))){
            throw new BusinessException(ResultEnum.SYSTEM_EXCEPTION.getCode(),ResultEnum.SYSTEM_EXCEPTION.getMsg());
        }
        return Result.success(resultJSON.getJSONArray("data"));
    }

    //可用充值通道查询
    @RequestMapping("rechargeChannel")
    public Result rechargeChannel(@RequestParam("token") String token,@RequestParam("channelCode") String channelCode){
        if (StringUtils.isEmpty(token)){
            throw new BusinessException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMsg());
        }
        TreeMap<String, String> paramMap = new TreeMap<>();
        paramMap.put("channelCode",channelCode);
        String sign = sign(paramMap);
        String url = String.format(RECHARGE_CHANNEL_URL,channelCode,sign);
        log.info("==>可用充值通道查询url:{}",url);
        String result = HttpRequest.get(url).header("accessToken",token).execute().body();
        log.info("==>可用充值通道查询 返回信息:{}",result);
        JSONObject resultJSON = JSONObject.parseObject(result);
        if (!"0".equals(resultJSON.getString("code"))){
            throw new BusinessException(ResultEnum.SYSTEM_EXCEPTION.getCode(),ResultEnum.SYSTEM_EXCEPTION.getMsg());
        }
        return Result.success(resultJSON.getJSONArray("data"));
    }

    //修改手机号
    @RequestMapping("changeMobile")
    public Result changeMobile(@Validated @RequestBody YYChangeMobileVO vo) {
        TreeMap<String, String> paramMap = new TreeMap<>();
        paramMap.put("mobile",vo.getMobile());
        paramMap.put("newMobile",vo.getNewMobile());
        paramMap.put("vertifyCode",vo.getVertifyCode());
        paramMap.put("channelCode",vo.getChannelCode());
        String sign = sign(paramMap);
        paramMap.put("sign",sign);
        String result = HttpRequest.post(CHANGE_MOBILE_URL).header("accessToken",vo.getToken()).execute().body();
        log.info("==>修改手机号 返回信息:{}",result);
        JSONObject resultJSON = JSONObject.parseObject(result);
        String code = resultJSON.getString("code");
        //"4008", "验证码错误" "4012", "该手机号码已被他人绑定" "4009", "手机绑定失败"
        if ("0".equals(code)){
            return Result.success();
        }else if ("4008".equals(code)){
            return Result.error(ResultEnum.VALIDATE_CODE_ERROR.getCode(),ResultEnum.VALIDATE_CODE_ERROR.getMsg());
        }else if ("4012".equals(code)){
            return Result.error(ResultEnum.PHONE_HAS_REGISTE.getCode(),ResultEnum.PHONE_HAS_REGISTE.getMsg());
        }else {
            return Result.error(ResultEnum.OPERRATE_FAIL.getCode(),ResultEnum.OPERRATE_FAIL.getMsg());
        }
    }

    //获取客服列表
    @RequestMapping("getCustomerService")
    public Result getCustomerService(String channelCode){

        TreeMap<String, String> paramMap = new TreeMap<>();
        paramMap.put("channelCode",channelCode);
        String sign = sign(paramMap);
        String url = String.format(CUSTOMSERVICE_URL,channelCode,sign);
        log.info("==>获取客服列表url:{}",url);
        String result = HttpRequest.get(url).execute().body();
        log.info("==>获取客服列表 返回信息:{}",result);
        JSONObject resultJSON = JSONObject.parseObject(result);
        if (!"0".equals(resultJSON.getString("code"))){
            throw new BusinessException(ResultEnum.SYSTEM_EXCEPTION.getCode(),ResultEnum.SYSTEM_EXCEPTION.getMsg());
        }
        return Result.success(resultJSON.getJSONArray("data"));
    }

    //获取分享链接
    @RequestMapping("getShareUrl")
    public Result getShareUrl(@RequestParam("userId") String userId,@RequestParam("channelCode")String channelCode){
        TreeMap<String, String> paramMap = new TreeMap<>();
        paramMap.put("channelCode",channelCode);
        String sign = sign(paramMap);
        String url = String.format(GET_SHARE_URL,channelCode,userId,sign);
        log.info("==>获取分享链接url:{}",url);
        String result = HttpRequest.get(url).execute().body();
        log.info("==>获取分享链接_返回信息:{}",result);
        JSONObject resultJSON = JSONObject.parseObject(result);
        if (!"0".equals(resultJSON.getString("code"))){
            throw new BusinessException(ResultEnum.SYSTEM_EXCEPTION.getCode(),ResultEnum.SYSTEM_EXCEPTION.getMsg());
        }
        return Result.success(resultJSON.getJSONObject("data"));
    }

    private static String sign(TreeMap<String, String> paramMap){
        StringBuilder sb = new StringBuilder();
        for (String key : paramMap.keySet()){
            String value = paramMap.get(key);
            sb.append(key + "=" + value + "&");
        }
        sb.append("key=");
        sb.append(SECRETKEY);
        log.info("签名字符串==>{}",sb.toString());
        String oursign = stringToMD5(sb.toString()).toLowerCase();
        log.info("签名==>{}",oursign);
        return oursign;
    }
}