package me.jellysquid.mods.sodium.mixin.features.block;

import me.jellysquid.mods.sodium.client.model.quad.sink.FallbackQuadSink;
import me.jellysquid.mods.sodium.client.render.pipeline.BlockRenderer;
import me.jellysquid.mods.sodium.client.render.pipeline.context.GlobalRenderContext;
import me.jellysquid.mods.sodium.client.util.rand.XoRoShiRoRandom;
import me.jellysquid.mods.sodium.common.util.DirectionUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {
    private final XoRoShiRoRandom random = new XoRoShiRoRandom();

    @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
    private void preRenderBlockInWorld(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, BufferBuilder consumer, boolean cull, Random rand, long seed, CallbackInfoReturnable<Boolean> cir) {
        GlobalRenderContext renderer = GlobalRenderContext.getInstance(world);
        BlockRenderer blockRenderer = renderer.getBlockRenderer();

        boolean ret = blockRenderer.renderModel(world, state, pos, model, new FallbackQuadSink(consumer), cull, seed);

        cir.setReturnValue(ret);
    }

    /**
     * @reason Use optimized vertex writer intrinsics, avoid allocations
     * @author JellySquid
     */
    @Overwrite
    public void render(BlockState blockState, BakedModel bakedModel, float red, float green, float blue, float alpha) {
        XoRoShiRoRandom random = this.random;
        for (Direction direction : DirectionUtil.ALL_DIRECTIONS) {
            List<BakedQuad> quads = bakedModel.getQuads(blockState, direction, random.setSeedAndReturn(42L));

            if (!quads.isEmpty()) {
                renderQuad(red, green, blue, alpha, bakedModel.getQuads(blockState, direction, random));
            }
        }

        List<BakedQuad> quads = bakedModel.getQuads(blockState, null, random.setSeedAndReturn(42L));

        if (!quads.isEmpty()) {
            renderQuad(red, green, blue, alpha, bakedModel.getQuads(blockState, null, random));
        }
    }

    @Shadow
    abstract void renderQuad(float f, float g, float h, float i, List<BakedQuad> list);

    //Fixme:
    /*private static void renderQuad(int defaultColor, List<BakedQuad> list) {
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
    }*/
}
