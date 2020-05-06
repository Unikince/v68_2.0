package com.dmg.gameconfigserver.model.bean.fish;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserverapi.fish.dto.FishDTO;

/**
 * 捕鱼-鱼
 */
@TableName("f_fish")
public class FishBean extends FishDTO {
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
