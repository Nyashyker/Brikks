package brikks.logic;

public class Bombs {
    public final static byte MAXIMUM = 3;

    private final byte[] points;
    private byte amount;


    public Bombs() {
        this(Bombs.generatePoints(), (byte) 3);
    }

    public Bombs(byte[] points, byte amount) {
        this.points = points;
        this.amount = amount;
    }

    private static byte[] generatePoints() {
        byte[] points = new byte[Bombs.MAXIMUM];

        for (byte i = 0; i < Bombs.MAXIMUM; i++) {
            points[i] = (byte) ((Bombs.MAXIMUM - i) * 2);
        }

        return points;
    }


    public byte get() {
        return this.amount;
    }

    public byte[] getPoints() {
        return this.points;
    }


    public boolean canUse() {
        return this.amount > 0;
    }

    public void use() {
        this.amount--;
    }

    public short calculateFinal() {
        short score = 0;
        for (byte i = 0; i < this.amount; i++) {
            score += this.points[i];
        }

        return score;
    }
}
