package brikks.logic;

public class Energy {
    public static final byte MAXIMUM = 28;

    private final boolean[] bonuses;
    private byte position;
    private byte available;

    private final BonusScore bonusScore;


    public Energy(final BonusScore bonusScore, final byte playerCount) {
        this(bonusScore, Energy.generateBonuses(), Energy.calculateStartingPosition(playerCount), Energy.calculateStartingAvailable(playerCount));
    }

    public Energy(final BonusScore bonusScore, final boolean[] bonuses, final byte position, final byte available) {
        this.bonuses = bonuses;
        this.position = position;
        this.available = available;

        this.bonusScore = bonusScore;
    }

    public static boolean[] generateBonuses() {
        boolean[] bonuses = new boolean[Energy.MAXIMUM];
        for (byte i = 0; i < Energy.MAXIMUM; i++) {
            bonuses[i] = false;
        }

        for (byte i = 6; i < Energy.MAXIMUM; i += 3) {
            bonuses[i] = true;
        }

        return bonuses;
    }

    private static byte calculateStartingAvailable(final byte playerCount) {
        if (playerCount <= 0) {
            throw new IllegalArgumentException("Player count must be greater than zero");
        }

        return playerCount == 1 ? (byte) 5 : (byte) 4;
        /*switch (playerCount) {
            case 1 -> 5;
            default -> 4;
        };*/
    }

    private static byte calculateStartingPosition(final byte playerCount) {
        if (playerCount <= 0) {
            throw new IllegalArgumentException("Player count must be greater than zero");
        }

        return switch (playerCount) {
            case 1 -> 5;
            // case 2 -> 4;
            case 3 -> 3;
            case 4 -> 2;
            default -> 4;
        };
    }


    public boolean[] getBonuses() {
        return this.bonuses;
    }

    public byte getPosition() {
        return this.position;
    }

    public byte getAvailable() {
        return this.available;
    }


    public boolean canSpend(final byte amount) {
        return this.available >= amount;
    }

    public void spend(final byte amount) {
        if (canSpend(amount)) {
            this.available -= amount;
        } else {
            throw new IllegalArgumentException("Do not have enough energy to spend");
        }
    }

    public byte grow(byte amount) {
        if (amount < 0) {
            this.shorten((byte) -amount);
            return 0;
        }

        if (this.position == Energy.MAXIMUM - 1) {
            return 0;
        }
        if (this.position + amount > Energy.MAXIMUM - 1) {
            amount = (byte) (Energy.MAXIMUM - this.position);
        }

        this.available += amount;

        byte grow = 0;
        for (byte i = this.position; i < this.position + amount; i++) {
            if (this.bonuses[i]) {
                grow++;
            }
        }

        this.position += amount;
        this.bonusScore.growByEnergy(grow);
        return grow;
    }

    public short calculateFinal() {
        return (short) (this.available / 2);
    }


    private void shorten(final byte amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The amount has to be greater than zero");
        }

        this.position -= amount;
        if (this.position < 0) {
            this.position = 0;
        }

        this.available -= amount;
        if (this.available < 0) {
            this.available = 0;
        }
    }
}
