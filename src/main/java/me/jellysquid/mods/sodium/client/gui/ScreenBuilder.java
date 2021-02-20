package me.jellysquid.mods.sodium.client.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ScreenBuilder extends Screen {
    private final Screen previousScreen;

    public ScreenBuilder(Text title, Screen previousScreen) {
        super(title);
        this.previousScreen = previousScreen;
    }
}
