package me.TahaCheji.commands;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GameData;
import me.TahaCheji.data.GameMode;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.mapUtil.LocalGameMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class AdminCommand implements CommandExecutor {


    String gameName = null;
    ItemStack gameIcon = null;
    GameMode gameMode = null;
    int gameMana = 0;
    int gameLives = 0;
    GameMap gameMap;

    Game game;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("BridgeAdminWizzards")) {
            Player player = (Player) sender;
            if(args[0].equalsIgnoreCase("edit")) {
                game = GameData.getGame(args[1]);
                gameMap = game.getMap();
                if(game.getP1Location() == null && game.getP2Location() == null && args.length == 2) {
                    player.sendMessage(ChatColor.GREEN + "GameBuilder" + "You need to set the players spawn points");
                    gameMap.load();
                    player.sendMessage(ChatColor.GREEN + "GameBuilder" + "Go to the spawn location the do the command /BridgeAdminWizzards edit " + game.getName() + " setSpawn");
                    return true;
                }
                if(args[2].equalsIgnoreCase("teleport")) {
                    player.teleport(gameMap.getWorld().getSpawnLocation());
                    return true;
                }
                if(args[2].equalsIgnoreCase("done")) {
                    player.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));
                    gameMap.unload();
                    try {
                        game.saveGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(args[2].equalsIgnoreCase("setP1Spawn")) {
                    game.setP1Location(player.getLocation());
                }
                if(args[2].equalsIgnoreCase("setP2Spawn")) {
                    game.setP2Location(player.getLocation());
                }
            }
            if(args[0].equalsIgnoreCase("create")) {
                if(args[1].equalsIgnoreCase("game")) {
                    if(args[2].equalsIgnoreCase("save")) {
                        game = new Game(gameName, gameIcon, gameMode, gameMana, gameLives, gameMap);
                        try {
                            game.saveGame();
                            player.sendMessage(ChatColor.GREEN + "GameBuilder" + "Great on to the next step!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    if(!(args.length >= 7)) {
                        return true;
                    }
                    gameName = args[2];
                    if(args[3].equalsIgnoreCase("gameIcon")) {
                        gameIcon = player.getItemInHand();
                    }
                    gameMode = GameMode.NORMAL;
                    gameMana =  Integer.parseInt(args[4]);
                    gameLives =  Integer.parseInt(args[5]);
                    File gameMapsFolder = new File(Main.getInstance().getDataFolder(), "maps");
                    gameMap = new LocalGameMap(gameMapsFolder, args[6], false);
                    player.sendMessage(ChatColor.GREEN + "GameBuilder" + "Great now all you need to do is save your game then add the spawn points!");
                    }
                }
            }
        return false;
    }
}
