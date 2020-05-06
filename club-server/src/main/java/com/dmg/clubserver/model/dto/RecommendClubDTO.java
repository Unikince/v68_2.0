package com.dmg.clubserver.model.dto;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 11:16
 * @Version V1.0
 **/
@Data
public class RecommendClubDTO {
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
    /**
     * 俱乐部广告
     */
    private String remark;
    /**
     * 申请状态 0:未申请 1:已申请 2:已加入
     */
    private Integer requsetStatus;
}