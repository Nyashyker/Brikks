package be.kdg.integration.brikks_project;

public class Energy {
    public static final byte MAXIMUM = 28;
    private final boolean[] BONUSES;
    private byte position;
    private byte available;
    private BonusScore bonusScore;

    public Energy(BonusScore bonusScore, byte playerCount) {
        this.BONUSES = new boolean[playerCount];
        generateBonuses();
        this.position = 0;
        this.available = 0;
        this.bonusScore = bonusScore;
    }

    public Energy(BonusScore bonusScore, boolean[] bonuses, byte position, byte available) {
        this.BONUSES = bonuses;
        this.position = position;
        if (this.position >= MAXIMUM) {
            this.position = MAXIMUM - 1;
        }
        this.available = available;
        this.bonusScore = bonusScore;
    }

    public boolean[] generateBonuses() {
        for (int i = 6; i < MAXIMUM; i += 3) {
            BONUSES[i] = true;
        }
        return BONUSES;
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
        byte bonusesEncountered = 0;

        if (position == MAXIMUM - 1) {
            return bonusesEncountered;
        }
        if (position + amount >= MAXIMUM) {
            amount = (byte) (MAXIMUM - position - 1);
        }


        for (int i = position; i < position + amount; i++) {
            if (BONUSES[i] ) {
                bonusesEncountered++;
            }
        }
        position += amount;
        available += amount;

        bonusScore.grow(bonusesEncountered);
        return bonusesEncountered;
    }

    public short calculateFinal() {
        return (short) (available / 2);
    }
}