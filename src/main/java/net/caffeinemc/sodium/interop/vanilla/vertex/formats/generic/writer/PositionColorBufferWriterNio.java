package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer;

import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexFormats;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionColorSink;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferView;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferWriterNio;

import java.nio.ByteBuffer;

public class PositionColorBufferWriterNio extends VertexBufferWriterNio implements PositionColorSink {
    public PositionColorBufferWriterNio(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexFormats.POSITION_COLOR);
    }

    @Override
    public void writeQuad(float x, float y, float z, int color) {
        int i = this.writeOffset;

        ByteBuffer buf = this.byteBuffer;
        buf.putFloat(i, x);
        buf.putFloat(i + 4, y);
        buf.putFloat(i + 8, z);
        buf.putInt(i + 12, color);

        this.advance();
    }
}
