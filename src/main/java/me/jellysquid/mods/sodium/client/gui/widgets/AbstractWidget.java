package me.jellysquid.mods.sodium.client.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

public abstract class AbstractWidget implements Drawable, Element {
    protected final TextRenderer font;

    protected final BufferRenderer bufferRenderer;

    protected AbstractWidget() {
        this.font = MinecraftClient.getInstance().textRenderer;
        bufferRenderer = new BufferRenderer();
    }

    protected void drawString(String str, int x, int y, int color) {
        this.font.draw(str, x, y, color);
    }

    protected void drawRect(int x1, int y1, int x2, int y2, int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        this.drawQuads(vertices -> addQuad(vertices, x1, y1, x2, y2, a, r, g, b));
    }

    protected void drawQuads(Consumer<BufferBuilder> consumer) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture();
        //GlStateManager.defaultBlendFunc();//Todo: Fixme

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR);

        consumer.accept(bufferBuilder);

        bufferBuilder.end();

        bufferRenderer.draw(bufferBuilder);
        GlStateManager.enableTexture();
        GlStateManager.disableBlend();
    }

    protected static void addQuad(BufferBuilder consumer, int x1, int y1, int x2, int y2, float a, float r, float g, float b) {
        consumer.vertex(x2, y1, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x1, y1, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x1, y2, 0.0D).color(r, g, b, a).next();
        consumer.vertex(x2, y2, 0.0D).color(r, g, b, a).next();
    }

    protected void playClickSound() {
        MinecraftClient.getInstance().getSoundManager()
                .play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    protected int getStringWidth(String text) {
        return this.font.getStringWidth(text);
    }
}
