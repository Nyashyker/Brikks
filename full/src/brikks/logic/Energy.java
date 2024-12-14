package brikks.logic;

public class Energy {
    public static final byte MAXIMUM = 28;

    private final boolean[] bonuses;
    private byte position;
    private byte available;

    private final BonusScore bonusScore;


    public Energy(BonusScore bonusScore, byte playerCount) {
        this(bonusScore, Energy.generateBonuses(), Energy.calculateStartingPosition(playerCount), Energy.calculateStartingAvailable(playerCount));
    }

    public Energy(BonusScore bonusScore, boolean[] bonuses, byte position, byte available) {
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

    private static byte calculateStartingAvailable(byte playerCount) {
        return playerCount == 1 ? (byte) 5 : (byte) 4;
        /*switch (playerCount) {
            case 1 -> 5;
            default -> 4;
        };*/
    }

    private static byte calculateStartingPosition(byte playerCount) {
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


    public boolean canSpend(byte amount) {
        return this.available >= amount;
    }

    public void spend(byte amount) {
        this.available -= amount;
    }

    public byte grow(byte amount) {
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
        this.bonusScore.grow(grow);
        return grow;
    }

    public short calculateFinal() {
        return (short) (this.available / 2);
    }
}
