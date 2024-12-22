package brikks.essentials;

import java.util.Random;

public class Dice {
    private final static Random rand = new Random();
    public final byte sides;
    private byte value;


    public Dice(final byte sides) {
        this.sides = sides;
        this.value = 0;
    }


    public byte get() {
        return this.value;
    }


    public byte roll() {
        this.value = (byte) Dice.rand.nextInt(this.sides);
        return this.value;
    }

    public void cheat(final byte value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%d-sided (%d)", this.sides, this.value);
    }
}
