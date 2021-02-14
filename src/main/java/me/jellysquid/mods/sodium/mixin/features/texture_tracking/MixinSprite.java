package me.jellysquid.mods.sodium.mixin.features.texture_tracking;

import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Sprite.class)
public interface MixinSprite {
    @Accessor("animation")
    Sprite.Animation getAnimation();

    @Accessor("images")
    NativeImage[] getImages();
}
