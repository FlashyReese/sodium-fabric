package me.jellysquid.mods.sodium.client.render.texture;

import me.jellysquid.mods.sodium.mixin.features.texture_tracking.MixinSprite;
import net.minecraft.client.texture.Sprite;

public class SpriteUtil {
    public static void markSpriteActive(Sprite sprite) {
        if (((MixinSprite)sprite).getAnimation() instanceof SpriteExtended) {
            ((SpriteExtended) ((MixinSprite)sprite).getAnimation()).markActive();
        }
    }
}
