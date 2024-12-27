package brikks.save;

import brikks.Player;
import brikks.essentials.enums.*;
import brikks.save.container.LoadedGame;
import brikks.save.container.PlayerLiderboard;
import brikks.save.container.SavedGame;

public class EmptySave extends Save {
    public EmptySave() {}

    @Override
    public boolean playerExists(String name) { return false; }
    @Override
    public SavedGame[] savedGames() { return new SavedGame[0]; }
    @Override
    public LoadedGame loadGame(final int id) { return null; }
    @Override
    public PlayerLiderboard[] liderboard() { return new PlayerLiderboard[0]; }
    @Override
    public PlayerSave getPlayerSave(final String name) { return new EmptyPlayerSave(); }
    @Override
    public void save(final Level difficulty) {}
    @Override
    public void save(final boolean duel) {}
    @Override
    public void save(final Player player) {}
    @Override
    public void saveStartDateTime() {}
    @Override
    public void saveEndDateTime() {}
    @Override
    public void startCountingTime() {}
    @Override
    public void updateDuration() {}
}
