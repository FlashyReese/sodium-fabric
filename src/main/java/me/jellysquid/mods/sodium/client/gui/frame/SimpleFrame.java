package me.jellysquid.mods.sodium.client.gui.frame;

import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.DrawContext;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleFrame extends AbstractFrame {
    protected List<Function<Dim2i, AbstractWidget>> functions;

    public SimpleFrame(Dim2i dim, List<Function<Dim2i, AbstractWidget>> functions) {
        super(dim);
        this.functions = functions;
        this.buildFrame();
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    @Override
    public void buildFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();

        this.functions.forEach(function -> this.children.add(function.apply(this.dim)));

        super.buildFrame();
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
    }

    public static class Builder {
        private final List<Function<Dim2i, AbstractWidget>> functions = new ArrayList<>();
        private Dim2i dim;

        public Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public Builder addChild(Function<Dim2i, AbstractWidget> function) {
            this.functions.add(function);
            return this;
        }

        public SimpleFrame build() {
            Validate.notNull(this.dim, "Dimension must be specified");

            return new SimpleFrame(this.dim, this.functions);
        }
    }
}