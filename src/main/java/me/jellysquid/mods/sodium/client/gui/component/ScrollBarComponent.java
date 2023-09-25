package me.jellysquid.mods.sodium.client.gui.component;

import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ScrollBarComponent extends AbstractWidget {

    protected static final int SCROLL_OFFSET = 6;

    protected final Dim2i dim;

    private final Mode mode;
    private final int frameLength;
    private final int viewPortLength;
    private final int maxScrollBarOffset;
    private final Consumer<Integer> onSetOffset;
    private int offset = 0;
    private boolean isDragging;

    private Dim2i scrollThumb = null;
    private int scrollThumbClickOffset;

    private Dim2i extendedScrollArea = null;

    public ScrollBarComponent(Dim2i dim, Mode mode, int frameLength, int viewPortLength, Consumer<Integer> onSetOffset) {
        this.dim = dim;
        this.mode = mode;
        this.frameLength = frameLength;
        this.viewPortLength = viewPortLength;
        this.onSetOffset = onSetOffset;
        this.maxScrollBarOffset = this.frameLength - this.viewPortLength;
    }

    public ScrollBarComponent(Dim2i scrollBarArea, Mode mode, int frameLength, int viewPortLength, Consumer<Integer> onSetOffset, Dim2i extendedScrollAreea) {
        this(scrollBarArea, mode, frameLength, viewPortLength, onSetOffset);
        this.extendedScrollArea = extendedScrollAreea;
    }

    public void updateThumbPosition() {
        int scrollThumbLength = (this.viewPortLength * (this.mode == Mode.VERTICAL ? this.dim.getHeight() : this.dim.getWidth() - 6)) / this.frameLength;
        int maximumScrollThumbOffset = this.viewPortLength - scrollThumbLength;
        int scrollThumbOffset = this.offset * maximumScrollThumbOffset / this.maxScrollBarOffset;
        this.scrollThumb = new Dim2i(this.dim.getX() + 2 + (this.mode == Mode.HORIZONTAL ? scrollThumbOffset : 0), this.dim.getY() + 2 + (this.mode == Mode.VERTICAL ? scrollThumbOffset : 0), (this.mode == Mode.VERTICAL ? this.dim.getWidth() : scrollThumbLength) - 4, (this.mode == Mode.VERTICAL ? scrollThumbLength : this.dim.getHeight()) - 4);
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.drawBorder(drawContext, this.dim.getX(), this.dim.getY(), this.dim.getLimitX(), this.dim.getLimitY(), 0xFFAAAAAA);
        this.drawRect(drawContext, this.scrollThumb.getX(), this.scrollThumb.getY(), this.scrollThumb.getLimitX(), this.scrollThumb.getLimitY(), 0xFFAAAAAA);
        if (this.isFocused()) {
            this.drawBorder(drawContext, this.dim.getX(), this.dim.getY(), this.dim.getLimitX(), this.dim.getLimitY(), -1);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.dim.containsCursor(mouseX, mouseY)) {
            if (this.scrollThumb.containsCursor(mouseX, mouseY)) {
                if (this.mode == Mode.VERTICAL) {
                    this.scrollThumbClickOffset = (int) (mouseY - (this.scrollThumb.getY() + this.scrollThumb.getHeight() / 2));
                } else {
                    this.scrollThumbClickOffset = (int) (mouseX - (this.scrollThumb.getX() + this.scrollThumb.getWidth() / 2));
                }
                this.isDragging = true;
            } else {
                int value;
                if (this.mode == Mode.VERTICAL) {
                    value = (int) ((mouseY - this.dim.getY() - (this.scrollThumb.getHeight() / 2)) / (this.dim.getHeight() - this.scrollThumb.getHeight()) * this.maxScrollBarOffset);
                } else {
                    value = (int) ((mouseX - this.dim.getX() - (this.scrollThumb.getWidth() / 2)) / (this.dim.getWidth() - this.scrollThumb.getWidth()) * this.maxScrollBarOffset);
                }
                this.setOffset(value);
                this.isDragging = false;
            }
            return true;
        }
        this.isDragging = false;
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.isDragging = false;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.isDragging) {
            int value;
            if (this.mode == Mode.VERTICAL) {
                value = (int) ((mouseY - this.scrollThumbClickOffset - this.dim.getY() - (this.scrollThumb.getHeight() / 2)) / (this.dim.getHeight() - this.scrollThumb.getHeight()) * this.maxScrollBarOffset);
            } else {
                value = (int) ((mouseX - this.scrollThumbClickOffset - this.dim.getX() - (this.scrollThumb.getWidth() / 2)) / (this.dim.getWidth() - this.scrollThumb.getWidth()) * this.maxScrollBarOffset);
            }
            this.setOffset(value);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.dim.containsCursor(mouseX, mouseY) || this.extendedScrollArea != null && this.extendedScrollArea.containsCursor(mouseX, mouseY)) {
            if (this.offset <= this.maxScrollBarOffset && this.offset >= 0) {
                int value = (int) (this.offset - verticalAmount * SCROLL_OFFSET);
                this.setOffset(value);
                return true;
            }
        }
        return false;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getMaxScrollBarOffset() {
        return this.maxScrollBarOffset;
    }

    public void setOffset(int value) {
        this.offset = MathHelper.clamp(value, 0, this.maxScrollBarOffset);
        this.updateThumbPosition();
        this.onSetOffset.accept(this.offset);
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return new ScreenRect(this.dim.getX(), this.dim.getY(), this.dim.getWidth(), this.dim.getHeight());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.isFocused())
            return false;

        if (this.mode == Mode.VERTICAL) {
            if (keyCode == InputUtil.GLFW_KEY_UP) {
                this.setOffset(this.getOffset() - SCROLL_OFFSET);
                return true;
            } else if (keyCode == InputUtil.GLFW_KEY_DOWN) {
                this.setOffset(this.getOffset() + SCROLL_OFFSET);
                return true;
            }
        } else {
            if (keyCode == InputUtil.GLFW_KEY_LEFT) {
                this.setOffset(this.getOffset() - SCROLL_OFFSET);
                return true;
            } else if (keyCode == InputUtil.GLFW_KEY_RIGHT) {
                this.setOffset(this.getOffset() + SCROLL_OFFSET);
                return true;
            }
        }

        return false;
    }

    public enum Mode {
        HORIZONTAL,
        VERTICAL
    }
}