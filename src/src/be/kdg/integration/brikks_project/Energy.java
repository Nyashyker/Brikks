package be.kdg.integration.brikks_project;

public class Energy {
    public static final byte MAXIMUM = 28;
    private final boolean[] BONUSES;
    private byte position;
    private byte available;

    public Energy(PlayerSave save, BonusScore bonusScore, byte playerCount) {
        this.BONUSES = new boolean[playerCount];
        generateBonuses();
        this.position = 0;
        this.available = 0;
    }

    public Energy(PlayerSave save, BonusScore bonusScore, boolean[] bonuses, byte position, byte available) {
        this.BONUSES = bonuses;
        this.position = position;
        this.available = available;
    }

    public boolean[] generateBonuses() {
        for (int i = 6; i < MAXIMUM; i += 3) {

        }
    }

    public boolean[] getBonuses() {
        return BONUSES;
    }

    public byte getPosition() {
        return position;
    }

    public byte getAvailable() {
        return available;
    }

    public boolean canSpend(byte amount) {
        return available >= amount;
    }

    public void spend(byte amount) {
        if (canSpend(amount)) {
            available -= amount;
        } else {
            throw new IllegalArgumentException("Not enough energy to spend");
        }
    }

    public byte grow(byte amount) {
        byte initialAvailable = available;
        if (available + amount <= MAXIMUM) {
            available += amount;
        } else {
            available = MAXIMUM;
        }
        return (byte) (available - initialAvailable);
    }

    public short calculateFinal() {
        short total = 0;
        for (boolean bonus : BONUSES) {
            total += bonus ? 1 : 0;
        }
        total += available;
        return total;
    }
}