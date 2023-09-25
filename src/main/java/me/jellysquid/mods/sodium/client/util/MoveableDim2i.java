package me.jellysquid.mods.sodium.client.util;

public class MoveableDim2i extends Dim2i {
    private final Point2i pos;

    public MoveableDim2i(Point2i pos, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.pos = pos;
    }

    public Point2i getPos() {
        return pos;
    }

    @Override
    public int getX() {
        return super.getX() + this.pos.getX();
    }

    @Override
    public int getY() {
        return super.getY() + this.pos.getY();
    }
}
