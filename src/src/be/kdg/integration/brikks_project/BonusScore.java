package be.kdg.integration.brikks_project;

public class BonusScore {
    public static final byte MAXIMUM_SCALE = 14;
    private final byte[] SCALLING_RATE;
    private byte scale;


    public BonusScore(PlayerSave save) {
        this.SCALLING_RATE = generateScallingRate();
        this.scale = 0;
    }

    public BonusScore(PlayerSave save, byte[] scallingRate, byte scale) {
        this.SCALLING_RATE = scallingRate;
        this.scale = scale;
    }

    private byte[] generateScallingRate() {
        return new byte[] {0, 1, 3, 6, 10, 15, 21, 28, 36, 44, 51, 58, 64, 70, 75};
    }

    public byte[] getScallingRate() {
        return SCALLING_RATE;
    }

    public byte getScale() {
        return scale;
    }

    public void grow(byte amount) {
        if (scale + amount <= MAXIMUM_SCALE) {
            scale += amount;
        } else {
            scale = MAXIMUM_SCALE;
        }
    }


    public short calculateFinal() {
        return (short) SCALLING_RATE[scale];
    }
}

