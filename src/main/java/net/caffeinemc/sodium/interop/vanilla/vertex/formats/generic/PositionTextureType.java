package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic;

import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexType;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer.PositionTextureBufferWriterNio;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer.PositionTextureBufferWriterUnsafe;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer.PositionTextureWriterFallback;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferView;
import net.caffeinemc.sodium.render.vertex.type.BlittableVertexType;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;

public class PositionTextureType implements VanillaVertexType<PositionTextureSink>, BlittableVertexType<PositionTextureSink> {
    @Override
    public PositionTextureSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new PositionTextureBufferWriterUnsafe(buffer) : new PositionTextureBufferWriterNio(buffer);
    }

    @Override
    public PositionTextureSink createFallbackWriter(VertexConsumer consumer) {
        return new PositionTextureWriterFallback(consumer);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return PositionTextureSink.VERTEX_FORMAT;
    }

    @Override
    public BlittableVertexType<PositionTextureSink> asBlittable() {
        return this;
    }
}
