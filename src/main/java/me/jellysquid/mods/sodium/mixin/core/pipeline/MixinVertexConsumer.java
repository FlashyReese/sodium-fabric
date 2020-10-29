package me.jellysquid.mods.sodium.mixin.core.pipeline;

import me.jellysquid.mods.sodium.client.model.consumer.GlyphVertexConsumer;
import me.jellysquid.mods.sodium.client.model.consumer.ParticleVertexConsumer;
import me.jellysquid.mods.sodium.client.model.consumer.QuadVertexConsumer;
import me.jellysquid.mods.sodium.client.util.Norm3b;
import me.jellysquid.mods.sodium.client.util.color.ColorABGR;
import net.minecraft.class_4588;
import net.minecraft.client.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_4588.class)
public interface MixinVertexConsumer extends ParticleVertexConsumer, QuadVertexConsumer, GlyphVertexConsumer {
    @Shadow
    class_4588 vertex(double x, double y, double z);

    @Shadow
    class_4588 texture(float u, float v);

    @Shadow
    class_4588 color(int red, int green, int blue, int alpha);

    @Shadow
    class_4588 method_22921(int u, int v);

    @Shadow
    class_4588 method_22914(float x, float y, float z);

    @Shadow
    void next();

    @Override
    default void vertexParticle(float x, float y, float z, float u, float v, int color, int light) {
        this.vertex(x, y, z);
        this.texture(u, v);
        this.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
        this.method_22921(light & '\uffff', light >> 16 & '\uffff');
        this.next();
    }

    @Override
    default void vertexQuad(float x, float y, float z, int color, float u, float v, int light,/* int overlay, */int normal) {
        this.vertex(x, y, z);
        this.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
        this.texture(u, v);
        //this.overlay(overlay & '\uffff', overlay >> 16 & '\uffff');
        this.method_22921(light & '\uffff', light >> 16 & '\uffff');
        this.method_22914(Norm3b.unpackX(normal), Norm3b.unpackY(normal), Norm3b.unpackZ(normal));
        this.next();
    }

    @Override
    default void vertexGlyph(Matrix4f matrix, float x, float y, float z, int color, float u, float v, int light) {
        this.vertex(x, y, z);
        this.color(ColorABGR.unpackRed(color), ColorABGR.unpackGreen(color), ColorABGR.unpackBlue(color), ColorABGR.unpackAlpha(color));
        this.texture(u, v);
        this.method_22921(light & '\uffff', light >> 16 & '\uffff');
        this.next();
    }
}
