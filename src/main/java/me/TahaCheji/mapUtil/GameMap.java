package me.TahaCheji.mapUtil;

import org.bukkit.World;

public interface GameMap {
    boolean load();
    void unload();
    boolean restoreFromSource();
    void saveMap();

    boolean isLoaded();
    World getWorld();
    String getName();
}
