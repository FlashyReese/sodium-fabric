package me.jellysquid.mods.sodium.mixin.features.block;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadViewMutable;
import me.jellysquid.mods.sodium.client.model.quad.sink.ModelQuadSink;
import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
import net.minecraft.client.render.BufferBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.ByteBuffer;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder implements ModelQuadSink {
    @Shadow
    private int field_20776;

    @Shadow
    private ByteBuffer bufByte;

    @Shadow
    protected abstract void grow(int size);

    @Shadow
    private int vertexCount;

    @Override
    public void write(ModelQuadViewMutable quad) {
        this.grow(ModelQuadUtil.VERTEX_SIZE_BYTES);

        quad.copyInto(this.bufByte, this.field_20776);

        this.field_20776 += ModelQuadUtil.VERTEX_SIZE_BYTES;
        this.vertexCount += 4;
    }
}
