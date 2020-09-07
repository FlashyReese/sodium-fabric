package me.jellysquid.mods.sodium.mixin.features.options;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    //Fixme: Somewhere else
    /*@Redirect(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;isFancyGraphicsEnabled()Z"))
    private boolean redirectGetFancyWeather() {
        return SodiumClientMod.options().quality.weatherQuality.isFancy(MinecraftClient.getInstance().options);
    }*/
}
