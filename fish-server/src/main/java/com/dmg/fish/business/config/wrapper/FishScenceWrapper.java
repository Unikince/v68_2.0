package com.dmg.fish.business.config.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.dmg.gameconfigserverapi.fish.dto.FishScenceDTO;

import cn.hutool.core.bean.BeanUtil;

/**
 * 捕鱼场景(json解析)
 */
public class FishScenceWrapper extends FishScenceDTO {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 刷鱼策略 */
    private final List<Integer> strategysData;

    /** 集合转换 */
    public static List<FishScenceWrapper> convertList(List<FishScenceDTO> dtos) {
        List<FishScenceWrapper> result = new ArrayList<>();
        for (FishScenceDTO dto : dtos) {
            result.add(new FishScenceWrapper(dto));
        }
        return result;
    }

    /** 构建方法 */
    public FishScenceWrapper(FishScenceDTO dto) {
        BeanUtil.copyProperties(dto, this);
        this.strategysData = JSON.parseArray("[" + dto.getStrategys() + "]", Integer.class);
    }

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * 获取：刷鱼策略
     *
     * @return 刷鱼策略
     */
    public List<Integer> getStrategysData() {
        return this.strategysData;
    }

}
