package com.dmg.clubserver.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/30 19:19
 * @Version V1.0
 **/
@Data
public class ClubInviteVO implements Serializable {
    private static final long serialVersionUID = 1600069890763062518L;
    /**
     * 邀请人
     */
    private Integer invitorId;
    /**
     * 被邀请人
     */
    private Integer beInvitorId;
    /**
     * 俱乐部id
     */
    private Integer clubId;
}