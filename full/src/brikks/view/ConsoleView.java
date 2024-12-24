package brikks.view;

import brikks.*;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.*;
import brikks.save.container.*;
import brikks.view.container.GameText;
import brikks.view.enums.*;

import java.util.*;

public class ConsoleView extends View {
    public final static Scanner keyboard = new Scanner(System.in);

    private static class Colors {
        private final static String ANSI_RESET = "\u001B[0m";
        private final static String ANSI_WHITE = "\u001B[37m";
        private final static String ANSI_YELLOW = "\u001B[33m";
        private final static String ANSI_GREEN = "\u001B[32m";
        private final static String ANSI_RED = "\u001B[31m";
        private final static String ANSI_BLUE = "\u001B[34m";
        private final static String ANSI_BLACK = "\u001B[30m";
        private final static String ANSI_DUELER = "\u001B[36m";

        private final static String ANSI_BG_WHITE = "\u001B[47m";
        private final static String ANSI_BG_YELLOW = "\u001B[43m";
        private final static String ANSI_BG_GREEN = "\u001B[42m";
        private final static String ANSI_BG_RED = "\u001B[41m";
        private final static String ANSI_BG_BLUE = "\u001B[44m";
        private final static String ANSI_BG_BLACK = "\u001B[40m";
        private final static String ANSI_BG_DUELER = "\u001B[46m";
    }

    private final GameText text;
    private final String gameLogo;


    public ConsoleView(final GameText text, final String gameLogo) {
        this.text = text;
        this.gameLogo = gameLogo;
    }


    @Override
    public Menu menu() {
        System.out.println(this.gameLogo);

        return switch (this.askUserChoice(this.text.menu(), this.text.menuVariants(), false)) {
            case 1 -> Menu.NEW_GAME;
            case 2 -> Menu.LOAD;
            case 3 -> Menu.LIDERBOARD;
            case 4 -> Menu.EXIT;
            default -> throw new InputMismatchException("Unexpected choice variant");
        };
    }

