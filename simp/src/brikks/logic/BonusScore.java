package brikks.logic;

public class BonusScore {
    public static final byte MAXIMUM_SCALE = 14;
    private final byte[] SCALING_RATE;
    private byte scale;


    public BonusScore() {
        this.SCALING_RATE = generateScalingRate();
        this.scale = 0;
    }

    public BonusScore(byte[] scallingRate, byte scale) {
        this.SCALING_RATE = scallingRate;
        if (scale > MAXIMUM_SCALE) {
            this.scale = MAXIMUM_SCALE - 1;
        } else {
            this.scale = scale;
        }
    }

    private byte[] generateScalingRate() {
        return new byte[] {0, 1, 3, 6, 10, 15, 21, 28, 36, 44, 51, 58, 64, 70, 75};
    }

    public byte[] getScalingRate() {
        return SCALING_RATE;
    }

    public byte getScale() {
        return scale;
    }

    private void grow(byte amount) {
        if (scale + amount <= MAXIMUM_SCALE) {
            scale += amount;
        } else {
            scale = MAXIMUM_SCALE - 1;
        }
    }


    public void growByEnergy(final byte amount) {
        this.grow(amount);
    }

    public byte growByBoard(final byte rowsFilled) {
        final byte amount = switch (rowsFilled) {
            case 0, 1 -> 0;
//            case 2 -> 1;
//            case 3 -> 2;
            case 4 -> 4;
            default -> (byte) (rowsFilled - 1);
        };

        this.grow(amount);

        return amount;
    }


    public byte get() {
        return this.SCALING_RATE[this.scale];
    }

    public byte getNext() {
        if (this.scale == BonusScore.MAXIMUM_SCALE - 1) {
            return -1;
        }

        return this.SCALING_RATE[this.scale + 1];
    }


    public short calculateFinal() {
        return (short) SCALING_RATE[scale];
    }
}
