package me.jellysquid.mods.sodium.client.gui.frame.tab;

import me.jellysquid.mods.sodium.client.gui.component.ScrollBarComponent;
import me.jellysquid.mods.sodium.client.gui.frame.AbstractFrame;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.gui.widgets.FlatButtonWidget;
import me.jellysquid.mods.sodium.client.util.Boxed;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TabFrame extends AbstractFrame {
    private final boolean tabSectionCanScroll;
    private final Dim2i tabSection;
    private final Dim2i frameSection;
    private final List<Tab<?>> tabs = new ArrayList<>();
    private final Runnable onSetTab;
    private final Boxed<Text> tabSectionSelectedTab;
    private ScrollBarComponent tabSectionScrollBar = null;
    private Tab<?> selectedTab;
    private AbstractFrame selectedFrame;

    public TabFrame(Dim2i dim, List<Tab<?>> tabs, Runnable onSetTab, Boxed<Text> tabSectionSelectedTab, Boxed<Integer> tabSectionScrollBarOffset) {
        super(dim);
        this.tabs.addAll(tabs);
        int tabSectionY = this.tabs.size() * 18;
        this.tabSectionCanScroll = tabSectionY > this.dim.getHeight();

        Optional<Integer> result = tabs.stream().map(tab -> this.getStringWidth(tab.title())).max(Integer::compareTo);

        this.tabSection = new Dim2i(this.dim.getX(), this.dim.getY(), result.map(integer -> integer + (this.tabSectionCanScroll ? 32 : 24)).orElseGet(() -> (int) (this.dim.getWidth() * 0.35D)), this.dim.getHeight());
        this.frameSection = new Dim2i(this.tabSection.getLimitX(), this.dim.getY(), this.dim.getWidth() - this.tabSection.getWidth(), this.dim.getHeight());

        this.onSetTab = onSetTab;
        if (this.tabSectionCanScroll) {
            this.tabSectionScrollBar = new ScrollBarComponent(new Dim2i(this.tabSection.getLimitX() - 11, this.tabSection.getY(), 10, this.tabSection.getHeight()), ScrollBarComponent.Mode.VERTICAL, tabSectionY, this.dim.getHeight(), offset -> {
                this.buildFrame();
                tabSectionScrollBarOffset.set(offset);
            }, this.dim);
            this.tabSectionScrollBar.setOffset(tabSectionScrollBarOffset.get());
        }
        this.tabSectionSelectedTab = tabSectionSelectedTab;

        if (this.tabSectionSelectedTab.get() != null) {
            this.selectedTab = this.tabs.stream().filter(tab -> tab.getTitle().getString().equals(this.tabSectionSelectedTab.get().getString())).findAny().get();
        }

        this.buildFrame();

        // Let's build each frame, future note for anyone: do not move this line.
        this.tabs.stream().filter(tab -> this.selectedTab != tab).forEach(tab -> tab.getFrameFunction().apply(this.frameSection));
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public void setTab(Tab<?> tab) {
        this.selectedTab = tab;
        this.tabSectionSelectedTab.set(this.selectedTab.getTitle());
        if (this.onSetTab != null) {
            this.onSetTab.run();
        }
        this.buildFrame();
    }

    @Override
    public void buildFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();

        if (this.selectedTab == null) {
            if (!this.tabs.isEmpty()) {
                // Just use the first tab for now
                this.selectedTab = this.tabs.get(0);
            }
        }

        this.rebuildTabs();
        this.rebuildTabFrame();

        if (this.tabSectionCanScroll) {
            this.tabSectionScrollBar.updateThumbPosition();
        }

        super.buildFrame();
    }

    private void rebuildTabs() {
        int offsetY = 0;
        for (Tab<?> tab : this.tabs) {
            int x = this.tabSection.getX();
            int y = this.tabSection.getY() + offsetY - (this.tabSectionCanScroll ? this.tabSectionScrollBar.getOffset() : 0);
            int width = this.tabSection.getWidth() - (this.tabSectionCanScroll ? 12 : 4);
            int height = 18;
            Dim2i tabDim = new Dim2i(x, y, width, height);

            FlatButtonWidget button = new FlatButtonWidget(tabDim, tab.getTitle(), () -> this.setTab(tab));
            button.setSelected(this.selectedTab == tab);
            button.setCentered(false);
            this.children.add(button);

            offsetY += 18;
        }
    }

    private void rebuildTabFrame() {
        if (this.selectedTab == null) return;
        AbstractFrame frame = this.selectedTab.getFrameFunction().apply(this.frameSection);
        if (frame != null) {
            this.selectedFrame = frame;
            frame.buildFrame();
            this.children.add(frame);
        }
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        this.applyScissor(this.dim.getX(), this.dim.getY(), this.dim.getWidth(), this.dim.getHeight(), () -> {
            for (AbstractWidget widget : this.children) {
                if (widget != this.selectedFrame) {
                    widget.render(drawContext, mouseX, mouseY, delta);
                }
            }
        });
        this.selectedFrame.render(drawContext, mouseX, mouseY, delta);
        if (this.tabSectionCanScroll) {
            this.tabSectionScrollBar.render(drawContext, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return (this.dim.containsCursor(mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button)) || (this.tabSectionCanScroll && this.tabSectionScrollBar.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) || (this.tabSectionCanScroll && this.tabSectionScrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button) || (this.tabSectionCanScroll && this.tabSectionScrollBar.mouseReleased(mouseX, mouseY, button));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount) || (this.tabSectionCanScroll && this.tabSectionScrollBar.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
    }

    public static class Builder {
        private final List<Tab<?>> functions = new ArrayList<>();
        private Dim2i dim;
        private Runnable onSetTab;
        private Boxed<Text> tabSectionSelectedTab = new Boxed<>(null);
        private Boxed<Integer> tabSectionScrollBarOffset = new Boxed<>(0);

        public Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public Builder addTabs(Consumer<List<Tab<?>>> tabs) {
            tabs.accept(this.functions);
            return this;
        }

        public Builder onSetTab(Runnable onSetTab) {
            this.onSetTab = onSetTab;
            return this;
        }

        public Builder setTabSectionSelectedTab(Boxed<Text> tabSectionSelectedTab) {
            this.tabSectionSelectedTab = tabSectionSelectedTab;
            return this;
        }

        public Builder setTabSectionScrollBarOffset(Boxed<Integer> tabSectionScrollBarOffset) {
            this.tabSectionScrollBarOffset = tabSectionScrollBarOffset;
            return this;
        }

        public TabFrame build() {
            Validate.notNull(this.dim, "Dimension must be specified");

            return new TabFrame(this.dim, this.functions, this.onSetTab, this.tabSectionSelectedTab, this.tabSectionScrollBarOffset);
        }
    }
}