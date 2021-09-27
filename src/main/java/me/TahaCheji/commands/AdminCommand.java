package me.TahaCheji.commands;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.ActiveGameGui;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GameMode;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.itemData.ItemGui;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.mapUtil.LocalGameMap;
import me.TahaCheji.playerData.Levels;
import me.TahaCheji.playerData.PlayerLocation;
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
        if (label.equalsIgnoreCase("bzAdmin")) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("U no have perms");
                return true;
            }
            if (args.length == 0) {
                return true;
            }
            if (args[0].equalsIgnoreCase("cheat")) {

                if (args[1].equalsIgnoreCase("Mana")) {
                    GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                    gamePlayer.setMana(Integer.parseInt(args[2]));
                    gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + "You set your mana as " + args[2]);
                }

                if (args[1].equalsIgnoreCase("Lives")) {
                    GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                    gamePlayer.setLives(Integer.parseInt(args[2]));
                    gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + "You set your lives as " + args[2]);
                }

                if(args[1].equalsIgnoreCase("lvl")) {
                    if(args.length == 2) {
                        GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                        Levels levels = gamePlayer.getLevels();
                        levels.setLevel(Integer.parseInt(args[2]));
                        gamePlayer.setLevels(levels);
                        try {
                            levels.saveLvl();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + "You set your lvl as " + args[2]);
                        return true;
                    } else {
                        GamePlayer gamePlayer = Main.getInstance().getPlayer(Bukkit.getPlayer(args[2]));
                        Levels levels = gamePlayer.getLevels();
                        levels.setLevel(Integer.parseInt(args[3]));
                        gamePlayer.setLevels(levels);
                        try {
                            levels.saveLvl();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + "You set your lvl as " + args[3]);
                    }
                }
                if(args[1].equalsIgnoreCase("xp")) {
                    if(args.length == 2) {
                        GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                        Levels levels = gamePlayer.getLevels();
                        levels.setXp(Integer.parseInt(args[2]));
                        gamePlayer.setLevels(levels);
                        try {
                            levels.saveLvl();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + "You set your lvl xp as " + args[2]);
                        return true;
                    } else {
                        GamePlayer gamePlayer = Main.getInstance().getPlayer(Bukkit.getPlayer(args[2]));
                        Levels levels = gamePlayer.getLevels();
                        levels.setXp(Integer.parseInt(args[3]));
                        gamePlayer.setLevels(levels);
                        try {
                            levels.saveLvl();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gamePlayer.getPlayer().sendMessage(ChatColor.GOLD + "You set your lvl as " + args[3]);
                    }
                }
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (args[1].equalsIgnoreCase("Items")) {
                    new ItemGui().getAllItemsGui(Main.getInstance().getPlayer(player)).open(player);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("active")) {
                new ActiveGameGui().getGameGui().open(player);
            }
            if (args[0].equalsIgnoreCase("troll")) {
                Player trollingPlayer = Bukkit.getPlayer(args[1]);
                if (trollingPlayer == null) {
                    return true;
                }
                if (!trollingPlayer.isOnline()) {
                    return true;
                }
                //create the troll and troll the player
            }
            if (args[0].equalsIgnoreCase("stop")) {
                Game game = Main.getInstance().getGame(args[1]);
                game.stopGame();
            }
            if (args[0].equalsIgnoreCase("edit")) {
                File gameMapsFolder = new File(Main.getInstance().getDataFolder(), "maps");
                gameMap = new LocalGameMap(gameMapsFolder, args[1], false);
                if(args.length == 3 && args[2].equalsIgnoreCase("save")) {
                    GameMap newGameMap = Main.getPlayerGameMapHashMap().get(player);
                    player.teleport(Main.getInstance().getLobbyPoint());
                    newGameMap.saveMap();
                    Main.removeGameMap(player, newGameMap);
                    return true;
                }
                gameMap.load();
                player.teleport(gameMap.getWorld().getSpawnLocation());
                Main.getInstance().getPlayer(player).setPlayerLocation(PlayerLocation.GAME);
                player.setGameMode(org.bukkit.GameMode.CREATIVE);
                Main.addGameMap(player, gameMap);
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (args[1].equalsIgnoreCase("game")) {
                    if (args[2].equalsIgnoreCase("save")) {
                        game = new Game(gameName, gameIcon, gameMode, gameMana, gameLives, gameMap);
                        try {
                            game.saveGame();
                            player.sendMessage(ChatColor.GREEN + "GameBuilder" + "Great now edit the spawn in the yml!");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                    if (!(args.length >= 7)) {
                        return true;
                    }
                    gameName = args[2];
                    if (args[3].equalsIgnoreCase("gameIcon")) {
                        gameIcon = player.getItemInHand();
                    }
                    gameMode = GameMode.NORMAL;
                    gameMana = Integer.parseInt(args[4]);
                    gameLives = Integer.parseInt(args[5]);
                    File gameMapsFolder = new File(Main.getInstance().getDataFolder(), "maps");
                    gameMap = new LocalGameMap(gameMapsFolder, args[6], false);
                    player.sendMessage(ChatColor.GREEN + "GameBuilder" + "Great now all you need to do is save your game then add the spawn points! [You can do that in the yml game file]");
                }
            }
        }
        return false;
    }
}
