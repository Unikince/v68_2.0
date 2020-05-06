package com.dmg.clubserver.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 15:43
 * @Version V1.0
 **/
@Data
public class ClubLobbyReviewListDTO {
    private Integer requestorId;
    private String nickName;
    private String headImage;
    /**
     * 俱乐部id
     */
    private Integer clubId;
    /**
     * 申请时间
     */
    private Date requestDate;
    /**
     * 俱乐部名称
     */
    private String clubName;
    /**
     * 当前成员人数
     */
    private Integer currentMemberNum;
    /**
     * 成员人数上限
     */
    private Integer memberNumLimit;
}