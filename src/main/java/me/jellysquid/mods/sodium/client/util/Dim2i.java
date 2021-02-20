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
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLimitX() {
        return this.getOriginX() + this.width;
    }

    public int getLimitY() {
        return this.getOriginY() + this.height;
    }

    public boolean containsCursor(double x, double y) {
        return x >= this.getOriginX() && x < this.getLimitX() && y >= this.getOriginY() && y < this.getLimitY();
    }

    public int getCenterX() {
        return this.getOriginX() + (this.width / 2);
    }

    public int getCenterY() {
        return this.getOriginY() + (this.height / 2);
    }

    public boolean canFitDimension(Dim2i anotherDim){
        if (this.getOriginX() <= anotherDim.getOriginX() && this.getOriginY() <= anotherDim.getOriginY() &&
                this.getLimitX() >= anotherDim.getLimitX() && this.getLimitY() >= anotherDim.getLimitY()){
            return true;
        }
        return false;
    }
}
