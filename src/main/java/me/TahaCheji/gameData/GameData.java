package me.TahaCheji.gameData;

import me.TahaCheji.Main;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.mapUtil.LocalGameMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameData {

    public static void saveGame(Game game) throws IOException {
        File gameData = new File(Main.getInstance().getDataFolder(), "games/" + game.getName() + ".yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(gameData);
        if (!gameData.exists()) {
            gameData.createNewFile();
            pD.set("data.gameName", game.getName());
            pD.set("data.gameIcon", game.getGameIcon());
            pD.set("data.gameMode", game.getGameMode().toString());
            pD.set("data.gameMana", game.getMana());
            pD.set("data.gameLives", game.getLives());
            pD.set("data.gameMap", game.getMap().getName());
            pD.set("p1Spawn.x", 0);
            pD.set("p1Spawn.y", 0);
            pD.set("p1Spawn.z", 0);

            pD.set("p2Spawn.x", 0);
            pD.set("p2Spawn.y", 0);
            pD.set("p2Spawn.z", 0);

            pD.set("lobby.x", 0);
            pD.set("lobby.y", 0);
            pD.set("lobby.z", 0);
            pD.save(gameData);
        }
    }

    public static List<Game> getAllSavedGames() {
        List<Game> arrayList = new ArrayList<>();
        File dataFolder = new File(Main.getInstance().getDataFolder(), "games");
        File[] files = dataFolder.listFiles();
        for (File file : files) {
            FileConfiguration pD = YamlConfiguration.loadConfiguration(file);
            String gameName = pD.getString("data.gameName");
            ItemStack material = pD.getItemStack("data.gameIcon");
            //gameMode
            int gameMana = pD.getInt("data.gameMana");
            int gameLives = pD.getInt("data.gameLives");
            File gameMapsFolder = new File(Main.getInstance().getDataFolder(), "maps");
            GameMap gameMap = new LocalGameMap(gameMapsFolder, pD.getString("data.gameMap"), false);
            Game game = new Game(gameName, material, GameMode.NORMAL, gameMana, gameLives, gameMap);
            Location p1Location = new Location(Bukkit.getWorld("world"), pD.getInt("p1Spawn.x"), pD.getInt("p1Spawn.y"), pD.getInt("p1Spawn.z"));
            Location p2Location = new Location(Bukkit.getWorld("world"), pD.getInt("p2Spawn.x"), pD.getInt("p2Spawn.y"), pD.getInt("p2Spawn.z"));
            Location lobbySpawn = new Location(Bukkit.getWorld("world"), pD.getInt("lobby.x"), pD.getInt("lobby.y"), pD.getInt("lobby.z"));
            game.setLobbySpawn(lobbySpawn);
            game.setP1Location(p1Location);
            game.setP2Location(p2Location);
            arrayList.add(game);
        }
        return arrayList;
    }

    public static Game getGame(String name) {
        Game getGame = null;
        for (Game game : getAllSavedGames()) {
            if (game.getName().contains(name)) {
                getGame = game;
            }
        }
        return getGame;
    }

    public static void removeGame(String gameName) {
        File dataFolder = new File(Main.getInstance().getDataFolder(), "games");
        File[] files = dataFolder.listFiles();
        for (File file : files) {
            if (file.getName().contains(gameName)) {
                file.delete();
            }
        }
    }


}
