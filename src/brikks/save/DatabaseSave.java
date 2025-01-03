package brikks.save;

import brikks.Player;
import brikks.essentials.enums.Level;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;


public class DatabaseSave extends Save {
    @Override
    public boolean playerExists(String name) {
        return false;
    }

    @Override
    public SavedGame[] savedGames() {
        return new SavedGame[0];
    }

    @Override
    public LoadedGame loadGame(int id) {
        return null;
    }

    @Override
    public PlayerLeaderboard[] leaderboard() {
        return new PlayerLeaderboard[0];
    }

    @Override
    public PlayerSave getPlayerSave(String name) {
        return null;
    }

    @Override
    public void save(Level difficulty) {

    }

    @Override
    public void save(boolean duel) {

    }

    @Override
    public void save(Player player) {

    }

    @Override
    public void saveStartDateTime() {

    }

    @Override
    public void saveEndDateTime() {

    }

    @Override
    public void startCountingTime() {

    }

    @Override
    public void updateDuration() {

    }
}
