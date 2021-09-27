package me.TahaCheji.playerData;

import me.TahaCheji.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LevelData {


    public static Levels getPlayerLevel(Player p) {
        File playerData = new File("plugins/BridgeWiz/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);

        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        Levels levels;

        UUID playerUUID = UUID.fromString(pD.getString("info.playerUUID"));
        Player player = Bukkit.getPlayer(playerUUID);

        levels = new Levels(Main.getInstance().getPlayer(player), pD.getInt("info.lvl"), pD.getInt("info.xp"));

        return levels;
    }

    public static void savePlayerLevel(Player p, Levels levels) throws IOException {
        File playerData = new File("plugins/BridgeWiz/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        pD.set("info.playerUUID", levels.getPlayer().getPlayer().getUniqueId().toString());
        pD.set("info.lvl", levels.getLevel());
        pD.set("info.xp", levels.getXp());
        pD.save(playerData);
    }


}
