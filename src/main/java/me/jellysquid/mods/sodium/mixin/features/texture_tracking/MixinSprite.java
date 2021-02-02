package me.jellysquid.mods.sodium.mixin.features.texture_tracking;

import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Sprite.class)
public interface MixinSprite {
    @Accessor("field_28468")
    Sprite.class_5790 field_28468();
}
