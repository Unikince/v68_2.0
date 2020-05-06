package com.dmg.clubserver.model.vo;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/31 18:11
 * @Version V1.0
 **/
@Data
public class ReviewInvitationVO {
    private Integer roleId;
    // 是否全部处理 0:否 1:全部同意 2:全部拒绝
    private Integer allProcess;
    private Integer clubId;
    // 审核状态 0:拒绝 1:同意
    private Integer reviewStatus;
}