package com.dmg.gameconfigserver.model.bean.home;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("t_dmg_game_pump_record")
public class GamePumpRecordBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 游戏名称
     */
    private String gameName;
    /**
     * 场次id
     */
    private Integer fileId;
    /**
     * 场次名称
     */
    private String fileName;
    /**
     * 水池
     */
    private BigDecimal pump;
    /**
     * 记录时间
     */
    private Date recordDate;

}
