package me.jellysquid.mods.sodium.client.render.texture;

import me.jellysquid.mods.sodium.mixin.features.texture_tracking.MixinSprite;
import net.minecraft.client.texture.Sprite;

public class SpriteUtil {
    public static void markSpriteActive(Sprite sprite) {
        if (((MixinSprite)sprite).field_28468() instanceof SpriteExtended) {
            ((SpriteExtended) ((MixinSprite)sprite).field_28468()).markActive();
        }
    }
}
