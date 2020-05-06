package com.dmg.clubserver.model.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/31 17:54
 * @Version V1.0
 **/
@Data
public class InvitationListDTO {

    /**
     * 俱乐部id
     */
    private Integer clubId;
    /**
     * 俱乐部名称
     */
    private String name;
    /**
     * 当前成员人数
     */
    private Integer currentMemberNum;
    /**
     * 成员人数上限
     */
    private Integer memberNumLimit;
}