package com.dmg.gameconfigserver.model.bean.config;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 *
 *
 * @author mice
 * @email .com
 * @date 2019-09-30 11:32:55
 */
@Data
@TableName("t_file_base_config")
public class FileBaseConfigBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 场次id
     */
    private Integer fileId;
    /**
     * 场次名称
     */
    private String fileName;
    /**
     * 是否开启 0:未开启 1:已开启
     */
    private Integer openStatus;
    /**
     * 人数上限
     */
    private Integer playerUpLimit;
    /**
     * 抽水百分比
     */
    private BigDecimal pumpRate;

}
