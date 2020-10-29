package me.jellysquid.mods.sodium.mixin.features.block;

import me.jellysquid.mods.sodium.client.model.consumer.QuadVertexConsumer;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.sink.FallbackQuadSink;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import me.jellysquid.mods.sodium.client.render.pipeline.context.GlobalRenderContext;
import me.jellysquid.mods.sodium.client.render.texture.SpriteUtil;
import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
import me.jellysquid.mods.sodium.client.util.color.ColorABGR;
import me.jellysquid.mods.sodium.client.util.rand.XoRoShiRoRandom;
import me.jellysquid.mods.sodium.common.util.DirectionUtil;
import net.minecraft.block.BlockState;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(BlockModelRenderer.class)
public class MixinBlockModelRenderer {
    private final XoRoShiRoRandom random = new XoRoShiRoRandom();

    @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
    private void preRenderBlockInWorld(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, class_4587 matrixStack, class_4588 consumer, boolean cull, Random rand, long seed, CallbackInfoReturnable<Boolean> cir) {
        GlobalRenderContext renderer = GlobalRenderContext.getInstance(world);
        BlockRenderer blockRenderer = renderer.getBlockRenderer();

        boolean ret = blockRenderer.renderModel(world, state, pos, model, new FallbackQuadSink(consumer, matrixStack), cull, seed);

        cir.setReturnValue(ret);
    }

    /**
     * @reason Use optimized vertex writer intrinsics, avoid allocations
     * @author JellySquid
     */
    @Overwrite
    public void render(Matrix4f matrix4f, class_4588 vertexConsumer, BlockState blockState, BakedModel bakedModel, float red, float green, float blue, int light) {
        XoRoShiRoRandom random = this.random;
        QuadVertexConsumer quadConsumer = (QuadVertexConsumer) vertexConsumer;

        // Clamp color ranges
        red = MathHelper.clamp(red, 0.0F, 1.0F);
        green = MathHelper.clamp(green, 0.0F, 1.0F);
        blue = MathHelper.clamp(blue, 0.0F, 1.0F);

        int defaultColor = ColorABGR.pack(red, green, blue, 1.0F);

        for (Direction direction : DirectionUtil.ALL_DIRECTIONS) {
            List<BakedQuad> quads = bakedModel.getQuads(blockState, direction, random.setSeedAndReturn(42L));

            if (!quads.isEmpty()) {
                renderQuad(matrix4f, quadConsumer, defaultColor, quads, light);
            }
        }

        List<BakedQuad> quads = bakedModel.getQuads(blockState, null, random.setSeedAndReturn(42L));

        if (!quads.isEmpty()) {
            renderQuad(matrix4f, quadConsumer, defaultColor, quads, light);
        }
    }

    private static void renderQuad(Matrix4f matrix4f, QuadVertexConsumer vertices, int defaultColor, List<BakedQuad> list, int light) {
        if (list.isEmpty()) {
            return;
        }

        for (BakedQuad bakedQuad : list) {
            int color = bakedQuad.hasColor() ? defaultColor : 0xFFFFFFFF;

            ModelQuadView quad = ((ModelQuadView) bakedQuad);

            for (int i = 0; i < 4; i++) {
                vertices.vertexQuad(matrix4f, quad.getX(i), quad.getY(i), quad.getZ(i), color, quad.getTexU(i), quad.getTexV(i),
                        light, ModelQuadUtil.getFacingNormal(bakedQuad.getFace()));
            }

            SpriteUtil.markSpriteActive(quad.getSprite());
        }
    }
}
