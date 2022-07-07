package net.caffeinemc.sodium.interop.vanilla.vertex.formats.quad;

import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferView;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.quad.writer.ModelQuadVertexBufferWriterNio;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.quad.writer.ModelQuadVertexBufferWriterUnsafe;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.quad.writer.ModelQuadVertexWriterFallback;
import net.caffeinemc.sodium.render.vertex.type.BlittableVertexType;
import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexType;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;

public class ModelQuadVertexType implements VanillaVertexType<ModelQuadVertexSink>, BlittableVertexType<ModelQuadVertexSink> {
    @Override
    public ModelQuadVertexSink createFallbackWriter(VertexConsumer consumer) {
        return new ModelQuadVertexWriterFallback(consumer);
    }

    @Override
    public ModelQuadVertexSink createBufferWriter(VertexBufferView buffer, boolean direct) {
        return direct ? new ModelQuadVertexBufferWriterUnsafe(buffer) : new ModelQuadVertexBufferWriterNio(buffer);
    }

    @Override
    public VertexFormat getVertexFormat() {
        return ModelQuadVertexSink.VERTEX_FORMAT;
    }

    @Override
    public BlittableVertexType<ModelQuadVertexSink> asBlittable() {
        return this;
    }
}
