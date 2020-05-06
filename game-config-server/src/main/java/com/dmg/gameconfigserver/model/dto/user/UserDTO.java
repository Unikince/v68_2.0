package com.dmg.gameconfigserver.model.dto.user;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/20 14:39
 * @Version V1.0
 **/
@Data
public class UserDTO extends PageReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户id
    private Long userId;
    //用户昵称
    private String userName;
    //手机号
    private String phone;

}