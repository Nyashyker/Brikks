package brikks.save;

import brikks.Player;
import brikks.essentials.enums.Level;
import brikks.essentials.enums.Mode;

public class EmptySave extends Save {
    public EmptySave() {}

    @Override
    public PlayerSave createPlayerSave(final String name) { return new EmptyPlayerSave(); }
    @Override
    public SavedGame[] savedGames() { return new SavedGame[0]; }
    @Override
    public LoadedGame loadGame(final int id) { return null; }
    @Override
    public PlayerLiderboard[] liderboard() { return null; }
    @Override
    public String[] existingPlayers() { return new String[0]; }
    @Override
    public PlayerSave getPlayerSave(final String name) { return null; }
    @Override
    public void save(final Level difficulty) {}
    @Override
    public void save(final Mode mode) {}
    @Override
    public void save(final Player player) {}
    @Override
    public void saveStartDateTime() {}
    @Override
    public void saveEndDateTime() {}
    @Override
    public void updateDuration(final int seconds) {}
    @Override
    public void dropSave() {}
}