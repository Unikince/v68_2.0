package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.HttpLoginDTO;
import com.dmg.lobbyserver.model.vo.JQLoginVO;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 16:26
 * @Version V1.0
 **/
public interface PlatformLoginService {

    /**
     * @description: 越南JQ平台登录
     * @param deviceCode
     * @return void
     * @author mice
     * @date 2019/12/23
    */
    HttpLoginDTO jqLogin(String deviceCode, JQLoginVO vo, String ip);
}