package me.jellysquid.mods.sodium.client.util;

public class Point2i {
    private int x;
    private int y;

    public Point2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getOriginX() {
        return this.x;
    }

    public int getOriginY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
