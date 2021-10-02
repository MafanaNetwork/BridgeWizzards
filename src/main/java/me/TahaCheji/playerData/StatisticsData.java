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

public class StatisticsData {


    public static PlayerStatistics getPlayerStatistics(Player p) {
        File playerData = new File("plugins/BridgeWiz/playerData/" + p.getUniqueId().toString() + "/statistics.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);

        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        UUID playerUUID = UUID.fromString(pD.getString("info.playerUUID"));
        Player player = Bukkit.getPlayer(playerUUID);

        return new PlayerStatistics(Main.getInstance().getPlayer(player), pD.getInt("info.wins"), pD.getInt("info.deaths"), pD.getInt("info.kills"));
    }

    public static void savePlayerStatistics(Player p, PlayerStatistics statisticsData) throws IOException {
        File playerData = new File("plugins/BridgeWiz/playerData/" + p.getUniqueId().toString() + "/statistics.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        try {
            pD.load(playerData);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        pD.set("info.playerUUID", statisticsData.getGamePlayer().getPlayer().getUniqueId().toString());
        pD.set("info.wins", statisticsData.getWins());
        pD.set("info.deaths", statisticsData.getDeaths());
        pD.set("info.kills", statisticsData.getKills());
        pD.save(playerData);
    }


}
