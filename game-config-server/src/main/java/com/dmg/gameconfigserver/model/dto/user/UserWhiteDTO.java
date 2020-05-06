package com.dmg.gameconfigserver.model.dto.user;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:30 2020/3/16
 **/
@Data
public class UserWhiteDTO extends PageReqDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户id
    private Long userId;
    //用户昵称
    private String userName;

}