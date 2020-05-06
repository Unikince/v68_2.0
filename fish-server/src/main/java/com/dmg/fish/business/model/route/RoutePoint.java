package com.dmg.fish.business.model.route;

/**
 * 路径采样点
 */
public class RoutePoint {
    private int x;
    private int y;
    private int l;
    private int r;

    public RoutePoint() {
    }

    public RoutePoint(int x, int y, int l, int r) {
        super();
        this.x = x;
        this.y = y;
        this.l = l;
        this.r = r;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getL() {
        return this.l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getR() {
        return this.r;
    }

    public void setR(int r) {
        this.r = r;
    }

}
