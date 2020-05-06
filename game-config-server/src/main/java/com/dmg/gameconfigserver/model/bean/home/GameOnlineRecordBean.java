package com.dmg.gameconfigserver.model.bean.home;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:36 2020/3/13
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_dmg_game_online_record")
public class GameOnlineRecordBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 位置编号
     */
    private Integer place;
    /**
     * 位置名称
     */
    private String placeName;
    /**
     * 场次id
     */
    private Integer fileId;
    /**
     * 场次名称
     */
    private String fileName;
    /**
     * 在线人数
     */
    private Integer onlineNum;
    /**
     * 记录时间
     */
    private Date recordDate;

}
