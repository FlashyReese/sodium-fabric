package me.jellysquid.mods.sodium.client.gui.frame.tab;

import me.jellysquid.mods.sodium.client.gui.frame.AbstractFrame;

public interface TabOption<T extends AbstractFrame> {
    T getFrame();
}
