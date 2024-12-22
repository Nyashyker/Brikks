package be.kdg.integration.brikks_project;

public class Bombs {
    public static final byte MAXIMUM = 3;
    private final byte[] points;
    private byte amount;


    public Bombs() {
        this.points = generatePoints();
        this.amount = MAXIMUM;
    }

    public Bombs(byte[] points, byte amount) {
        this.points = points;
        this.amount = amount;
    }

    private byte[] generatePoints() {
        return new byte[]{1, 2, 4};
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
        if (canUse()) {
            this.amount--;
        } else {
            throw new IllegalArgumentException("No bombs left");
        }
    }

    public short calculateFinal() {
        short bombsScore = 0;
        for (byte i = 0; i < (MAXIMUM - this.amount); i++) {
            bombsScore += this.points[i];
        }
        return bombsScore;
    }
}
