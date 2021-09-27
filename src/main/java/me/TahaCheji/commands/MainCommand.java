package me.TahaCheji.commands;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GameGui;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.playerData.PlayerLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("bz")) {
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("join")) {
                if (args.length == 1) {
                    new GameGui().getGameGui().open(player);
                    return true;
                }
                Game game = Main.getInstance().getGame(args[1]);
                if (game == null) {
                    player.sendMessage("That Game does not exist");
                    return true;
                }
                GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                if (Main.getInstance().isInGame(player)) {
                    player.sendMessage("You are already in a game");
                    return true;
                }
                gamePlayer.setGame(game);
                game.joinGame(gamePlayer);
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (Main.getInstance().isInGame(player)) {
                    Game game = Main.getInstance().getGame(player);
                    try {
                        game.leaveGame(Main.getInstance().getPlayer(player));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("hub")) {
                if (Main.getInstance().isInGame(player)) {
                    Game game = Main.getInstance().getGame(player);
                    try {
                        game.leaveGame(Main.getInstance().getPlayer(player));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    Main.getInstance().getPlayer(player).setPlayerLocation(PlayerLocation.LOBBY);
                    player.teleport(Main.getInstance().getLobbyPoint());
                }
            }
        }
        return false;
    }
}
