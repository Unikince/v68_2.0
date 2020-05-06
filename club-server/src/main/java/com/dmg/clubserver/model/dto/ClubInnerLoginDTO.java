package com.dmg.clubserver.model.dto;

import com.dmg.clubserver.model.table.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/28 13:53
 * @Version V1.0
 **/
@Data
public class ClubInnerLoginDTO implements Serializable {

    /**
     * 俱乐部id
     */
    private Integer clubId;
    /**
     * 俱乐部名称
     */
    private String name;
    /**
     * 玩家状态 0:未冻结 1:冻结
     */
    private Integer playerStatus;
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
     * 副管理id
     */
    private List<Integer> managerIds = new ArrayList<>();
    /**
     * 俱乐部广告
     */
    private String remark;
    /**
     * 俱乐部房卡
     */
    private Integer roomCard;
    /**
     * 俱乐部牌桌
     */
    private Map<Integer,Table> tables = new ConcurrentHashMap<>();

}