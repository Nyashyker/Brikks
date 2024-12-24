import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.*;
import brikks.save.*;
import brikks.view.*;
import brikks.view.container.GameText;

import java.util.LinkedList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        final GameText t = new GameText(
                "Назад",
                "Уведіть будь-що, щоб перейти в головне меню. ",
                "Оберіть (%d-%d): ",
                "Уведіть число з указаного діапазону!",
                "МЕНЮ",
                new String[]{
                        "Нова гра",
                        "Завантажити",
                        "Таблиця лідерів",
                        "Вийти"
                },
                "ТАБЛИЦЯ ЛІДЕРІВ",
                "Гравець із таким ім'ям уже існує. Бажаєте використати його?",
                "Оберіть рівень складности:",
                new String[]{
                        "Перший: 1 ОЕ дається навіть за накриття неправильним кольором",
                        "Другий: 2 ОЕ дається за накриття правильним кольором (стандартний)",
                        "Третій: 1 ОЕ відбирається за накриття неправильним кольором",
                        "Четвертий: 2 ОЕ відберається за накриття неправильним кольором",
                },
                "Хочете провести дуель? ",
                "Скільки гравців гратиме? ",
                "Оберіть ім'я гравцю: ",
                "Оберіть зебереження:",
                "Додаткові бали: %d (-> %d)",
                "Очки енергії: %d (+ДБ -> %d)",
                "Бомби: %d (= %d бал.)",
                "\tГру завершено!!!",
                "%s - %s - %d\n",
                new String[]{
                        "Перший раз?",
                        "Ти можеш більше!",
                        "Молодець!",
                        "А хтось наловчився!",
                        "Насолоджуйся своїми здобутками!",
                        "Ти це бачив?",
                        "Та ти вже певно професіонал!",
                        "От хвалько!",
                        "МАЙСТЕР-БЛОКАЙСТЕР 2000!!!",
                },
                "Підсумки:",
                "%s переміг %s\n",
                "Вихід..."
        );

        final Player p = new Player(new EmptyPlayerSave(), "Nyashyker", (byte) 1, Level.TWO);
        final View v = new ConsoleView(t, "\n\n\t\tBRIKKS\n\n");

        v.draw(p);
/*
        final Brikks game = new Brikks(new ConsoleView(), new EmptySave(), generateBlocksTable());
        game.start();
*/
    }

    public static Block[][] generateBlocksTable() {
        Block[][] blocks = new Block[BlocksTable.HEIGHT][BlocksTable.WIDTH];


        // White
        blocks[0][0] = new Block(
                mkShape("    " +
                        "    " +
                        " x  " +
                        "xxx "
                ), Color.WHITE);

        blocks[0][1] = new Block(
                mkShape("    " +
                        " x  " +
                        " xx " +
                        " x  "
                ), Color.WHITE);

        blocks[0][2] = new Block(
                mkShape("    " +
                        "    " +
                        "xxx " +
                        " x  "
                ), Color.WHITE);

        blocks[0][3] = new Block(
                mkShape("    " +
                        " x  " +
                        "xx  " +
                        " x  "
                ), Color.WHITE);

        // Yellow
        blocks[1][0] = new Block(
                mkShape("    " +
                        " x  " +
                        " x  " +
                        "xx  "
                ), Color.YELLOW);

        blocks[1][1] = new Block(
                mkShape("    " +
                        "    " +
                        "x   " +
                        "xxx "
                ), Color.YELLOW);

        blocks[1][2] = new Block(
                mkShape("    " +
                        "xx  " +
                        "x   " +
                        "x   "
                ), Color.YELLOW);

        blocks[1][3] = new Block(
                mkShape("    " +
                        "    " +
                        "xxx " +
                        "  x "
                ), Color.YELLOW);

        // Green
        blocks[2][0] = new Block(
                mkShape("    " +
                        "x   " +
                        "x   " +
                        "xx  "
                ), Color.GREEN);

        blocks[2][1] = new Block(
                mkShape("    " +
                        "    " +
                        "xxx " +
                        "x   "
                ), Color.GREEN);

        blocks[2][2] = new Block(
                mkShape("    " +
                        "xx  " +
                        " x  " +
                        " x  "
                ), Color.GREEN);

        blocks[2][3] = new Block(
                mkShape("    " +
                        "    " +
                        "  x " +
                        "xxx "
                ), Color.GREEN);

        // Red
        blocks[3][0] = new Block(
                mkShape("    " +
                        " x  " +
                        "xx  " +
                        "x   "
                ), Color.RED);

        blocks[3][1] = new Block(
                mkShape("    " +
                        " x  " +
                        "xx  " +
                        "x   "
                ), Color.RED);

        blocks[3][2] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        " xx "
                ), Color.RED);

        blocks[3][3] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        " xx "
                ), Color.RED);

        // Blue
        blocks[4][0] = new Block(
                mkShape("    " +
                        "x   " +
                        "xx  " +
                        " x  "
                ), Color.BLUE);

        blocks[4][1] = new Block(
                mkShape("    " +
                        "x   " +
                        "xx  " +
                        " x  "
                ), Color.BLUE);

        blocks[4][2] = new Block(
                mkShape("    " +
                        "    " +
                        " xx " +
                        "xx  "
                ), Color.RED);

        blocks[4][3] = new Block(
                mkShape("    " +
                        "    " +
                        " xx " +
                        "xx  "
                ), Color.BLUE);

        // Black
        blocks[5][0] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        "xx  "
                ), Color.BLUE);

        blocks[5][1] = new Block(
                mkShape("    " +
                        "    " +
                        "xx  " +
                        "xx  "
                ), Color.BLUE);

        blocks[5][2] = new Block(
                mkShape("    " +
                        "    " +
                        "    " +
                        "xxxx"
                ), Color.BLUE);

        blocks[5][3] = new Block(
                mkShape("x   " +
                        "x   " +
                        "x   " +
                        "x   "
                ), Color.BLUE);


        return blocks;
    }


    public static Position[] mkShape(final String shape) {
        return mkShape(shape, 'x', (byte) 4);
    }

    public static Position[] mkShape(final String shape, final char color, final byte block_length) {
        final Position start = getShapeStart(shape, color, block_length);

        final List<Position> block = new LinkedList<Position>();
        for (byte y = (byte) (block_length - 1); y >= 0; y--) {
            for (byte x = 0; x < block_length; x++) {
                if (shape.charAt(y * block_length + x) == color) {
                    block.add(new Position((byte) (x - start.getX()), (byte) (y - start.getY())));
                }
            }
        }
        block.removeFirst();

        return block.toArray(Position[]::new);
    }

    private static Position getShapeStart(String shape, char color, final byte block_length) {
        if (shape.length() % block_length != 0) {
            throw new IllegalArgumentException("Shape must be exact square with side length of block_length");
        }

        Position start = null;

        for (byte y = (byte) (block_length - 1); y >= 0; y--) {
            for (byte x = 0; x < block_length; x++) {
                if (shape.charAt(y * block_length + x) == color) {
                    start = new Position(x, (byte) (block_length - 1));
                    break;
                }
            }
        }

        if (start == null) {
            throw new IllegalArgumentException("Shape " + shape + " not found\nUse " + color + " to describe the shape");
        }

        return start;
    }
}
