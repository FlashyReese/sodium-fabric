package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer;

import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexFormats;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionTextureSink;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferView;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferWriterNio;

import java.nio.ByteBuffer;

public class PositionTextureBufferWriterNio extends VertexBufferWriterNio implements PositionTextureSink {
    public PositionTextureBufferWriterNio(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexFormats.POSITION_TEXTURE);
    }

    @Override
    public void writeVertex(float x, float y, float z, float u, float v) {
        int i = this.writeOffset;

        ByteBuffer buf = this.byteBuffer;
        buf.putFloat(i, x);
        buf.putFloat(i + 4, y);
        buf.putFloat(i + 8, z);
        buf.putFloat(i + 12, u);
        buf.putFloat(i + 16, v);

        this.advance();
    }
}
