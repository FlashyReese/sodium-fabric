package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer;

import net.caffeinemc.sodium.interop.vanilla.vertex.fallback.VertexWriterFallback;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionTextureSink;
import net.minecraft.client.render.VertexConsumer;

public class PositionTextureWriterFallback extends VertexWriterFallback implements PositionTextureSink {
    public PositionTextureWriterFallback(VertexConsumer consumer) {
        super(consumer);
    }

    @Override
    public void writeVertex(float x, float y, float z, float u, float v) {
        this.consumer.vertex(x, y, z);
        this.consumer.texture(u, v);
    }
}
