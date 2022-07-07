package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic;

import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer.*;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferView;
import net.caffeinemc.sodium.render.vertex.type.BlittableVertexType;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;

public class PositionColorType implements VanillaVertexType<PositionColorSink>, BlittableVertexType<PositionColorSink> {
    @Override
    public PositionColorSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new PositionColorBufferWriterUnsafe(buffer) : new PositionColorBufferWriterNio(buffer);
    }

    @Override
    public PositionColorSink createFallbackWriter(VertexConsumer consumer) {
        return new PositionColorWriterFallback(consumer);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return PositionTextureSink.VERTEX_FORMAT;
    }

    @Override
    public BlittableVertexType<PositionColorSink> asBlittable() {
        return this;
    }
}
