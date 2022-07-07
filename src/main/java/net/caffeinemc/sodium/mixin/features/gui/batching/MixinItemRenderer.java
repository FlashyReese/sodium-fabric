package net.caffeinemc.sodium.mixin.features.gui.batching;

import net.caffeinemc.sodium.interop.vanilla.vertex.VanillaVertexFormats;
import net.caffeinemc.sodium.interop.vanilla.vertex.formats.generic.PositionColorSink;
import net.caffeinemc.sodium.render.batching.ItemRenderBatch;
import net.caffeinemc.sodium.render.batching.ItemRendererExtended;
import net.caffeinemc.sodium.render.vertex.VertexDrain;
import net.caffeinemc.sodium.util.packed.ColorABGR;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer implements ItemRendererExtended {
    @Shadow
    public float zOffset;

    private static void writeColoredRect(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        int color = ColorABGR.pack(red, green, blue, alpha);

        PositionColorSink sink = VertexDrain.of(buffer)
                .createSink(VanillaVertexFormats.POSITION_COLOR);
        sink.ensureCapacity(4);
        sink.writeQuad(x, y, 0.0f, color);
        sink.writeQuad(x, y + height, 0.0f, color);
        sink.writeQuad(x + width, y + height, 0.0f, color);
        sink.writeQuad(x + width, y, 0.0f, color);
        sink.flush();
    }

    @Shadow
    public abstract void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model);

    @Shadow
    public abstract BakedModel getModel(ItemStack stack, @Nullable World world, @Nullable LivingEntity entity, int seed);

    @Override
    public void renderItemModel(ItemRenderBatch batch, MatrixStack matrixStack, int x, int y, ItemStack stack, LivingEntity entity, int seed) {
        if (stack.isEmpty()) {
            return;
        }

        BakedModel model = this.getModel(stack, null, entity, seed);
        this.zOffset = model.hasDepth() ? this.zOffset + 50.0F + (float) 0 : this.zOffset + 50.0F;

        try {
            this.renderGuiItemModel(stack, x, y, model, batch, matrixStack);
        } catch (Throwable e) {
            CrashReport report = CrashReport.create(e, "Rendering item");

            CrashReportSection section = report.addElement("Item being rendered");
            section.add("Item Type", () -> String.valueOf(stack.getItem()));
            section.add("Item Damage", () -> String.valueOf(stack.getDamage()));
            section.add("Item NBT", () -> String.valueOf(stack.getNbt()));
            section.add("Item Foil", () -> String.valueOf(stack.hasGlint()));

            throw new CrashException(report);
        }

        this.zOffset = model.hasDepth() ? this.zOffset - 50.0F - (float) 0 : this.zOffset - 50.0F;
    }

    protected void renderGuiItemModel(ItemStack stack, int x, int y, BakedModel model, ItemRenderBatch batcher, MatrixStack matrixStack) {
        matrixStack.push();
        matrixStack.translate(x + 8.0D, y + 8.0D, 100.0F + this.zOffset);
        matrixStack.scale(16.0F, -16.0F, 16.0F);

        this.renderItem(stack, ModelTransformation.Mode.GUI, false, matrixStack,
                batcher.getItemRendererVertexConsumer(model.isSideLit()), 0xf000f0, OverlayTexture.DEFAULT_UV, model);

        matrixStack.pop();
    }

    @Override
    public void renderItemLabel(ItemRenderBatch batch, TextRenderer textRenderer, MatrixStack matrixStack, int x, int y, ItemStack stack, String countLabel) {
        if (stack.isEmpty()) {
            return;
        }

        if (stack.getCount() != 1 || countLabel != null) {
            String label = countLabel == null ? String.valueOf(stack.getCount()) : countLabel;

            matrixStack.push();
            matrixStack.translate(0.0D, 0.0D, this.zOffset + 200.0F);

            textRenderer.draw(label, (x + 19 - 2 - textRenderer.getWidth(label)), (y + 6 + 3), 0xffffff, true,
                    matrixStack.peek().getPositionMatrix(), batch.getItemFontBuffer(), false, 0, 0xf000f0);

            matrixStack.pop();
        }
    }

    @Override
    public void renderItemOverlays(ItemRenderBatch batch, ItemStack stack, int x, int y) {
        if (stack.isEmpty()) {
            return;
        }

        if (stack.isItemBarVisible()) {
            int step = stack.getItemBarStep();
            int color = stack.getItemBarColor();

            writeColoredRect(batch.getItemOverlayBuffer(), x + 2, y + 13,
                    13, 2, 0, 0, 0, 255);

            writeColoredRect(batch.getItemOverlayBuffer(), x + 2, y + 13, step,
                    1, color >> 16 & 255, color >> 8 & 255, color & 255, 255);
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        float cooldown;

        if (player != null) {
            cooldown = player.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());
        } else {
            cooldown = 0.0F;
        }

        if (cooldown > 0.0F) {
            writeColoredRect(batch.getItemOverlayBuffer(), x, y + MathHelper.floor(16.0F * (1.0F - cooldown)),
                    16, MathHelper.ceil(16.0F * cooldown), 255, 255, 255, 127);
        }
    }
}
