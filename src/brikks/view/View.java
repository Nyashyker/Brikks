package brikks.view;

import brikks.Player;
import brikks.essentials.enums.Level;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;
import brikks.view.container.GameText;
import brikks.view.enums.Menu;

import java.util.List;


public abstract class View implements PlayerAsk, DuelAsk {
    public final static byte MAX_NAME_LEN = 37;
    public final static byte LEADERBOARD_COUNT = 12;

    protected final GameText text;


    protected View(final GameText text) {
        this.text = text;
    }


    abstract public Menu menu();

    abstract public void leaderboard(final List<PlayerLeaderboard> players);


    abstract public boolean askUseExistingPlayer(final String name);

    abstract public Level askDifficulty();

    abstract public boolean askDuel();

    abstract public byte askPlayerCount(final byte maxPlayers);

    abstract public String askName(final byte playerNumber);


    abstract public SavedGame askChoiceSave(final List<SavedGame> variants);


    abstract public void draw(final Player player);

    abstract public void endSolo(final String name, final short finalScore, final Level difficulty);

    abstract public void endStandard(final Player[] players);

    abstract public void endDuel(final String winner, final String loser);

    abstract public void exit();
}
