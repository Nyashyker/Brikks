package brikks.save;


import brikks.Player;


public class EmptyPlayerSave extends PlayerSave {
    public EmptyPlayerSave() { super(null); }

    @Override
    public void save(Player player, byte playerOrder) {}
    @Override
    public void setDuration() {}
    @Override
    public void updateDuration() {}
    @Override
    public void update(Player player) {}
    @Override
    public void save(short score) {}
}
