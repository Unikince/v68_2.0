package com.dmg.fish.business.config.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dmg.gameconfigserverapi.fish.dto.FishRouteDTO;

import cn.hutool.core.bean.BeanUtil;

/**
 * 捕鱼-鱼路线图(json解析)
 */
public class FishRouteWrapper extends FishRouteDTO {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 路径长度 */
    private long length;

    /** 集合转换 */
    public static List<FishRouteWrapper> convertList(List<FishRouteDTO> dtos) {
        List<FishRouteWrapper> result = new ArrayList<>();
        for (FishRouteDTO dto : dtos) {
            result.add(new FishRouteWrapper(dto));
        }
        return result;
    }

    /** 构建方法 */
    public FishRouteWrapper(FishRouteDTO dto) {
        BeanUtil.copyProperties(dto, this);
        JSONArray pointData = JSON.parseArray(dto.getData());
        this.length = pointData.getJSONObject(pointData.size() - 1).getInteger("l");
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
     * 获取：路径长度
     *
     * @return 路径长度
     */
    public long getLength() {
        return this.length;
    }
}
