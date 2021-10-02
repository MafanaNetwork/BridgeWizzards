package me.TahaCheji.playerData;

import me.TahaCheji.gameData.GamePlayer;

import javax.imageio.IIOException;
import java.io.IOException;

public class PlayerStatistics {

    private final GamePlayer gamePlayer;
    private int wins;
    private int deaths;
    private int kills;

    public PlayerStatistics(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public PlayerStatistics(GamePlayer gamePlayer, int wins, int deaths, int kills) {
        this.gamePlayer = gamePlayer;
        this.wins = wins;
        this.deaths = deaths;
        this.kills = kills;
    }

    public void save() throws IOException {
        StatisticsData.savePlayerStatistics(gamePlayer.getPlayer(), this);
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getWins() {
        return wins;
    }
}
