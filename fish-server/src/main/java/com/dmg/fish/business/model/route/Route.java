package com.dmg.fish.business.model.route;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 路径
 */
public class Route {
    /** 路径id */
    public final int id;
    /** 编辑器采样数据 */
    public final List<RoutePoint> points;

    public Route(int routeId, String pointsData) {
        this.id = routeId;
        this.points = JSON.parseArray(pointsData, RoutePoint.class);
    }

}
