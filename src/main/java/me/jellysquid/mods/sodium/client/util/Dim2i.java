package me.jellysquid.mods.sodium.client.util;

public class Dim2i extends Point2i {
    private int width;
    private int height;

    public Dim2i(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLimitX() {
        return this.getX() + this.getWidth();
    }

    public int getLimitY() {
        return this.getY() + this.getHeight();
    }

    public int getCenterX() {
        return this.getX() + (this.getWidth() / 2);
    }

    public int getCenterY() {
        return this.getY() + (this.getHeight() / 2);
    }

    public boolean containsCursor(double x, double y) {
        return x >= this.getX() && x < this.getLimitX() && y >= this.getY() && y < this.getLimitY();
    }

    public boolean canFitDimension(Dim2i anotherDim) {
        return this.getX() <= anotherDim.getX() && this.getY() <= anotherDim.getY() && this.getLimitX() >= anotherDim.getLimitX() && this.getLimitY() >= anotherDim.getLimitY();
    }

    public boolean overlapWith(Dim2i other) {
        return this.getX() < other.getLimitX() && this.getLimitX() > other.getX() && this.getY() < other.getLimitY() && this.getLimitY() > other.getY();
    }
}
