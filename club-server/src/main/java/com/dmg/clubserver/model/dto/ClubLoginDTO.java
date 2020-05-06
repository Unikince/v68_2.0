package com.dmg.clubserver.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 13:53
 * @Version V1.0
 **/
@Data
public class ClubLoginDTO implements Serializable {
    private List<HasJoinClubDTO> hasJoinClubList;
    private boolean review;


    @Data
    public static class HasJoinClubDTO{
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
         * 管理员id
         */
        private Integer creatorId;
        /**
         * 俱乐部广告
         */
        private String remark;
    }

}