package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer;

import net.caffeinemc.sodium.interop.vanilla.vertex.fallback.VertexWriterFallback;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionColorSink;
import net.caffeinemc.sodium.util.packed.ColorABGR;
import net.minecraft.client.render.VertexConsumer;

public class PositionColorWriterFallback extends VertexWriterFallback implements PositionColorSink {
    public PositionColorWriterFallback(VertexConsumer consumer) {
        super(consumer);
    }

    @Override
    public void writeQuad(float x, float y, float z, int color) {
        VertexConsumer consumer = this.consumer;
        consumer.vertex(x, y, z);
        consumer.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
        consumer.next();
    }
}