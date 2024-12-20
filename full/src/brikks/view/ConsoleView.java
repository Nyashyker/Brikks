package brikks.view;

import brikks.*;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.Board;
import brikks.save.container.*;
import brikks.view.enums.*;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleView extends View {
    public static final Scanner keyboard = new Scanner(System.in);


    private final GameText text;
    private final String gameLogo;


    public ConsoleView(final GameText text, final String gameLogo) {
        this.text = text;
        this.gameLogo = gameLogo;
    }


    @Override
    public Menu menu() {
        System.out.println(this.gameLogo);

        return switch (this.askUserChoice(this.text.menu(), this.text.menuVariants(), true)) {
            case 0 -> Menu.EXIT;
            case 1 -> Menu.NEW_GAME;
            case 2 -> Menu.LOAD;
            case 3 -> Menu.LIDERBOARD;
            default -> throw new InputMismatchException("Unexpected choice variant");
        };
    }

    @Override
    public void liderboard(PlayerLiderboard[] players) {
        // TODO: players arrive from save already ordered decreasing
        System.out.println(this.text.liderboard());
        System.out.println();

        for (final PlayerLiderboard player : players) {
            System.out.println(player.toString());
        }
        System.out.println();

        System.out.println(this.text.liderboardExit());
        try {
            keyboard.nextLine();
        } catch (NoSuchElementException | IllegalStateException ignored) {}
    }


    @Override
    public boolean askUseExistingPlayer(final String name) {
        return this.askUserChoice(this.text.askUseExistingPlayer());
    }

    @Override
    public Level askDifficulty() {
        return switch (this.askUserChoice(this.text.askDifficulty(), this.text.difficultyVariants(), true)) {
            case 0 -> null;
            case 1 -> Level.ONE;
            case 2 -> Level.TWO;
            case 3 -> Level.THREE;
            case 4 -> Level.FOUR;
            default -> throw new InputMismatchException("Unexpected choice variant");
        };
    }

    @Override
    public boolean askDuel() {
        return this.askUserChoice(this.text.askDuel());
    }

    @Override
    public byte askPlayerCount(final byte maxPlayers) {
        final byte count = this.askUserNumber(this.text.askPlayerCount() + ": ", (byte) 0, (byte) 4);
        return count == 0 ? -1 : count;
    }

    @Override
    public String askName() {
        final String name = this.askUserString(this.text.askName(), true);
        return name.isEmpty() ? null : name;
    }


    // TODO: real'no zroby ConsoleView
    @Override
    public SavedGame askChoiceSave(SavedGame[] variants) {
        return null;
    }


    @Override
    public void draw(Player player) {
    }

    @Override
    public void endSolo(Rank[] ranks, short finalScore) {
    }

    @Override
    public void endStandard(Player[] players) {
    }

    @Override
    public void endDuel(Player winner, Player loser) {
    }

    @Override
    public void exit() {
        System.out.println(this.text.exit());
    }


    // PlayerAsk
    @Override
    public boolean askReroll(final BlocksTable blocks, final MatrixDice matrixDie) {
        return false;
    }

    // TODO: placing spot mozxe buty null
    @Override
    public Position askPlacingSpot(final Block block, final Position[] variants) {
        return new Position((byte) 0, (byte) 0);
    }

    @Override
    public Doing askDoing(final BlocksTable blocks, final Position roll) {
        return Doing.EXIT;
    }

    @Override
    public byte askRotation(final Block[] variants) {
        return (byte) 0;
    }

    @Override
    public Position askChoice(final BlocksTable variants) {
        return new Position((byte) 0, (byte) 0);
    }

    @Override
    public void successPlace(final PlacedBlock placed) {
    }

    @Override
    public void successBomb() {
    }

    @Override
    public void successRotation(final byte energyCost) {
    }

    @Override
    public void successChoice(final byte energyCost) {
    }

    @Override
    public void failPlace() {
    }

    @Override
    public void failBomb() {
    }

    @Override
    public void failRotation() {
    }

    @Override
    public void failChoice() {
    }

    @Override
    public void failGiveUp() {
    }

    @Override
    public void fail() {
    }


    // DuelAsk
    @Override
    public Position askPlacingMiniblock(Board opponentsBoard, Position[] variants) {
        return new Position((byte) 0, (byte) 0);
    }


    // askers
    private byte askUserChoice(final String message, final String[] variants, final boolean exitAvailable) {
        final byte minimumChoice = (byte) (exitAvailable ? 0 : 1);

        final StringBuilder description = new StringBuilder();
        description.append("\n");
        description.append(message);
        description.append("\n");
        if (exitAvailable) {
            description.append(0);
            description.append(". ");
            description.append(this.text.exitVariant());
            description.append("\n");
        }
        for (byte i = 0; i < variants.length; i++) {
            description.append(i + 1);
            description.append(". ");
            description.append(variants[i]);
            description.append("\n");
        }
        description.append("\n");

        return this.askUserNumber(description.toString(), minimumChoice, (byte) variants.length);
    }

    private byte askUserNumber(final String description, final byte minimumChoice, final byte maximumChoice) {
        System.out.println(description);

        byte choice = -1;
        while (choice == -1) {
            System.out.printf(this.text.choiceInRange(), minimumChoice, maximumChoice);
            if (!keyboard.hasNextByte()) {
                keyboard.nextLine();
                continue;
            }
            choice = keyboard.nextByte();

            if (choice < minimumChoice || choice > maximumChoice) {
                System.out.println(this.text.inputValidChoice());
                choice = -1;
            }
        }

        return choice;
    }

    private boolean askUserChoice(final String message) {
        while (true) {
            final String input = this.askUserString(message, false);

            if (LanguagesSupport.isTrue(input)) {
                return true;
            } else if (LanguagesSupport.isFalse(input)) {
                return false;
            }
        }
    }

    private String askUserString(final String message, final boolean emptyAllowed) {
        String input;
        do {
            System.out.print(message + ": ");
            input = keyboard.nextLine();
        } while (!emptyAllowed && input.isEmpty());

        return input;
    }
}
