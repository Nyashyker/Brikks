package brikks.logic;

public class BonusScore {
    public static final byte MAX_SCALE = 15;

    private final byte[] scalingRate;
    private byte scale;


    public BonusScore() {
        this((byte) 0);
    }

    public BonusScore(final byte scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Scale cannot be negative");
        }
        if (scale >= BonusScore.MAX_SCALE) {
            throw new IllegalArgumentException("scale must be < " + BonusScore.MAX_SCALE);
        }

        this.scalingRate = BonusScore.generateScalingRate();
        this.scale = scale;
    }

    public static byte[] generateScalingRate() {
        byte[] bonusScore = new byte[]{0, 1, 3, 6, 10, 15, 21, 28, 36, 44, 51, 58, 64, 70, 75};

        //noinspection ConstantValue
        if (bonusScore.length != BonusScore.MAX_SCALE) {
            throw new IllegalArgumentException("Generated scaling rate has to be" + BonusScore.MAX_SCALE + " long");
        }

        return bonusScore;
    }


    public byte getScale() {
        return this.scale;
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

    public short calculateFinal() {
        return this.scalingRate[this.scale];
    }

    public short calculateNextFinal() {
        if (this.scale == BonusScore.MAX_SCALE - 1) {
            return -1;
        }

        return this.scalingRate[this.scale + 1];
    }


    private void grow(byte amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("You can not grow negatively");
        }

        if (this.scale == BonusScore.MAX_SCALE - 1) {
            return;
        }
        if (this.scale + amount > BonusScore.MAX_SCALE - 1) {
            amount = (byte) (BonusScore.MAX_SCALE - this.scale);
        }

        this.scale += amount;
    }
}
