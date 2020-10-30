package me.jellysquid.mods.sodium.mixin.features.buffer_builder.fast_advance;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder{
    @Shadow
    private VertexFormat format;

    @Shadow
    private VertexFormatElement currentElement;

    @Shadow
    private int field_20776;

    @Shadow
    private int currentElementId;

    //Fixme: currently disabled in Mixin java.lang.IllegalStateException: Not filled all elements of the vertex
    /**
     * @author JellySquid
     * @reason Remove modulo operations and recursion
     */
    @Overwrite
    public void nextElement() {
        List<VertexFormatElement> elements = this.format.getElements();

        do {
            this.field_20776 += this.currentElement.getSize();

            // Wrap around the element pointer without using modulo
            if (++this.currentElementId >= elements.size()) {
                this.currentElementId -= elements.size();
            }

            this.currentElement = elements.get(this.currentElementId);
        } while (this.currentElement.getType() == VertexFormatElement.Type.PADDING);
    }
}
