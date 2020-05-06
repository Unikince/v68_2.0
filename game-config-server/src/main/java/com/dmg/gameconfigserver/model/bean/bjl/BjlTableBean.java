package com.dmg.gameconfigserver.model.bean.bjl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserverapi.bjl.dto.BjlTableDTO;

/**
 * 百家乐场次配置
 */
@TableName("f_bjl_table")
public class BjlTableBean extends BjlTableDTO {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
