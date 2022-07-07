package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer;

import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexFormats;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionTextureSink;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferView;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferWriterUnsafe;
import org.lwjgl.system.MemoryUtil;

public class PositionTextureBufferWriterUnsafe extends VertexBufferWriterUnsafe implements PositionTextureSink {
    public PositionTextureBufferWriterUnsafe(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexFormats.POSITION_TEXTURE);
    }

    @Override
    public void writeVertex(float x, float y, float z, float u, float v) {
        long i = this.writePointer;

        MemoryUtil.memPutFloat(i, x);
        MemoryUtil.memPutFloat(i + 4, y);
        MemoryUtil.memPutFloat(i + 8, z);

        MemoryUtil.memPutFloat(i + 12, u);
        MemoryUtil.memPutFloat(i + 16, v);

        this.advance();
    }
}
