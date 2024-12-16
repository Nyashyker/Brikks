package brikks.logic;

public class BonusScore {
    public static final byte MAXIMUM_SCALE = 15;
    private final byte[] scallingRate;
    private byte scale;


    public BonusScore() {
        this(BonusScore.generateScalingRate(), (byte) 0);
    }

    public BonusScore(byte[] scalingRate, byte scale) {
        if (scalingRate == null) {
            throw new NullPointerException("Scaling rate cannot be null");
        }
        if (scale < 0) {
            throw new IllegalArgumentException("Scale cannot be negative");
        }
        if (scale >= scalingRate.length) {
            throw new IllegalArgumentException("Scale cannot be greater than the scaling rate");
        }

        this.scallingRate = scalingRate;
        this.scale = scale;
    }

    private static byte[] generateScalingRate() {
        byte[] bonusScore = new byte[] {0, 1, 3, 6, 10, 15, 21, 28, 36, 44, 51, 58, 64, 70, 75};

        if (bonusScore.length != BonusScore.MAXIMUM_SCALE) {
            throw new IllegalArgumentException("Generated scaling rate has to be" + BonusScore.MAXIMUM_SCALE + " long");
        }

        return bonusScore;
    }


    public byte[] getScalingRate() {
        return this.scallingRate;
    }

    public byte getScale() {
        return this.scale;
    }


    public void grow(byte amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("You can not grow negatively");
        }

        if (this.scale == BonusScore.MAXIMUM_SCALE - 1) {
            return;
        }
        if (this.scale + amount > BonusScore.MAXIMUM_SCALE - 1) {
            amount = (byte) (BonusScore.MAXIMUM_SCALE - this.scale);
        }

        this.scale += amount;
    }

    public short calculateFinal() {
        return this.scallingRate[this.scale];
    }
}
