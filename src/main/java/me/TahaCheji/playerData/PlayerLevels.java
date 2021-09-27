package me.TahaCheji.playerData;

import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GamePlayer;
import org.bukkit.ChatColor;

public enum PlayerLevels {

    ONE(100),
    TWO(PlayerLevels.ONE.getXp() * 5),
    THREE(PlayerLevels.TWO.getXp() * 5),
    FOUR(PlayerLevels.THREE.getXp() * 5),
    FIVE(PlayerLevels.FOUR.getXp() * 5),
    SIX(PlayerLevels.FIVE.getXp() * 5),
    SEVEN(PlayerLevels.SIX.getXp() * 5),
    EIGHT(PlayerLevels.SEVEN.getXp() * 5),
    NINE(PlayerLevels.EIGHT.getXp() * 5),
    TEN(PlayerLevels.NINE.getXp() * 5);

    private final int xp;

    PlayerLevels(int xp) {
        this.xp = xp;
    }

    public int getXp() {
        return xp;
    }

    public static PlayerLevels getXpTo (GamePlayer gamePlayer) {
        if(gamePlayer.getLevels().getLevel() == 0) {
            return PlayerLevels.ONE;
        }
        if(gamePlayer.getLevels().getLevel() == 1) {
            return PlayerLevels.TWO;
        }
        if(gamePlayer.getLevels().getLevel() == 2) {
            return PlayerLevels.THREE;
        }
        if(gamePlayer.getLevels().getLevel() == 3) {
            return PlayerLevels.FOUR;
        }
        if(gamePlayer.getLevels().getLevel() == 4) {
            return PlayerLevels.FIVE;
        }
        if(gamePlayer.getLevels().getLevel() == 5) {
            return PlayerLevels.SIX;
        }
        if(gamePlayer.getLevels().getLevel() == 6) {
            return PlayerLevels.SEVEN;
        }
        if(gamePlayer.getLevels().getLevel() == 7) {
            return PlayerLevels.EIGHT;
        }
        if(gamePlayer.getLevels().getLevel() == 8) {
            return PlayerLevels.NINE;
        }
        if(gamePlayer.getLevels().getLevel() == 9) {
            return PlayerLevels.TEN;
        }
        return null;
    }
}
