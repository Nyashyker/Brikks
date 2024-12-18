package brikks.view;

import brikks.*;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.Board;
import brikks.save.*;
import brikks.view.enums.*;

import java.util.Scanner;

public class ConsoleView extends View {
    public static final Scanner keyboard = new Scanner(System.in);

    public ConsoleView() {}

    // TODO: real'no zroby ConsoleView

    @Override
    public Menu menu() { return Menu.NEW_GAME; }
    @Override
    public void liderboard(PlayerLiderboard[] players) {}
    @Override
    public boolean askUseExistingPlayer(final String name) { return false; }
    @Override
    public Level askDifficulty() { return Level.TWO; }
    @Override
    public Mode askMode() { return Mode.SOLO; }
    @Override
    public byte askPlayerCount(final byte maxPlayers) { return maxPlayers; }
    @Override
    public String askName() { return "NONE"; }
    @Override
    public PlacedBlock[] sakFirstChoise(BlocksTable variants, Player[] players) { return new PlacedBlock[0]; }
    @Override
    public void draw(Player player) {}
    @Override
    public void endSolo(Rank[] ranks, short finalScore) {}
    @Override
    public void endStandard(Player[] players) {}
    @Override
    public void endDuel(Player winner, Player loser) {}
    @Override
    public void exit() {}



    @Override
    public boolean askReroll(final BlocksTable blocks, final MatrixDice matrixDie) { return false; }
    // TODO: placing spot mozxe buty null
    @Override
    public Position askPlacingSpot(final Block block, final Position[] variants) { return new Position((byte) 0, (byte) 0); }
    @Override
    public Doing askDoing(final BlocksTable blocks, final Position roll) { return Doing.EXIT; }
    @Override
    public byte askRotation(final Block[] variants) { return (byte) 0; }
    @Override
    public Position askChoice(final BlocksTable variants) { return new Position((byte) 0, (byte) 0); }
    @Override
    public void successPlace(final PlacedBlock placed) {}
    @Override
    public void successBomb() {}
    @Override
    public void successRotation(final byte energyCost) {}
    @Override
    public void successChoice(final byte energyCost) {}
    @Override
    public void failPlace() {}
    @Override
    public void failBomb() {}
    @Override
    public void failRotation() {}
    @Override
    public void failChoice() {}
    @Override
    public void failGiveUp() {}
    @Override
    public void fail() {}



    @Override
    public Position askPlacingMiniblock(Board opponentsBoard, Position[] variants) { return new Position((byte) 0, (byte) 0); }
}
