package brikks;

import brikks.save.PlayerSave;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.*;
import brikks.view.DuelAsk;
import brikks.view.PlayerAsk;

public class Player {
    public final String name;
    private boolean plays;

    private final PlayerSave saver;
    private final BonusScore bonusScore;
    private final Energy energy;
    private final Bombs bombs;
    private final Board board;


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
    }

    public Player(final PlayerSave saver, final String name, final boolean plays, final Board board, final Energy energy, final Bombs bombs, BonusScore bonusScore) {
        if (saver == null) {
            throw new IllegalArgumentException("Saver cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        if (energy == null) {
            throw new IllegalArgumentException("Energy cannot be null");
        }
        if (bombs == null) {
            throw new IllegalArgumentException("Bombs cannot be null");
        }
        if (bonusScore == null) {
            throw new IllegalArgumentException("BonusScore cannot be null");
        }


        this.saver = saver;
        this.name = name;
        this.plays = plays;
        this.board = board;
        this.energy = energy;
        this.bombs = bombs;
        this.bonusScore = bonusScore;
    }


    public PlayerSave getSaver() {
        return this.saver;
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


    public boolean canContinue(final BlocksTable blocks, final Position dieRoll) {
        // TODO: zroby canContinue()
        return this.plays;
    }

    // TODO: a tocxno treba?
    public byte turn(final PlayerAsk user, final BlocksTable blocks, final MatrixDice matrixDie) {
        if (user.askReroll()) {
            matrixDie.roll();
        }

        return this.turn(user, blocks, matrixDie.get());
    }

    public byte turn(final PlayerAsk user, final BlocksTable blocks, final Position roll) {
        while (user.askUseSpecial()) {
            switch (user.askSpecial()) {
                case BOMB -> {
                    if (this.bombs.canUse()) {
                        this.bombs.use();
                        return 0;
                    } else {
                        // I am allowing multiple failing
                        user.failBomb();
                    }
                }

                case ROTATE -> {
                    final byte rotation = user.askRotation(blocks.getRowOfBlocks(roll.getY()));
                    // I am allowing to rotate to the same state for no cost
                    if (this.energy.canSpend((byte) Math.abs(roll.getX() - rotation))) {
                        this.energy.spend((byte) Math.abs(roll.getX() - rotation));
                        roll.setX(rotation);
                    } else {
                        // I am allowing multiple failing
                        user.failRotation();
                    }
                }

                case CHOICE -> {
                    if (this.energy.canSpend((byte) 5)) {
                        this.energy.spend((byte) 5);
                        roll.set(user.askChoice(blocks));
                    } else {
                        // I am allowing multiple failing
                        user.failChoice();
                    }
                }

                //  case BACK -> /*nothing*/
            }
        }

        final Block block = blocks.getBlock(roll);
        final Position choice = user.askPlacingSpot(block, this.board.canBePlaced(block));

        return this.board.place(new PlacedBlock(block, choice));
    }

    public boolean duelTurn(final DuelAsk user, final Board opponentsBoard, byte amount) {
        for (; amount > 0; amount--) {
            final Position[] variants = this.board.canBePlaced();
            if (variants.length == 0) {
                return false;
            }

            final Position choice = user.askPlacingMiniblock(opponentsBoard, variants);
            opponentsBoard.opponentsPlace(choice);
        }

        return true;
    }

    public short calculateFinal() {
        return (byte) (this.bonusScore.calculateFinal() +
                this.energy.calculateFinal() +
                this.bombs.calculateFinal() +
                this.board.calculateFinal()
        );
    }

    public void save() {
        this.saver.save(this);
    }

    public void saveFinal() {
        this.saver.save(this.calculateFinal());
    }
}
