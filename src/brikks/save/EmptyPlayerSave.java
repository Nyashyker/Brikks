package brikks.save;


import brikks.Player;


public class EmptyPlayerSave implements PlayerSave {
    public EmptyPlayerSave() {}

    @Override
    public void save(Player player, byte playerOrder) {}

    @Override
    public void update(Player player) {}

    @Override
    public void save(short score) {}
}
