package com.dmg.fish.business.model.route;

/**
 * 位置
 */
public class Position {
    /** x坐标 */
    public double x;
    /** y坐标 */
    public double y;

    public Position() {
    }

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public Position add(Position pos) {
        return new Position(this.x + pos.x, this.y + pos.y);
    }

    public Position add(double x, double y) {
        return new Position(this.x + x, this.y + y);
    }

}
