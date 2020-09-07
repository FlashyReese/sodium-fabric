package me.jellysquid.mods.sodium.client.render.chunk.passes.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockLayer;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import net.minecraft.util.Identifier;

public class SolidRenderPass extends BlockRenderPass {
    public SolidRenderPass(int ordinal, Identifier id, BlockLayer... layers) {
        super(ordinal, id, true, layers);
    }

    @Override
    public void beginRender() {
        //Fixme:
        //RenderLayer.SOLID.startDrawing();

        GlStateManager.enableAlphaTest();
    }

    @Override
    public void endRender() {
        GlStateManager.disableAlphaTest();

        //RenderLayer.getSolid().endDrawing();
    }
}
