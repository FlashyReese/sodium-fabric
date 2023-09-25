package me.jellysquid.mods.sodium.client.gui.frame;

import me.jellysquid.mods.sodium.client.gui.component.ScrollBarComponent;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.util.Boxed;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import me.jellysquid.mods.sodium.client.util.Point2i;
import net.minecraft.client.gui.DrawContext;

public class ScrollableFrame extends AbstractFrame {
    protected final Point2i frameOrigin;
    protected final AbstractFrame frame;

    private boolean canScrollHorizontal;
    private boolean canScrollVertical;
    private Dim2i viewPortDimension = null;
    private ScrollBarComponent verticalScrollBar = null;
    private ScrollBarComponent horizontalScrollBar = null;

    public ScrollableFrame(Dim2i dim, AbstractFrame frame, Boxed<Integer> verticalScrollBarOffset, Boxed<Integer> horizontalScrollBarOffset) {
        super(dim);
        this.frame = frame;
        this.frameOrigin = new Point2i(frame.dim.getX(), frame.dim.getY());
        this.setupFrame(verticalScrollBarOffset, horizontalScrollBarOffset);
        this.buildFrame();
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public void setupFrame(Boxed<Integer> verticalScrollBarOffset, Boxed<Integer> horizontalScrollBarOffset) {
        // Check if the frame is larger than the scrollable frame then mark it as scrollable
        int maxWidth = 0;
        int maxHeight = 0;
        if (!this.dim.canFitDimension(this.frame.dim)) {
            if (this.dim.getLimitX() < this.frame.dim.getLimitX()) {
                int value = this.frame.dim.getX() - this.dim.getX() + this.frame.dim.getWidth();
                if (maxWidth < value) {
                    maxWidth = value;
                }
            }
            if (this.dim.getLimitY() < this.frame.dim.getLimitY()) {
                int value = this.frame.dim.getY() - this.dim.getY() + this.frame.dim.getHeight();
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
            this.viewPortDimension = new Dim2i(this.dim.getX(), this.dim.getY(), this.dim.getWidth() - 11, this.dim.getHeight() - 11);
        } else if (this.canScrollHorizontal) {
            this.viewPortDimension = new Dim2i(this.dim.getX(), this.dim.getY(), this.dim.getWidth(), this.dim.getHeight() - 11);
            this.frame.dim.setHeight(this.frame.dim.getHeight() - 11);
            this.frame.buildFrame();
        } else if (this.canScrollVertical) {
            this.viewPortDimension = new Dim2i(this.dim.getX(), this.dim.getY(), this.dim.getWidth() - 11, this.dim.getHeight());
            this.frame.dim.setWidth(this.frame.dim.getWidth() - 11);
            this.frame.buildFrame();
        }

        if (this.canScrollHorizontal) {
            this.horizontalScrollBar = new ScrollBarComponent(new Dim2i(this.viewPortDimension.getX(), this.viewPortDimension.getLimitY() + 1, this.viewPortDimension.getWidth(), 10), ScrollBarComponent.Mode.HORIZONTAL, this.frame.dim.getWidth(), this.viewPortDimension.getWidth(), offset -> {
                this.frame.dim.setX(this.frameOrigin.getX() - this.horizontalScrollBar.getOffset());
                horizontalScrollBarOffset.set(offset);
            });
            this.horizontalScrollBar.setOffset(horizontalScrollBarOffset.get());
        }
        if (this.canScrollVertical) {
            this.verticalScrollBar = new ScrollBarComponent(new Dim2i(this.viewPortDimension.getLimitX() + 1, this.viewPortDimension.getY(), 10, this.viewPortDimension.getHeight()), ScrollBarComponent.Mode.VERTICAL, this.frame.dim.getHeight(), this.viewPortDimension.getHeight(), offset -> {
                this.frame.dim.setY(this.frameOrigin.getY() - this.verticalScrollBar.getOffset());
                verticalScrollBarOffset.set(offset);
            }, this.viewPortDimension);
            this.verticalScrollBar.setOffset(verticalScrollBarOffset.get());
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
            this.frame.dim.setX(this.frameOrigin.getX() - this.horizontalScrollBar.getOffset());
            this.children.add(this.horizontalScrollBar);
        }

        if (this.canScrollVertical) {
            this.frame.dim.setY(this.frameOrigin.getY() - this.verticalScrollBar.getOffset());
            this.children.add(this.verticalScrollBar);
        }

        this.frame.buildFrame();
        this.children.add(this.frame);
        super.buildFrame();
        // fixme: Ridiculous hack to snap to focused element
        // for the meanwhile this works until a proper solution is implemented.
        // this shouldn't be hardcoded into scrollable frame
        this.frame.registerFocusChangeCallback(element -> {
            if (element instanceof ControlElement<?> controlElement && this.canScrollVertical) {
                Dim2i dim = controlElement.getDimensions();
                int inputOffset = this.verticalScrollBar.getOffset();
                if (dim.getY() <= this.viewPortDimension.getY()) {
                    inputOffset += dim.getY() - this.viewPortDimension.getY();
                } else if (dim.getLimitY() >= this.viewPortDimension.getLimitY()) {
                    inputOffset += dim.getLimitY() - this.viewPortDimension.getLimitY();
                }
                this.verticalScrollBar.setOffset(inputOffset);
            }
        });
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        if (this.canScrollHorizontal || this.canScrollVertical) {
            this.applyScissor(this.viewPortDimension.getX(), this.viewPortDimension.getY(), this.viewPortDimension.getWidth(), this.viewPortDimension.getHeight(), () -> super.render(drawContext, mouseX, mouseY, delta));
        } else {
            super.render(drawContext, mouseX, mouseY, delta);
        }

        if (this.canScrollHorizontal) {
            this.horizontalScrollBar.render(drawContext, mouseX, mouseY, delta);
        }

        if (this.canScrollVertical) {
            this.verticalScrollBar.render(drawContext, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button) || (this.canScrollHorizontal && this.horizontalScrollBar.mouseClicked(mouseX, mouseY, button)) || (this.canScrollVertical && this.verticalScrollBar.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) || (this.canScrollHorizontal && this.horizontalScrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) || (this.canScrollVertical && this.verticalScrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button) || (this.canScrollHorizontal && this.horizontalScrollBar.mouseReleased(mouseX, mouseY, button)) || (this.canScrollVertical && this.verticalScrollBar.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) || (this.canScrollHorizontal && this.horizontalScrollBar.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)) || (this.canScrollVertical && this.verticalScrollBar.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
    }

    public static class Builder {
        private Dim2i dim = null;
        private AbstractFrame frame = null;
        private Boxed<Integer> verticalScrollBarOffset = new Boxed<>(0);
        private Boxed<Integer> horizontalScrollBarOffset = new Boxed<>(0);

        public Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public Builder setVerticalScrollBarOffset(Boxed<Integer> verticalScrollBarOffset) {
            this.verticalScrollBarOffset = verticalScrollBarOffset;
            return this;
        }

        public Builder setHorizontalScrollBarOffset(Boxed<Integer> horizontalScrollBarOffset) {
            this.horizontalScrollBarOffset = horizontalScrollBarOffset;
            return this;
        }

        public Builder setFrame(AbstractFrame frame) {
            this.frame = frame;
            return this;
        }

        public ScrollableFrame build() {
            return new ScrollableFrame(this.dim, this.frame, this.verticalScrollBarOffset, this.horizontalScrollBarOffset);
        }
    }
}