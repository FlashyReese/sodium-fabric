package me.jellysquid.mods.sodium.mixin.features.chunk_rendering;

import net.minecraft.client.render.chunk.ChunkBatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChunkBatcher.class)
public class MixinChunkBuilder {
    //Shouldn't break but it's broken
    @ModifyVariable(method = "<init>", index = 8, at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayListWithExpectedSize(I)Ljava/util/ArrayList;", remap = false))
    private int modifyThreadPoolSize(int prev) {
        // Do not allow any resources to be allocated
        return 0;
    }
}
