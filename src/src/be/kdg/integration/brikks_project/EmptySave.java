package be.kdg.integration.brikks_project;

public abstract class EmptySave {

    public abstract void save(Board board);
    public abstract void save(Bombs bombs);
    public abstract void save(Energy energy);
    public abstract void save(BonusScore bonusScore);
    public abstract void save(Player player);
    public abstract void save(byte row, byte column);
}
