package be.simp;

public class PlacedBlock extends Block {
    protected final Position position;

    public PlacedBlock(Block block) {
        super(block);
        this.position = new Position(0, 0);
    }

    public PlacedBlock(final Block block, final Position position) {
        super(block);
        this.position = position;
    }


    public Position setPlace() {
        return this.position;
    }

    public void setPlace(final Position position) {
        this.position.set(position);
        this.position.set(position);
    }

    @Override
    public Position[] getBlock() {
        Position[] block = new Position[this.shape.length + 1];

        block[0] = this.position;
        for (int i = 1; i < Block.len; i++) {
            block[i] = this.shape[i];
        }

        return block;
    }
}
