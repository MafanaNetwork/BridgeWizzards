package me.TahaCheji.data;

import me.TahaCheji.Main;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.mapUtil.LocalGameMap;
import org.bukkit.Location;
import org.bukkit.Material;
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
            pD.save(gameData);
        }
        pD.set("data.p1Location", game.getP1Location());
        pD.set("data.p2Location", game.getP2Location());
        pD.save(gameData);
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
            Game game = new Game(gameName, material ,GameMode.NORMAL, gameMana, gameLives, gameMap);
            if(pD.contains("data.p1Location") && pD.contains("data.p1Location")) {
                Location p1Location = pD.getLocation("data.p1Location");
                Location p2Location = pD.getLocation("data.p2Location");
                game.setP1Location(p1Location);
                game.setP2Location(p2Location);
            }
            arrayList.add(game);
        }
        return arrayList;
    }

    public static Game getGame(String name) {
        Game getGame = null;
        for(Game game : getAllSavedGames()) {
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
