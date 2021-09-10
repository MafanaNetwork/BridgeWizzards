package me.TahaCheji.mapUtil;

import me.TahaCheji.util.FileDelete;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class LocalGameMap implements GameMap {
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;
    private boolean isLoaded;

    public LocalGameMap(File worldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(worldFolder, worldName);

        if (loadOnInit) load();
    }

    @Override
    public boolean load() {
        if(isLoaded()) return true;
        this.activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(),
                sourceWorldFolder.getName() + "_active_" +
                        System.currentTimeMillis());

        try {
            FileDelete.copyFolder(sourceWorldFolder, activeWorldFolder);
            isLoaded = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));

        if(bukkitWorld != null) {
            this.bukkitWorld.setAutoSave(false);
            System.out.println(bukkitWorld.getName());
        }
        isLoaded = true;
        return isLoaded();
    }

    @Override
    public void unload() {
        if(bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        if(activeWorldFolder != null) FileDelete.delete(activeWorldFolder);
        isLoaded = false;
        bukkitWorld = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        unload();
        return load();
    }

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

    @Override
    public String getName() {
        return sourceWorldFolder.getName();
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }
}
