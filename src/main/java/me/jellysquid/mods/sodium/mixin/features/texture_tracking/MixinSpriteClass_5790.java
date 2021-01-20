package me.jellysquid.mods.sodium.mixin.features.texture_tracking;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Sprite.class_5790.class)
public class MixinSpriteClass_5790 {
    @Shadow
    private int field_28471;//frameTicks;

    @Shadow
    private int field_28470;//frameIndex;
}
