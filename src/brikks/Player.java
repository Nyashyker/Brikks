package brikks;

import brikks.container.TurnsResults;
import brikks.essentials.Block;
import brikks.essentials.MatrixDice;
import brikks.essentials.PlacedBlock;
import brikks.essentials.Position;
import brikks.essentials.enums.Level;
import brikks.logic.Bombs;
import brikks.logic.BonusScore;
import brikks.logic.Energy;
import brikks.logic.board.Board;
import brikks.save.PlayerSave;
import brikks.view.DuelAsk;
import brikks.view.PlayerAsk;

import java.time.LocalTime;
import java.util.function.Supplier;


public class Player implements Comparable<Player> {
    public final String name;
    private boolean plays;

    private final PlayerSave saver;
    private final BonusScore bonusScore;
    private final Energy energy;
    private final Bombs bombs;
    private final Board board;

    private Supplier<Short> finalCalculator;


    public Player(final PlayerSave saver, final String name, final byte playerCount, final Level difficulty) {
        if (saver == null) {
            throw new IllegalArgumentException("Saver cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (playerCount <= 0) {
            throw new IllegalArgumentException("Player count must be greater than 0");
        }
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }

        this.saver = saver;
        this.name = name;
        this.plays = true;
        this.bonusScore = new BonusScore();
        this.energy = new Energy(this.bonusScore, playerCount);
        this.bombs = new Bombs();
        this.board = new Board(this.bonusScore, this.energy, difficulty);

        this.finalCalculator = () -> (short) (
                this.bonusScore.calculateFinal()
                        + this.energy.calculateFinal()
                        + this.bombs.calculateFinal()
                        + this.board.calculateFinal()
        );
    }

    public Player(
            final PlayerSave saver,
            final String name,
            final boolean plays,
            final Board board,
            final Energy energy,
            final Bombs bombs,
            final BonusScore bonusScore
    ) {
        if (saver == null) {
            throw new IllegalArgumentException("Saver cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }


        this.saver = saver;
        this.name = name;
        this.plays = plays;
        this.board = board;
        this.energy = energy;
        this.bombs = bombs;
        this.bonusScore = bonusScore;

        this.finalCalculator = () -> (short) (
                this.bonusScore.calculateFinal()
                        + this.energy.calculateFinal()
                        + this.bombs.calculateFinal()
                        + this.board.calculateFinal()
        );
    }


    public boolean isPlays() {
        return this.plays;
    }

    public BonusScore getBonusScore() {
        return bonusScore;
    }

    public Energy getEnergy() {
        return this.energy;
    }

    public Bombs getBombs() {
        return this.bombs;
    }

    public Board getBoard() {
        return this.board;
    }

    public PlayerSave getSaver() {
        return this.saver;
    }


    public void firstChoice(final Block block) {
        if (block == null) {
            throw new IllegalArgumentException("Block cannot be null");
        }

        final Position[] variants = this.board.canBePlaced(block);
        final byte index = (byte) (LocalTime.now().getNano() % variants.length);
        final PlacedBlock placed = new PlacedBlock(block, variants[index]);

        this.board.place(placed, false);
    }

    public TurnsResults turn(final PlayerAsk user, final GameSave gameSave, final BlocksTable blocks, final MatrixDice matrixDie) {
        if (user.askReroll(blocks.getBlock(matrixDie.get()))) {
            matrixDie.roll();
        }

        return this.turn(user, gameSave, blocks, matrixDie.get());
    }

    public TurnsResults turn(final PlayerAsk user, final GameSave gameSave, final BlocksTable blocks, final Position roll) {
        while (true) {
            final Block block = blocks.getBlock(roll);
            final Position[] variants = this.board.canBePlaced(block);
            final boolean canGiveUp = variants.length == 0;

            switch (user.askDeed(block)) {
                case BOMB -> {
                    if (this.bombs.canUse()) {
                        this.bombs.use();
                        user.successBomb();
                        return new TurnsResults(false, false, (byte) 0);
                    } else {
                        user.failBomb();
                    }
                }

                case ROTATE -> {
                    final byte rotation = user.askRotation(blocks.getRowOfBlocks(roll.getY()));
                    if (rotation == -1) {
                        break;
                    }
                    final byte energyCost = (byte) Math.abs(roll.getX() - rotation);

                    if (energyCost != 0 && this.energy.canSpend(energyCost)) {
                        this.energy.spend(energyCost);
                        roll.setX(rotation);

                        user.successRotation(energyCost);
                    } else {
                        user.failRotation();
                    }
                }

                case CHOICE -> {
                    final byte energyCost = 5;
                    if (this.energy.canSpend(energyCost)) {
                        this.energy.spend(energyCost);
                        final Position choice = user.askChoice(blocks.getTable());
                        if (choice == null) {
                            break;
                        }
                        roll.set(choice);

                        user.successChoice(energyCost);
                    } else {
                        user.failChoice();
                    }
                }

                case PLACE -> {
                    if (!canGiveUp) {
                        final Position choice = user.askPlacingSpot(this, block, variants);
                        if (choice == null) {
                            break;
                        }
                        final PlacedBlock placed = new PlacedBlock(block, choice);

                        final byte duelBonus = this.board.place(placed, true);
                        user.successPlace(this, placed);
                        return new TurnsResults(false, false, duelBonus);
                    } else {
                        user.failPlace();
                    }
                }

                case GIVE_UP -> {
                    if (canGiveUp) {
                        user.fail();
                        return new TurnsResults(false, true, (byte) 0);
                    } else {
                        user.failGiveUp();
                    }
                }

                case SAVE -> {
                    gameSave.save(roll);
                }

                case EXIT -> {
                    return new TurnsResults(true, false, (byte) 0);
                }
            }
        }
    }

    public boolean duelTurn(final DuelAsk user, final Player opponent, byte amount) {
        /*
         * -> true  : win
         */

        for (; amount > 0; amount--) {
            final Position[] variants = opponent.board.canBePlaced(BlocksTable.duelBlock);
            if (variants.length == 0) {
                return true;
            }

            final Position choice = user.askPlacingMiniblock(opponent, variants);
            opponent.board.place(new PlacedBlock(BlocksTable.duelBlock, choice), false);
        }

        return false;
    }


    public void save(final BlocksTable blocksTable, final byte order) {
        this.saver.save(blocksTable,this, order);
    }

    public void update(final BlocksTable blocksTable) {
        this.saver.update(this, blocksTable);
    }


    public short calculateFinal() {
        return this.finalCalculator.get();
    }

    public void saveFinal() {
        this.plays = false;
        this.saver.save(this.calculateFinal());
    }

    // Duel final
    public void saveFinal(final boolean hasWon) {
        if (hasWon) {
            this.finalCalculator = () -> Short.MAX_VALUE;
        } else {
            this.finalCalculator = () -> (short) 0;
        }

        this.saveFinal();
    }


    public void begin() {
        this.saver.setDuration();
    }

    public void end() {
        this.saver.updateDuration();
    }


    @Override
    public int compareTo(final Player other) {
        return this.calculateFinal() - other.calculateFinal();
    }
}
