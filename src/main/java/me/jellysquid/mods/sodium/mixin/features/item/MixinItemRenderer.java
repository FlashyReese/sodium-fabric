package me.jellysquid.mods.sodium.mixin.features.item;

import me.jellysquid.mods.sodium.client.util.rand.XoRoShiRoRandom;
import me.jellysquid.mods.sodium.common.util.DirectionUtil;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    private final XoRoShiRoRandom random = new XoRoShiRoRandom();

    @Shadow
    @Final
    private ItemColors colorMap;

    /**
     * @reason Avoid allocations
     * @author JellySquid
     */
    @Overwrite
    private void renderModel(BakedModel model, int light, ItemStack stack) {
        XoRoShiRoRandom random = this.random;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
        bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_UV_NORMAL);
        for (Direction direction : DirectionUtil.ALL_DIRECTIONS) {
            List<BakedQuad> quads = model.getQuads(null, direction, random.setSeedAndReturn(42L));

            if (!quads.isEmpty()) {
                this.renderQuads(bufferBuilder, model.getQuads(null, direction, random), light, stack);
            }
        }

        List<BakedQuad> quads = model.getQuads(null, null, random.setSeedAndReturn(42L));

        if (!quads.isEmpty()) {
            this.renderQuads(bufferBuilder, model.getQuads(null, null, random), light, stack);
        }
    }

    abstract void renderQuads(BufferBuilder bufferBuilder, List<BakedQuad> list, int i, ItemStack itemStack);

    /**
     * @reason Use vertex building intrinsics
     * @author JellySquid
     */
    /*@Overwrite
    private void method_23180(class_4587 matrices, class_4588 vertices, List<BakedQuad> quads, ItemStack stack, int light) {

        QuadVertexConsumer consumer = (QuadVertexConsumer) vertices;
        ItemColorProvider colorProvider = null;

        for (BakedQuad bakedQuad : quads) {
            int color = 0xFFFFFFFF;

            if (!stack.isEmpty() && bakedQuad.hasColor()) {
                if (colorProvider == null) {
                    colorProvider = ((ItemColorsExtended) this.colorMap).getColorProvider(stack);
                }

                color = ColorARGB.toABGR((colorProvider.getColor(stack, bakedQuad.getColorIndex())), 255);
            }

            ModelQuadView quad = ((ModelQuadView) bakedQuad);

            for (int i = 0; i < 4; i++) {
                consumer.vertexQuad(matrices.method_22910(), quad.getX(i), quad.getY(i), quad.getZ(i), color, quad.getTexU(i), quad.getTexV(i),
                        light, ModelQuadUtil.getFacingNormal(bakedQuad.getFace()));
            }

            SpriteUtil.markSpriteActive(quad.getSprite());
        }
    }*/
}
