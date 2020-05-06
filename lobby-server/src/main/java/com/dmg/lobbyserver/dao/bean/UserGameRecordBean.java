package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description  用户游戏记录
 * @Author jock
 * @Date 17:22
 * @Version V1.0
 **/
@Data
@TableName("user_game_record")
public class UserGameRecordBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 牌局编号
     */
    private String matchNumber;
    /**
     * 房间类型(1.平民场2.老板场3.土豪场4.大师场)
     */
    private Integer roomType;
    /**
     * 盈利金额
     */
    private double profitMoney;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 游戏类型(1.牛牛2.金花)
     */
    private Integer gameType;



}
