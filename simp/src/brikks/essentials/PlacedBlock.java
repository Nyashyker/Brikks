package brikks.essentials;

public class PlacedBlock extends Block {
    protected final Position position;


    public PlacedBlock(Block block) {
        super(block);
        this.position = new Position((byte) 0, (byte) 0);
    }

    public PlacedBlock(final Block block, final Position position) {
        super(block);
        this.position = position;
    }


    @Override
    public Position[] getBlock() {
        Position[] block = new Position[this.shape.length + 1];

        block[0] = this.position;
        for (int i = 1; i < Block.LEN; i++) {
            block[i] = this.shape[i - 1].add(this.position);
        }

        return block;
    }

    public Position getPlace() {
        return this.position;
    }

    public void setPlace(final Position position) {
        this.position.set(position);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", super.toString(), this.position);
    }
}
