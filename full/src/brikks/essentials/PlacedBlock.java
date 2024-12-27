package brikks.essentials;

public class PlacedBlock extends Block {
    protected final Position position;


    public PlacedBlock(final Block block) {
        super(block);
        this.position = new Position();
    }

    public PlacedBlock(final Block block, final Position position) {
        super(block);
        this.position = position;
    }


    @Override
    public Position[] getBlock() {
        Position[] block = new Position[this.shape.length];

        block[0] = this.position;
        for (int i = 1; i < this.shape.length; i++) {
            block[i] = this.shape[i].add(this.position);
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