    @Override
    public void liderboard(final PlayerLiderboard[] players) {
        // TODO: players arrive from save already ordered decreasing
        System.out.println(this.text.liderboard());
        System.out.println();

        for (final PlayerLiderboard player : players) {
            System.out.printf("%s - (%s - %s) %s - %d",
                    player.name(), player.startDateTime(), player.endDateTime(), player.duration(), player.score());
        }
        System.out.println();

        this.goToMainMenuOnTap();
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


    @Override
    public SavedGame askChoiceSave(final SavedGame[] variants) {
        final String[] textedVariants = new String[variants.length];

        for (byte i = 0; i < textedVariants.length; i++) {
            final StringBuilder played = new StringBuilder();
            for (final String name : variants[i].playerNames()) {
                played.append(name);
                played.append(", ");
            }
            played.delete(played.length() - 2, played.length());

            textedVariants[i] = String.format("%s: %s", variants[i].startTime(), played);
        }

        final byte choice = this.askUserChoice(this.text.askChoiceSave(), textedVariants, true);
        return choice == 0 ? null : variants[choice];
    }


    @Override
    public void draw(final Player player) {
        final byte side = 5;
        // cellLen + bordersCount
        final byte center = Board.WIDTH * 2 + Board.WIDTH + 1;
        final byte width = center + side * 2;

        // Creating border
        final String border = '+' + "-".repeat(width) + '+' + '\n';

        // Creating column number
        final StringBuilder columnNumber = new StringBuilder();
        columnNumber.append('|').append(" ".repeat(side + 1));
        for (byte i = 1; i <= Board.WIDTH; i++) {
            columnNumber.append(this.normNumberLen(i, (byte) 2)).append(' ');
        }
        // one space already added
        columnNumber.append(" ".repeat(side)).append('|').append('\n');

        final StringBuilder playerScreen = new StringBuilder();


        playerScreen.append(border);
        // Centred name
        {
            final byte nameTab = (byte) (width - player.name.length());
            playerScreen.append('|').append(" ".repeat(nameTab / 2));
            playerScreen.append(player.name);
            playerScreen.append(" ".repeat(nameTab / 2 + nameTab % 2)).append('|').append('\n');
        }

        playerScreen.append(border);
        playerScreen.append(columnNumber);

        // BOARD
        {
            final Board board = player.getBoard();

            // Transform board to String
            final String[][] stringedBoard = new String[Board.HEIGHT][Board.WIDTH];
            // Filling up
            for (byte y = 0; y < Board.HEIGHT; y++) {
                stringedBoard[y] = new String[Board.WIDTH];
                for (byte x = 0; x < Board.WIDTH; x++) {
                    stringedBoard[y][x] = "  ";
                }
            }

            // Adding bonuses
            {
                final Color[][] bonuses = board.getEnergyBonus();
                for (byte y = 0; y < Board.HEIGHT; y++) {
                    for (byte x = 0; x < Board.WIDTH; x++) {
                        if (bonuses[y][x] != null) {
                            stringedBoard[y][x] = this.bgColor2string(bonuses[y][x]);
                        }
                    }
                }
            }

            // Adding placed blocks
            for (PlacedBlock placed : board.getBoard()) {
                final String color = this.color2string(placed.getColor());

                final Position start = placed.getPlace();
                for (Position relativePos : placed.getBlock()) {
                    final Position pos = start.add(relativePos);
                    stringedBoard[pos.getY()][pos.getX()] = color;
                }
            }

            // Drawing
            for (byte y = 0; y < Board.HEIGHT; y++) {
                // Border
                playerScreen.append('|').append(" ".repeat(side));
                playerScreen.append("+--".repeat(Board.WIDTH)).append('+');
                playerScreen.append(" ".repeat(side)).append('|').append('\n');

                playerScreen.append("| ");
                // Score for row
                playerScreen.append(this.normNumberLen((byte) board.calculateRow(y), (byte) 2));
                playerScreen.append("  ");
                // Board itself
                for (String cell : stringedBoard[y]) {
                    playerScreen.append('|').append(cell);
                }
                playerScreen.append("| ");
                // Row multiplier
                playerScreen.append(String.format("%3s", "x" + board.getRowMultiplier(y)));
                playerScreen.append(" |");
                playerScreen.append('\n');
            }
            // Border
            playerScreen.append('|').append(" ".repeat(side));
            playerScreen.append("+--".repeat(Board.WIDTH)).append('+');
            playerScreen.append(" ".repeat(side)).append('|').append('\n');
        }

        playerScreen.append(columnNumber);
        playerScreen.append(border);

        // BONUS SCORE
        {
            final BonusScore bonusScore = player.getBonusScore();

            playerScreen.append('|').append(" ".repeat(side));
            final String stringedBonusScore = String.format(this.text.bonusScore(),
                    bonusScore.get(), bonusScore.getNext());
            playerScreen.append(stringedBonusScore);
            playerScreen.append(" ".repeat(center - stringedBonusScore.length() + side));
            playerScreen.append('|').append('\n');
        }

        // ENERGY
        {
            final Energy energy = player.getEnergy();

            playerScreen.append('|').append(" ".repeat(side));
            final String stringedEnergy = String.format(this.text.energy(),
                    energy.getAvailable(), energy.getDistanceToNextBonus());
            playerScreen.append(stringedEnergy);
            playerScreen.append(" ".repeat(center - stringedEnergy.length() + side));
            playerScreen.append('|').append('\n');
        }
        // BOMBS
        {
            final Bombs bombs = player.getBombs();

            playerScreen.append('|').append(" ".repeat(side));
            final String stringedBombs = String.format(this.text.bombs(),
                    bombs.get(), bombs.calculateFinal());
            playerScreen.append(stringedBombs);
            playerScreen.append(" ".repeat(center - stringedBombs.length() + side));
            playerScreen.append('|').append('\n');
        }

        playerScreen.append(border);

        System.out.println(playerScreen.toString() + '\n');
    }

    @Override
    public void endSolo(final String name, final short finalScore, final Level difficulty) {
        System.out.println("\n\n");
        System.out.println(this.text.end());

        System.out.printf(this.text.endSolo(),
                name, this.text.ranks()[Ranks.getRank(difficulty, finalScore)], finalScore);

        System.out.println("\n\n");

        this.goToMainMenuOnTap();
    }

    @Override
    public void endStandard(final Player[] players) {
        Arrays.sort(players);

        System.out.println("\n\n");
        System.out.println(this.text.end());
        System.out.println(this.text.endStandard());

        for (byte i = 0; i < players.length; i++) {
            System.out.printf("%d. %s - %d\n", i, players[i].name, players[i].calculateFinal());
        }

        System.out.println("\n\n");

        this.goToMainMenuOnTap();
    }

    @Override
    public void endDuel(final String winner, final String loser) {
        System.out.println("\n\n");
        System.out.println(this.text.end());

        System.out.printf(this.text.endDuel(), winner, loser);

        System.out.println("\n\n");

        this.goToMainMenuOnTap();
    }

    @Override
    public void exit() {
        System.out.println(this.text.exit());
    }


    // TODO: real'no zroby ConsoleView
    // PlayerAsk
    @Override
    public boolean askReroll(final BlocksTable blocks, final MatrixDice matrixDie) {
        return false;
    }

    // TODO: placing spot mozxe buty null
    @Override
    public Position askPlacingSpot(final Block block, final Position[] variants) {
//        final byte choice = this.askUserChoice(this.text.askPlacingSpot(), variants, true);
//        return choice == 0 ? null : variants[choice - 1];
        return variants[0];
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


    private void goToMainMenuOnTap() {
        System.out.println(this.text.goToMainMenuOnTap());
        try {
            keyboard.nextLine();
        } catch (NoSuchElementException | IllegalStateException ignored) {
        }
    }

    private String color2string(final Color color) {
        return switch (color) {
            case Color.WHITE -> Colors.ANSI_WHITE + "WH";
            case Color.YELLOW -> Colors.ANSI_YELLOW + "YW";
            case Color.GREEN -> Colors.ANSI_GREEN + "GN";
            case Color.RED -> Colors.ANSI_RED + "RD";
            case Color.BLUE -> Colors.ANSI_BLUE + "BL";
            case Color.BLACK -> Colors.ANSI_BLACK + "BK";
            case Color.DUELER -> Colors.ANSI_DUELER + "MB";
            default -> throw new InputMismatchException("Unexpected color");
        } + Colors.ANSI_RESET;
    }

    private String bgColor2string(final Color bgColor) {
        return switch (bgColor) {
            case Color.WHITE -> Colors.ANSI_BG_WHITE + "wh";
            case Color.YELLOW -> Colors.ANSI_BG_YELLOW + "yw";
            case Color.GREEN -> Colors.ANSI_BG_GREEN + "gn";
            case Color.RED -> Colors.ANSI_BG_RED + "rd";
            case Color.BLUE -> Colors.ANSI_BG_BLUE + "bl";
            case Color.BLACK -> Colors.ANSI_BG_BLACK + "bk";
            case Color.DUELER -> Colors.ANSI_BG_DUELER + "mb";
            default -> throw new InputMismatchException("Unexpected color");
        } + Colors.ANSI_RESET;
    }

    private String normNumberLen(byte number, final byte len) {
        if (number < 0) {
            throw new IllegalArgumentException("Numbers less than zero is not supported for the moment");
        }

        final StringBuilder normNumber = new StringBuilder();
        while (number != 0) {
            normNumber.append(number % 10);
            number /= 10;
        }

        if (normNumber.length() <= len) {
            normNumber.append("0".repeat(len - normNumber.length()));
        }

        return normNumber.reverse().toString();
    }

    // Askers
    private byte askUserChoice(final String message, final String[] variants, final boolean exitAvailable) {
        final byte minimumChoice = (byte) (exitAvailable ? 0 : 1);

        final StringBuilder description = new StringBuilder();
        description.append("\n");
        description.append(message);
        description.append("\n");
        if (exitAvailable) {
            description.append(0);
            description.append(". ");
            description.append(this.text.backVariant());
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
