package com.dmg.gameconfigserver.model.bean.fish;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserverapi.fish.dto.FishScenceDTO;

/**
 * 捕鱼场景
 */
@TableName("f_fish_scence")
public class FishScenceBean extends FishScenceDTO {
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
