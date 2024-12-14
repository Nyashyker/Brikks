package brikks.logic;

public class BonusScore {
    public static final byte MAXIMUM_SCALE = 14;
    private final byte[] scallingRate;
    private byte scale;


    public BonusScore() {
        this(BonusScore.generateScalingRate(), (byte) 0);
    }

    public BonusScore(byte[] scalingRate, byte scale) {
        this.scallingRate = scalingRate;
        this.scale = scale;
    }

    private static byte[] generateScalingRate() {
        return new byte[] {0, 1, 3, 6, 10, 15, 21, 28, 36, 44, 51, 58, 64, 70, 75};
    }


    public byte[] getScalingRate() {
        return this.scallingRate;
    }

    public byte getScale() {
        return this.scale;
    }


    public void grow(byte amount) {
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
