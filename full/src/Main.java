import brikks.BlocksTable;
import brikks.essentials.ASCIIColor;
import brikks.essentials.Block;
import brikks.essentials.Position;

public class Main {
    public static void main(String[] args) {

    }

    private Block[][] generateBlocksTable() {
        Block[][] blocks = new Block[BlocksTable.height][BlocksTable.width];


        // W - White
        blocks[0][0] = new Block(
                new Position[] {
                        new Position((byte) (byte) 1, (byte) 0),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -1, (byte) -1),
                },
                new ASCIIColor('W')
        );
        blocks[0][1] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -1, (byte) -1),
                },
                new ASCIIColor('W')
        );
        blocks[0][2] = new Block(
                new Position[] {
                        new Position((byte) -1, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('W')
        );
        blocks[0][3] = new Block(
                new Position[] {
                        new Position((byte) -1, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('W')
        );

        // Y - Yellow
        blocks[1][0] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('Y')
        );
        blocks[1][1] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -2, (byte) -1),
                },
                new ASCIIColor('Y')
        );
        blocks[1][2] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('Y')
        );
        blocks[1][3] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) -1, (byte) 0),
                        new Position((byte) -1, (byte) 0),
                },
                new ASCIIColor('Y')
        );

        // G - Green
        blocks[2][0] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -1, (byte) -1),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('G')
        );
        blocks[2][1] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('G')
        );
        blocks[2][2] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) -1, (byte) 0),
                },
                new ASCIIColor('G')
        );
        blocks[2][3] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('G')
        );

        // R - Red
        blocks[3][0] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('R')
        );
        blocks[3][1] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('R')
        );
        blocks[3][2] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -2, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('R')
        );
        blocks[3][3] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -2, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('R')
        );

        // B - Blue
        blocks[4][0] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) -1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('B')
        );
        blocks[4][1] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) -1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('B')
        );
        blocks[4][2] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('B')
        );
        blocks[4][3] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('B')
        );

        // F - Black
        blocks[5][0] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -1, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('F')
        );
        blocks[5][1] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) -1, (byte) -1),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('F')
        );
        blocks[5][2] = new Block(
                new Position[] {
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 0, (byte) -1),
                        new Position((byte) 0, (byte) -1),
                },
                new ASCIIColor('F')
        );
        blocks[5][3] = new Block(
                new Position[] {
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 1, (byte) 0),
                        new Position((byte) 1, (byte) 0),
                },
                new ASCIIColor('F')
        );


        return blocks;
    }

}
