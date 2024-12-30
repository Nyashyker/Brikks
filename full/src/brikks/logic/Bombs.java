package brikks.logic;

public class Bombs {
    public final static byte MAX_AMOUNT = 3;

    private final byte[] points;
    private byte amount;


    public Bombs() {
        this(Bombs.generatePoints(), (byte) 3);
    }

    public Bombs(byte[] points, byte amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }

        this.points = points;
        this.amount = amount;
    }

    private static byte[] generatePoints() {
        byte[] points = new byte[Bombs.MAX_AMOUNT];

        points[0] = 1;
        for (byte i = 1; i < Bombs.MAX_AMOUNT; i++) {
            points[i] = (byte) (i * 2);
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
        if (!this.canUse()) {
            throw new IllegalCallerException("You supposed to check on availability of bombs!");
        }

        this.amount--;
    }

    public short calculateFinal() {
        short score = 0;
        for (byte i = (byte) (Bombs.MAX_AMOUNT - this.amount); i < Bombs.MAX_AMOUNT; i++) {
            score += this.points[i];
        }

        return score;
    }


    @Override
    public String toString() {
        final StringBuilder scores = new StringBuilder();

        for (byte i = (byte) (Bombs.MAX_AMOUNT - this.amount); i < Bombs.MAX_AMOUNT; i++) {
            scores.append(this.points[i]);
            scores.append(' ');
        }
        scores.delete(scores.length() - 1, scores.length());

        return scores.toString();
    }
}
