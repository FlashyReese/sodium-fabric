package me.jellysquid.mods.sodium.client.gui.frame;

import me.jellysquid.mods.sodium.client.gui.frame.components.ScrollBarComponent;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import me.jellysquid.mods.sodium.client.util.Point2i;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

public class ScrollFrame extends AbstractFrame {

    protected final Point2i frameOrigin;
    protected final BasicFrame frame;

    private boolean canScrollHorizontal;
    private boolean canScrollVertical;
    private Dim2i viewPortDimension = null;
    private ScrollBarComponent verticalScrollBar = null;
    private ScrollBarComponent horizontalScrollBar = null;


    public ScrollFrame(Dim2i dim, BasicFrame frame, boolean renderOutline) {
        super(dim, renderOutline);
        this.frame = frame;
        this.frameOrigin = new Point2i(frame.getDimensions().getOriginX(), frame.getDimensions().getOriginY());
        this.setupFrame();
        this.buildFrame();
    }

    public void setupFrame() {

        int maxWidth = 0;
        int maxHeight = 0;
        if (!this.dim.canFitDimension(this.frame.getDimensions())) {
            if (this.dim.getLimitX() < this.frame.getDimensions().getLimitX()) {
                int value = this.frame.getDimensions().getOriginX() - this.dim.getOriginX() + this.frame.getDimensions().getWidth();
                if (maxWidth < value) {
                    maxWidth = value;
                }
            }
            if (this.dim.getLimitY() < this.frame.getDimensions().getLimitY()) {
                int value = this.frame.getDimensions().getOriginY() - this.dim.getOriginY() + this.frame.getDimensions().getHeight();
                if (maxHeight < value) {
                    maxHeight = value;
                }
            }
        }

        if (maxWidth > 0) {
            this.canScrollHorizontal = true;
        }
        if (maxHeight > 0) {
            this.canScrollVertical = true;
        }

        if (this.canScrollHorizontal && this.canScrollVertical) {
            this.viewPortDimension = new Dim2i(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getWidth() - 11, this.dim.getHeight() - 11);
        } else if (this.canScrollHorizontal) {
            this.viewPortDimension = new Dim2i(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getWidth(), this.dim.getHeight() - 11);
            this.horizontalScrollBar = new ScrollBarComponent(new Dim2i(this.dim.getOriginX(), this.dim.getLimitY() - 10, this.dim.getWidth(), 10), ScrollBarComponent.Mode.HORIZONTAL, this.frame.getDimensions().getWidth(), this.dim.getWidth(), this::buildFrame);
        } else if (this.canScrollVertical) {
            this.viewPortDimension = new Dim2i(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getWidth() - 11, this.dim.getHeight());
        }

        if (this.canScrollHorizontal) {
            this.horizontalScrollBar = new ScrollBarComponent(new Dim2i(this.viewPortDimension.getOriginX(), this.viewPortDimension.getLimitY() + 1, this.viewPortDimension.getWidth(), 10), ScrollBarComponent.Mode.HORIZONTAL, this.frame.getDimensions().getWidth(), this.viewPortDimension.getWidth(), this::buildFrame);
        }
        if (this.canScrollVertical) {
            this.verticalScrollBar = new ScrollBarComponent(new Dim2i(this.viewPortDimension.getLimitX() + 1, this.viewPortDimension.getOriginY(), 10, this.viewPortDimension.getHeight()), ScrollBarComponent.Mode.VERTICAL, this.frame.getDimensions().getHeight(), this.viewPortDimension.getHeight(), this::buildFrame, this.viewPortDimension);
        }
    }

    @Override
    public void buildFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();

        if (this.canScrollHorizontal) {
            this.horizontalScrollBar.updateThumbPosition();
        }

        if (this.canScrollVertical) {
            this.verticalScrollBar.updateThumbPosition();
        }

        if (this.canScrollHorizontal) {
            this.frame.getDimensions().setX(this.frameOrigin.getOriginX() - this.horizontalScrollBar.getOffset());
        }

        if (this.canScrollVertical) {
            this.frame.getDimensions().setY(this.frameOrigin.getOriginY() - this.verticalScrollBar.getOffset());
        }

        this.frame.buildFrame();
        this.children.add(this.frame);
        super.buildFrame();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.canScrollHorizontal || this.canScrollVertical) {
            if (this.renderOutline) {
                this.drawRectOutline(this.dim.getOriginX(), this.dim.getOriginY(), this.dim.getLimitX(), this.dim.getLimitY(), 0xFFAAAAAA);
            }
            int scale = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
            GL11.glScissor(this.viewPortDimension.getOriginX() * scale, MinecraftClient.getInstance().getWindow().getHeight() - this.viewPortDimension.getLimitY() * scale, this.viewPortDimension.getWidth() * scale, this.viewPortDimension.getHeight() * scale);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            super.render(matrices, mouseX, mouseY, delta);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        } else {
            super.render(matrices, mouseX, mouseY, delta);
        }

        if (this.canScrollHorizontal) {
            this.horizontalScrollBar.render(matrices, mouseX, mouseY, delta);
        }

        if (this.canScrollVertical) {
            this.verticalScrollBar.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        if (this.canScrollHorizontal) {
            if (this.horizontalScrollBar.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        if (this.canScrollVertical) {
            if (this.verticalScrollBar.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        if (this.canScrollHorizontal) {
            if (this.horizontalScrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                this.horizontalScrollBar.mouseReleased(mouseX, mouseY, button);
                return true;
            }
        }
        if (this.canScrollVertical) {
            if (this.verticalScrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                this.verticalScrollBar.mouseReleased(mouseX, mouseY, button);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        if (this.canScrollHorizontal) {
            if (this.horizontalScrollBar.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        if (this.canScrollVertical) {
            if (this.verticalScrollBar.mouseReleased(mouseX, mouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (super.mouseScrolled(mouseX, mouseY, amount)) {
            return true;
        }
        if (this.canScrollHorizontal) {
            if (this.horizontalScrollBar.mouseScrolled(mouseX, mouseY, amount)) {
                return true;
            }
        }
        if (this.canScrollVertical) {
            if (this.verticalScrollBar.mouseScrolled(mouseX, mouseY, amount)) {
                return true;
            }
        }
        return false;
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private boolean renderOutline = false;
        private Dim2i dim = null;
        private BasicFrame frame = null;

        public Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public Builder shouldRenderOutline(boolean state) {
            this.renderOutline = state;
            return this;
        }

        public Builder setFrame(BasicFrame frame) {
            this.frame = frame;
            return this;
        }

        public ScrollFrame build() {
            return new ScrollFrame(this.dim, this.frame, this.renderOutline);
        }
    }
}
