package me.jellysquid.mods.sodium.client.render.chunk.passes.impl;

import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockLayer;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.util.Identifier;

public class TranslucentRenderPass extends BlockRenderPass {
    public TranslucentRenderPass(int ordinal, Identifier id, BlockLayer... layers) {
        super(ordinal, id, false, layers);
    }

    @Override
    public void beginRender() {
        BlockRenderLayer.field_9179.method_22723();
    }

    @Override
    public void endRender() {
        BlockRenderLayer.field_9179.method_22724();
    }
}
