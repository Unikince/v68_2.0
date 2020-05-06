package com.dmg.fish.business.model.fish;

import java.util.List;

import com.dmg.fish.business.model.FishScence;
import com.dmg.fish.business.model.route.Position;
import com.dmg.fish.business.model.route.Route;
import com.dmg.fish.business.model.route.RoutePoint;

/**
 * 鱼
 */
public class Fish {
    /** 鱼的标识id */
    public final int id;
    /** 鱼的id */
    public final int fishId;
    /** 鱼的出生时间 */
    public final long birthTime;
    /** 鱼所在的场景 */
    public final FishScence scence;
    /** 起点 */
    public final Position start;
    /** 路径 */
    public final Route route;
    /** 鱼的倍数 */
    public int multiple;
    /** 是否死亡 */
    public boolean died = false;
    /** 移动的速度 */
    public int speed;

    public Fish(int fishId, int multiple, long birthTime, Position start, Route route, int speed, FishScence scence) {
        this.id = ++scence.fishIdSeed;
        this.fishId = fishId;
        this.multiple = multiple;
        this.birthTime = birthTime;
        this.scence = scence;
        this.start = start;
        this.speed = speed;
        this.route = route;
    }

    /**
     * 鱼出生后活了多少时间(秒)
     *
     * @return
     */
    public int liveTime() {
        return (int) (System.currentTimeMillis() - this.birthTime);
    }

    /**
     * 获取鱼的位置
     *
     * @return
     */
    public Position position() {
        return this.position(this.liveTime() / (double) 1000);
    }

    /**
     * 想对坐标(即没有加上起点的坐标)
     *
     * @param dt 路径时间秒
     * @return null, 已经游到头了
     */
    public Position position(double dt) {
        // 计算当前时间需要移动的距离
        double elapsedDistance = dt * this.speed;

        // 通过距离找到当前在2个点之间的位置
        RoutePoint lastPoint = null;
        RoutePoint nextPoint = null;
        List<RoutePoint> points = this.route.points;

        for (int i = 0; i < (points.size() - 1); i++) {
            if ((elapsedDistance >= points.get(i).getL()) && (elapsedDistance < points.get(i + 1).getL())) {

                lastPoint = points.get(i);
                nextPoint = points.get(i + 1);
                break;
            }
        }

        if (lastPoint == null) {// 路径已经运动完成
            return null;
        }

        double vectorX = nextPoint.getX() - lastPoint.getX();
        double vectorY = nextPoint.getY() - lastPoint.getY();
        // 计算采样点之间的距离
        double pointDis = Math.sqrt(Math.pow(vectorX, 2) + Math.pow(vectorY, 2));

        // 计算当前位置在采样点之间占的比例
        double curDis = elapsedDistance - lastPoint.getL();
        double rate = curDis / pointDis;

        // 通过当前位置在采样点之间的比例，计算坐标值和角度值
        double newX, newY;
        newX = lastPoint.getX() + (vectorX * rate);
        newY = lastPoint.getY() + (vectorY * rate);

        double offsetX = this.start.x - points.get(0).getX();
        double offsetY = this.start.y - points.get(0).getY();

        return new Position(newX + offsetX, newY + offsetY);
    }
}
