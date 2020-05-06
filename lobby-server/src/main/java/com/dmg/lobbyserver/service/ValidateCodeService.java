package com.dmg.lobbyserver.service;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/18 16:15
 * @Version V1.0
 **/
public interface ValidateCodeService {

    /**
     * @description: 验证码过期
     * @param number phoneNumber/email/userId
     * @return boolean
     * @author mice
     * @date 2019/6/18
    */
    boolean expire(String number);

    /**
     * @description: 验证码是否正确
     * @param number phoneNumber/email/userId
     * @param validateCode
     * @return boolean
     * @author mice
     * @date 2019/6/18
    */
    boolean validateSuccess(String number,String validateCode);
}