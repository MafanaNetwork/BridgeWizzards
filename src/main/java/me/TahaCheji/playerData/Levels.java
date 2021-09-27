package me.TahaCheji.playerData;

import me.TahaCheji.gameData.GamePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import java.io.IOException;

public class Levels {

    private GamePlayer player;
    private int level;
    private int xp;


    public Levels(GamePlayer player) {
        this.player = player;
    }

    public Levels(GamePlayer player, int level, int xp) {
        this.player = player;
        this.level = level;
        this.xp = xp;
    }

    public void saveLvl () throws IOException {
        LevelData.savePlayerLevel(player.getPlayer(), this);
    }

    public void addXp (int xp) throws IOException {
        setXp(xp + getXp());
        if(getXp() >= PlayerLevels.getXpTo(player).getXp()) {
            addLvl(1);
            setXp(0);
            Player player = getPlayer().getPlayer();
            player.sendMessage(ChatColor.GRAY + "-----------------------------------------------");
            player.sendMessage(ChatColor.GOLD + "LEVEL UP!");
            player.sendMessage(ChatColor.GOLD + "PlayerLevel: " + getLevel());
            player.sendMessage(ChatColor.GOLD + "LevelXp: " + getXp());
            player.sendMessage(ChatColor.GRAY + "-----------------------------------------------");
            saveLvl();
        }
    }

    public void addLvl (int lvl) throws IOException {
        setLevel(lvl + getLevel());
        Player player = getPlayer().getPlayer();
        player.sendMessage(ChatColor.GRAY + "-----------------------------------------------");
        player.sendMessage(ChatColor.GOLD + "LEVEL UP!");
        player.sendMessage(ChatColor.GOLD + "PlayerLevel: " + getLevel());
        player.sendMessage(ChatColor.GOLD + "LevelXp: " + getXp());
        player.sendMessage(ChatColor.GRAY + "-----------------------------------------------");
        saveLvl();
        if(getLevel() >= 10) {
            setLevel(10);
        }
    }



    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setPlayer(GamePlayer player) {
        this.player = player;
    }

    public GamePlayer getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }
}
