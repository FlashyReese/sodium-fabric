package me.jellysquid.mods.sodium.mixin.features.chunk_rendering;

import me.jellysquid.mods.sodium.client.world.ClientWorldExtended;
import me.jellysquid.mods.sodium.client.world.SodiumChunkManager;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld implements ClientWorldExtended {
    private long biomeSeed;

    /**
     * Captures the biome generation seed so that our own caches can make use of it.
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientPlayNetworkHandler clientPlayNetworkHandler, ClientWorld.class_5271 levelInfo, DimensionType dimensionType, int chunkLoadDistance, Supplier<Profiler> supplier, WorldRenderer worldRenderer, CallbackInfo ci) {
        this.biomeSeed = levelInfo.getSeed();
    }

    /**
     * Replace the client world chunk manager with our own implementation that is both faster and contains additional
     * features needed to pull off event-based rendering.
     */
    @Dynamic
    @Redirect(method = "method_2940", at = @At(value = "NEW", target = "net/minecraft/client/world/ClientChunkManager"))
    private static ClientChunkManager redirectCreateChunkManager(ClientWorld world, int renderDistance) {
        return new SodiumChunkManager(world, renderDistance);
    }

    @Override
    public long getBiomeSeed() {
        return this.biomeSeed;
    }
}
