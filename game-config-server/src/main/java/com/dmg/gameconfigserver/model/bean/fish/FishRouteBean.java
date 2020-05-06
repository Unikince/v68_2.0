package com.dmg.gameconfigserver.model.bean.fish;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserverapi.fish.dto.FishRouteDTO;

/**
 * 捕鱼-鱼路线图
 */
@TableName("f_fish_route")
public class FishRouteBean extends FishRouteDTO {
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
