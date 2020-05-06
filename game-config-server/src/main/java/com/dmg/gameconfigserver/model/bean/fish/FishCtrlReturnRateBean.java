package com.dmg.gameconfigserver.model.bean.fish;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;

/**
 * 捕鱼返奖率控制
 */
@TableName("f_fish_ctrl_return_rate")
public class FishCtrlReturnRateBean extends FishCtrlReturnRateDTO {
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
