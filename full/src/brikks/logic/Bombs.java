package brikks.logic;

public class Bombs {
    public static final byte MAX_AMOUNT = 3;

    private final byte[] points;
    private byte amount;


    public Bombs() {
        this(Bombs.MAX_AMOUNT);
    }

    public Bombs(final byte amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }
        if (amount > Bombs.MAX_AMOUNT) {
            throw new IllegalArgumentException("Amount must be <= " + Bombs.MAX_AMOUNT);
        }

        this.points = Bombs.generatePoints();
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
