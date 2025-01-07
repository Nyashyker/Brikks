package brikks.view;

import brikks.BlocksTable;
import brikks.Player;
import brikks.essentials.Block;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;
import brikks.essentials.enums.Color;
import brikks.essentials.enums.Level;
import brikks.logic.Board;
import brikks.logic.Bombs;
import brikks.logic.BonusScore;
import brikks.logic.Energy;
import brikks.save.container.PlayerLeaderboard;
import brikks.save.container.SavedGame;
import brikks.view.container.GameText;
import brikks.view.enums.Deed;
import brikks.view.enums.Menu;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleView extends View {
    public static final Scanner keyboard = new Scanner(System.in);

    private static class Colors {
        private static final String ANSI_RESET = "\u001B[0m";
        private static final String ANSI_WHITE = "\u001B[37m";
        private static final String ANSI_YELLOW = "\u001B[33m";
        private static final String ANSI_GREEN = "\u001B[32m";
        private static final String ANSI_RED = "\u001B[31m";
        private static final String ANSI_BLUE = "\u001B[34m";
        private static final String ANSI_BLACK = "\u001B[30m";
        private static final String ANSI_DUELER = "\u001B[36m";

        private static final String ANSI_BG_WHITE = "\u001B[47m";
        private static final String ANSI_BG_YELLOW = "\u001B[43m";
        private static final String ANSI_BG_GREEN = "\u001B[42m";
        private static final String ANSI_BG_RED = "\u001B[41m";
        private static final String ANSI_BG_BLUE = "\u001B[44m";
        private static final String ANSI_BG_BLACK = "\u001B[40m";
        private static final String ANSI_BG_DUELER = "\u001B[46m";
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
            case 3 -> Menu.LEADERBOARD;
            case 4 -> Menu.EXIT;
            default -> throw new InputMismatchException("Unexpected choice variant");
        };
    }

    @Override
    public void leaderboard(final List<PlayerLeaderboard> players) {
        System.out.println(this.text.leaderboard());
        System.out.println();

        final DateTimeFormatter start = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        final DateTimeFormatter end = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        final DateTimeFormatter duration = DateTimeFormatter.ofPattern("D HH:mm:ss");
        for (final PlayerLeaderboard player : players) {
            System.out.printf("%s - (%s - %s) %s - %d\n",
                    player.name(), player.startDateTime().format(start), player.endDateTime().format(end), player.duration().format(duration), player.score());
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
        return this.askUserNumber(this.text.askPlayerCount(), (byte) 0, (byte) 4);
    }

    @Override
    public String askName(final byte playerNumber) {
        String name;
        do {
            name = this.askUserString(String.format(this.text.askName(), playerNumber), true);
            name = name.replace("\\", "\\\\");
            // TODO: protect problematic symbols
            if (name.length() > View.MAX_NAME_LEN) {
                System.out.printf(this.text.nameToLong(), View.MAX_NAME_LEN);
            }
        } while (name.length() > View.MAX_NAME_LEN);
        return name.isEmpty() ? null : name;
    }


    @Override
    public SavedGame askChoiceSave(final List<SavedGame> variants) {
        final String[] textedVariants = new String[variants.size()];

        for (byte i = 0; i < textedVariants.length; i++) {
            final StringBuilder played = new StringBuilder();
            for (final String name : variants.get(i).playerNames()) {
                played.append(name);
                played.append(", ");
            }
            played.delete(played.length() - 2, played.length());

            textedVariants[i] = String.format("%s: %s", variants.get(i).startTime(), played);
        }

        final byte choice = this.askUserChoice(this.text.askChoiceSave(), textedVariants, true);
        return choice == 0 ? null : variants.get(choice - 1);
    }


    @Override
    public void draw(final Player player) {
        final byte side = 5;
        // cellLen + bordersCount
        final byte center = Board.WIDTH * 2 + Board.WIDTH + 1;
        final byte width = center + side * 2;

        // Creating border
        final String border = '+' + "-".repeat(width) + '+';

        // Creating column number
        final StringBuilder columnNumber = new StringBuilder();
        columnNumber.append('|').append(" ".repeat(side + 1));
        for (byte i = 1; i <= Board.WIDTH; i++) {
            columnNumber.append(this.normNumberLen(i, (byte) 2)).append(' ');
        }
        // one space already added
        columnNumber.append(" ".repeat(side)).append('|');

        // Creating screen plate
        final StringBuilder[] playerScreen;
        {
            final byte height = 11 + Board.HEIGHT * 2;
            playerScreen = new StringBuilder[height];
            for (byte y = 0; y < height; y++) {
                playerScreen[y] = new StringBuilder();
            }
        }
        byte index = 0;


        playerScreen[index++].append(border);
        // Centred name
        {
            final byte nameTab = (byte) (width - player.name.length());
            playerScreen[index].append('|').append(" ".repeat(nameTab / 2));
            playerScreen[index].append(player.name);
            playerScreen[index++].append(" ".repeat(nameTab / 2 + nameTab % 2)).append('|');
        }

        playerScreen[index++].append(border);
        playerScreen[index++].append(columnNumber);

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

                for (Position shapePos : placed.getBlock()) {
                    stringedBoard[shapePos.getY()][shapePos.getX()] = color;
                }
            }

            // Drawing
            for (byte y = 0; y < Board.HEIGHT; y++) {
                // Border
                playerScreen[index].append('|').append(" ".repeat(side));
                playerScreen[index].append("+--".repeat(Board.WIDTH)).append('+');
                playerScreen[index++].append(" ".repeat(side)).append('|');

                playerScreen[index].append("| ");
                // Score for row
                playerScreen[index].append(this.normNumberLen((byte) board.calculateRow(y), (byte) 2));
                playerScreen[index].append("  ");
                // Board itself
                for (String cell : stringedBoard[y]) {
                    playerScreen[index].append('|').append(cell);
                }
                playerScreen[index].append("| ");
                // Row multiplier
                playerScreen[index].append(String.format("%3s", "x" + board.getRowMultiplier(y)));
                playerScreen[index++].append(" |");
            }
            // Border
            playerScreen[index].append('|').append(" ".repeat(side));
            playerScreen[index].append("+--".repeat(Board.WIDTH)).append('+');
            playerScreen[index++].append(" ".repeat(side)).append('|');
        }

        playerScreen[index++].append(columnNumber);
        playerScreen[index++].append(border);

        // BONUS SCORE
        {
            final BonusScore bonusScore = player.getBonusScore();

            playerScreen[index].append('|').append(" ".repeat(side - 2));
            final String stringedBonusScore = String.format(this.text.bonusScore(),
                    bonusScore.calculateFinal(), bonusScore.calculateNextFinal() == -1 ?
                            this.text.max() : bonusScore.calculateNextFinal() + "");
            playerScreen[index].append(stringedBonusScore);
            playerScreen[index].append(" ".repeat(center - stringedBonusScore.length() + side + 2));
            playerScreen[index++].append('|');
        }

        // ENERGY
        {
            final Energy energy = player.getEnergy();

            playerScreen[index].append('|').append(" ".repeat(side - 2));
            final String stringedEnergy = String.format(this.text.energy(),
                    energy.getAvailable(), energy.getDistanceToNextBonus() == -1 ?
                            this.text.max() : energy.getDistanceToNextBonus() + "");
            playerScreen[index].append(stringedEnergy);
            playerScreen[index].append(" ".repeat(center - stringedEnergy.length() + side + 2));
            playerScreen[index++].append('|');
        }

        // BOMBS
        {
            final Bombs bombs = player.getBombs();

            playerScreen[index].append('|').append(" ".repeat(side - 2));
            final String stringedBombs = String.format(this.text.bombs(),
                    bombs.get(), bombs.calculateFinal());
            playerScreen[index].append(stringedBombs);
            playerScreen[index].append(" ".repeat(center - stringedBombs.length() + side + 2));
            playerScreen[index++].append('|');
        }

        playerScreen[index].append(border);

        for (final StringBuilder display : playerScreen) {
            System.out.println(display);
        }
        System.out.println();
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


    // PlayerAsk
    @Override
    public boolean askReroll(final Block block) {
        this.showBlock(block);
        return this.askUserChoice(this.text.askReroll());
    }

    @Override
    public Position askPlacingSpot(final Player player, final Block block, final Position[] variants) {
        this.draw(player);
        this.showBlock(block);

        final String[] stringedVariants = new String[variants.length];
        for (byte i = 0; i < variants.length; i++) {
            stringedVariants[i] = String.format(this.text.blockPosition(),
                    variants[i].getX() + 1, Board.HEIGHT - variants[i].getY());
        }

        final byte choice = this.askUserChoice(this.text.askPlacingSpot(), stringedVariants, true);
        return choice == 0 ? null : variants[choice - 1];
    }

    @Override
    public Deed askDeed(final Block block) {
        this.showBlock(block);
        return switch (this.askUserChoice(this.text.askDeed(), this.text.deedVariants(), false)) {
            case 1 -> Deed.BOMB;
            case 2 -> Deed.ROTATE;
            case 3 -> Deed.CHOICE;
            case 4 -> Deed.PLACE;
            case 5 -> Deed.GIVE_UP;
            case 6 -> Deed.SAVE;
            case 7 -> Deed.EXIT;
            default -> throw new IllegalArgumentException("Unexpected value for deed");
        };
    }

    @Override
    public byte askRotation(final Block[] variants) {
        this.showBlockRow(variants);
        final byte choice = this.askUserNumber(this.text.askRotation(), (byte) 0, (byte) variants.length);
        return (byte) (choice - 1);
    }

    @Override
    public Position askChoice(final Block[][] variants) {
        this.showBlocksTable(variants);
        final byte choiceX = this.askUserNumber(this.text.askChoiceX(), (byte) 0, BlocksTable.WIDTH);
        if (choiceX == 0) {
            return null;
        }
        final byte choiceY = this.askUserNumber(this.text.askChoiceY(), (byte) 0, BlocksTable.HEIGHT);
        if (choiceY == 0) {
            return null;
        }
        return new Position((byte) (choiceX - 1), (byte) (choiceY - 1));
    }

    @Override
    public void successPlace(final PlacedBlock placed) {
        // There could be something in not console version
    }

    @Override
    public void successBomb() {
        // There could be something in not console version
    }

    @Override
    public void successRotation(final byte energyCost) {
        // There could be something in not console version
    }

    @Override
    public void successChoice(final byte energyCost) {
        // There could be something in not console version
    }

    @Override
    public void failPlace() {
        System.out.println(this.text.failPlace());
    }

    @Override
    public void failBomb() {
        System.out.println(this.text.failBomb());
    }

    @Override
    public void failRotation() {
        System.out.println(this.text.failRotation());
    }

    @Override
    public void failChoice() {
        System.out.println(this.text.failChoice());
    }

    @Override
    public void failGiveUp() {
        System.out.println(this.text.failGiveUp());
    }

    @Override
    public void fail() {
        System.out.println(this.text.fail());
    }


    // DuelAsk
    @Override
    public Position askPlacingMiniblock(final Player opponent, final Position[] variants) {
        this.draw(opponent);
        this.showBlock(Board.duelBlock);

        final String[] stringedVariants = new String[variants.length];
        for (byte i = 0; i < variants.length; i++) {
            stringedVariants[i] = String.format("%d  %d", variants[i].getX(), variants[i].getY());
        }

        final byte choice = this.askUserChoice(this.text.askPlacingSpotDuel(), stringedVariants, false);
        return variants[choice - 1];
    }


    private void goToMainMenuOnTap() {
        System.out.println(this.text.goToMainMenuOnTap());
        keyboard.nextLine();
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

    // Showers for blocks
    private void showBlocksTable(final Block[][] blocksTable) {
        System.out.println();

        for (final Block[] blocksRow : blocksTable) {
            final byte blockHeight;
            if (blocksRow[0].getColor() == Color.BLACK) {
                blockHeight = 4;
            } else {
                blockHeight = 3;
            }

            this.showBlockRow(blocksRow, blockHeight, (byte) 4);
        }
    }

    private void showBlockRow(final Block[] blocksRow) {
        System.out.println();

        byte blockSize = 0;
        for (final Block block : blocksRow) {
            final Position size = block.getSize();
            final byte maxSize = size.getX() > size.getY() ? size.getX() : size.getY();
            if (blockSize < maxSize) {
                blockSize = maxSize;
            }
        }

        this.showBlockRow(blocksRow, blockSize, blockSize);
    }

    private void showBlockRow(final Block[] blocksRow, final byte blockHeight, final byte blockWidth) {
        final StringBuilder[] shownBlock = new StringBuilder[blockHeight + 4];
        for (byte y = 0; y < blockHeight + 4; y++) {
            shownBlock[y] = new StringBuilder();
        }

        for (final Block block : blocksRow) {
            // `shownBlock` gets modified by this call
            this.buildBlock(block, shownBlock, blockWidth);
        }

        for (final StringBuilder stringedBlock : shownBlock) {
            System.out.println(stringedBlock);
        }
    }

    private void showBlock(final Block block) {
        System.out.println();

        final Position blockSize = block.getSize();

        final StringBuilder[] shownBlock = new StringBuilder[blockSize.getY() + 4];
        for (byte y = 0; y < blockSize.getY() + 4; y++) {
            shownBlock[y] = new StringBuilder();
        }

        this.buildBlock(block, shownBlock, blockSize.getX());

        for (final StringBuilder stringedBlock : shownBlock) {
            System.out.println(stringedBlock);
        }
    }

    private void buildBlock(final Block block, final StringBuilder[] base, final byte width) {
        final Position blockSize = block.getSize();

        final String[][] blockTable = new String[blockSize.getY()][blockSize.getX()];
        for (byte y = 0; y < blockSize.getY(); y++) {
            blockTable[y] = new String[blockSize.getX()];
            for (byte x = 0; x < blockSize.getX(); x++) {
                blockTable[y][x] = null;
            }
        }

        // Converting Block to stringed 2D array
        // but upside down
        // (because of the way it then converts in the string)
        {
            final String color = this.color2string(block.getColor());

            for (final Position shape : block.getBlock()) {
                blockTable[-shape.getY()][shape.getX()] = color;
            }
        }

        byte height = (byte) base.length;

        // Some border const
        final String border = '+' + "--".repeat(width + 2) + '+';
        final String subBorder = '|' + "  ".repeat(width + 2) + '|';

        base[--height].append(border);
        // Centring the shape vertically
        {
            byte subHeight;
            do {
                base[--height].append(subBorder);
                subHeight = (byte) (height - 3 - blockSize.getY());
            } while (subHeight / 2 + subHeight % 2 > 0);
        }
        // Drawing the shape
        // & centring it horizontally
        {
            final byte subWidth = (byte) (width + 2 - blockSize.getX());
            for (byte y = 0; y < blockSize.getY(); y++) {
                base[--height].append('|').append(" ".repeat(subWidth));
                for (byte x = 0; x < blockSize.getX(); x++) {
                    base[height].append(blockTable[y][x] == null ? "  " : blockTable[y][x]);
                }
                base[height].append(" ".repeat(subWidth)).append('|');
            }
        }
        // Centring the shape vertically
        // for the sake of simplicity, just taking the reminder
        do {
            base[--height].append(subBorder);
        } while (height > 1);
        base[--height].append(border);
    }

    // Colors
    private String color2string(final Color color) {
        return switch (color) {
            case Color.WHITE -> Colors.ANSI_WHITE + "WH";
            case Color.YELLOW -> Colors.ANSI_YELLOW + "YW";
            case Color.GREEN -> Colors.ANSI_GREEN + "GN";
            case Color.RED -> Colors.ANSI_RED + "RD";
            case Color.BLUE -> Colors.ANSI_BLUE + "BL";
            case Color.BLACK -> Colors.ANSI_BLACK + "BK";
            case Color.DUELER -> Colors.ANSI_DUELER + "MB";
            //noinspection UnnecessaryDefault
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
            //noinspection UnnecessaryDefault
            default -> throw new InputMismatchException("Unexpected color");
        } + Colors.ANSI_RESET;
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
        keyboard.nextLine();

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
            System.out.print(message);
            input = keyboard.nextLine();
        } while (!emptyAllowed && input.isEmpty());

        return input;
    }
}
