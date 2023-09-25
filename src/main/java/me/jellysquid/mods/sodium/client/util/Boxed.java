package me.jellysquid.mods.sodium.client.util;

public class Boxed<V> {
    private V value;

    public Boxed(V initialValue) {
        value = initialValue;
    }

    public V get() {
        return value;
    }

    public void set(V newValue) {
        value = newValue;
    }
}