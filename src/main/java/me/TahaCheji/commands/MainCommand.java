package me.TahaCheji.commands;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GameData;
import me.TahaCheji.data.GameGui;
import me.TahaCheji.data.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("bz")) {
            Player player = (Player) sender;
             if(args[0].equalsIgnoreCase("join")) {
                if(args.length == 1) {
                    new GameGui().getGameGui().open(player);
                    return true;
                }
                 Game game = Main.getInstance().getGame(args[1]);
                if(game == null) {
                    return true;
                }
                if(game.isState(Game.GameState.ACTIVE)) {
                    player.sendMessage("That game is active");
                    return true;
                }
                GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
                gamePlayer.setGame(game);
                game.joinGame(gamePlayer);
            }
             if(args[0].equalsIgnoreCase("leave")) {
                 if(Main.getInstance().isInGame(player)) {
                     Game game = Main.getInstance().getGame(player);
                     game.leaveGame(Main.getInstance().getPlayer(player));
                     return true;
                 }
                 if(player.isOp()) {
                     Game game = Main.getInstance().getGame(player);
                     game.leaveAdmin(Main.getInstance().getPlayer(player));
                 }
             }
        }
        return false;
    }
}
