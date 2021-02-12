package me.jellysquid.mods.sodium.client.render.chunk.format.hfp;

import me.jellysquid.mods.sodium.client.model.vertex.buffer.VertexBufferView;
import me.jellysquid.mods.sodium.client.model.vertex.buffer.VertexBufferWriterUnsafe;
import me.jellysquid.mods.sodium.client.render.chunk.format.DefaultModelVertexFormats;
import me.jellysquid.mods.sodium.client.render.chunk.format.ModelVertexSink;
import me.jellysquid.mods.sodium.client.render.chunk.format.ModelVertexUtil;
import net.minecraft.client.texture.Sprite;

public class HFPModelVertexBufferWriterUnsafe extends VertexBufferWriterUnsafe implements ModelVertexSink {
    public HFPModelVertexBufferWriterUnsafe(VertexBufferView backingBuffer) {
        super(backingBuffer, DefaultModelVertexFormats.MODEL_VERTEX_HFP);
    }

    @Override
    public void writeQuad(float x, float y, float z, int color, float u, float v, int light, Sprite sprite) {
        this.writeQuadInternal(
                ModelVertexUtil.denormalizeFloatAsShort(x),
                ModelVertexUtil.denormalizeFloatAsShort(y),
                ModelVertexUtil.denormalizeFloatAsShort(z),
                color,
                ModelVertexUtil.denormalizeFloatAsShort(u),
                ModelVertexUtil.denormalizeFloatAsShort(v),
                ModelVertexUtil.encodeLightMapTexCoord(light)
        );
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void writeQuadInternal(short x, short y, short z, int color, short u, short v, int light) {
        long i = this.writePointer;

        UNSAFE.putShort(i, x);
        UNSAFE.putShort(i + 2, y);
        UNSAFE.putShort(i + 4, z);
        UNSAFE.putInt(i + 8, color);
        UNSAFE.putShort(i + 12, u);
        UNSAFE.putShort(i + 14, v);
        UNSAFE.putInt(i + 16, light);

        this.advance();
    }

}
