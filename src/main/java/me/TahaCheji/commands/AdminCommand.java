package me.TahaCheji.commands;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GameData;
import me.TahaCheji.data.GameMode;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.gameItems.GGun;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.mapUtil.LocalGameMap;
import org.bukkit.*;
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
        if(label.equalsIgnoreCase("bzAdmin")) {
            Player player = (Player) sender;
            if(!player.isOp()) {
                player.sendMessage("U no have perms");
                return true;
            }
            if(args.length == 0) {
                return true;
            }
            if(args[0].equalsIgnoreCase("cheat")) {
                if(args[1].equalsIgnoreCase("Mana")) {
                    GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                    gamePlayer.setMana(Integer.parseInt(args[2]));
                }
            }
            if(args[0].equalsIgnoreCase("give")) {
                player.getInventory().addItem(new GGun().getItem());
                return true;
            }
            if(args[0].equalsIgnoreCase("active")) {
                //open up a score board for the active games
                if(args[1].equalsIgnoreCase("silent")) {
                    //every time you join the active game the players wont know
                }
            }
            if(args[0].equalsIgnoreCase("troll")) {
                Player trollingPlayer = Bukkit.getPlayer(args[1]);
                if(trollingPlayer == null) {
                    return true;
                }
                if(!trollingPlayer.isOnline()) {
                    return true;
                }
                //create the troll and troll the player
            }
            if(args[0].equalsIgnoreCase("stop")) {
                Game game = Main.getInstance().getGame(args[1]);
                game.stopGame();
            }
            if(args[0].equalsIgnoreCase("edit")) {
                if(args[2].equalsIgnoreCase("stop")) {
                    player.teleport(Main.getInstance().getLobbyPoint());
                    gameMap.unload();
                }
                File gameMapsFolder = new File(Main.getInstance().getDataFolder(), "maps");
                GameMap gameMap = new LocalGameMap(gameMapsFolder, args[1], true);
                player.teleport(gameMap.getWorld().getSpawnLocation());
            }
            if(args[0].equalsIgnoreCase("create")) {
                if(args[1].equalsIgnoreCase("game")) {
                    if(args[2].equalsIgnoreCase("save")) {
                        game = new Game(gameName, gameIcon, gameMode, gameMana, gameLives, gameMap);
                        try {
                            game.saveGame();
                            player.sendMessage(ChatColor.GREEN + "GameBuilder" + "Great now edit the spawn in the yml!");
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
                    player.sendMessage(ChatColor.GREEN + "GameBuilder" + "Great now all you need to do is save your game then add the spawn points! [You can do that in the yml game file]");
                    }
                }
            }
        return false;
    }
}
