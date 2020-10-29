package me.jellysquid.mods.sodium.client.render.chunk.passes.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockLayer;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.util.Identifier;

public class SolidRenderPass extends BlockRenderPass {
    public SolidRenderPass(int ordinal, Identifier id, BlockLayer... layers) {
        super(ordinal, id, true, layers);
    }

    @Override
    public void beginRender() {
        BlockRenderLayer.SOLID.method_22723();

        RenderSystem.enableAlphaTest();
    }

    @Override
    public void endRender() {
        RenderSystem.disableAlphaTest();

        BlockRenderLayer.SOLID.method_22724();
    }
}
