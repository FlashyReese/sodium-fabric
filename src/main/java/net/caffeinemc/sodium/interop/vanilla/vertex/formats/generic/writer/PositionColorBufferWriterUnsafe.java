package net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.writer;

import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexFormats;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionColorSink;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferView;
import net.caffeinemc.sodium.render.vertex.buffer.VertexBufferWriterUnsafe;
import org.lwjgl.system.MemoryUtil;

public class PositionColorBufferWriterUnsafe extends VertexBufferWriterUnsafe implements PositionColorSink {
    public PositionColorBufferWriterUnsafe(VertexBufferView backingBuffer) {
        super(backingBuffer, VanillaVertexFormats.POSITION_COLOR);
    }

    @Override
    public void writeQuad(float x, float y, float z, int color) {
        long i = this.writePointer;

        MemoryUtil.memPutFloat(i, x);
        MemoryUtil.memPutFloat(i + 4, y);
        MemoryUtil.memPutFloat(i + 8, z);
        MemoryUtil.memPutInt(i + 12, color);

        this.advance();
    }
}
