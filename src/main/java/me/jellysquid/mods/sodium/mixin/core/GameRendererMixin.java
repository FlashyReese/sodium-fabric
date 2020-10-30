package me.jellysquid.mods.sodium.mixin.core;

import me.jellysquid.mods.sodium.client.render.GameRendererAccess;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererAccess {
    @Shadow
    @Final
    private LightmapTextureManager lightmapTextureManager;

    @Override
    public LightmapTextureManager getLightmapTextureManager() {
        return this.lightmapTextureManager;
    }
}
