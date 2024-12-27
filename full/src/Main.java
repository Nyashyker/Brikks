import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.*;
import brikks.save.*;
import brikks.view.*;
import brikks.view.container.GameText;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        final GameText text;
        // Defining text
        // TODO: translate from Ukrainian
        {
            text = new GameText(
                    "Назад",
                    "Уведіть будь-що, щоб перейти в головне меню.",
                    "Оберіть (%d-%d)",
                    "Уведіть число з указаного діапазону!",
                    "МЕНЮ",
                    new String[]{
                            "Нова гра",
                            "Завантажити",
                            "Таблиця лідерів",
                            "Вийти"
                    },
                    "ТАБЛИЦЯ ЛІДЕРІВ",
                    "Гравець із таким ім'ям уже існує. Бажаєте використати його",
                    "Оберіть рівень складности:",
                    new String[]{
                            "Перший: 1 ОЕ дається навіть за накриття неправильним кольором",
                            "Другий: 2 ОЕ дається за накриття правильним кольором (стандартний)",
                            "Третій: 1 ОЕ відбирається за накриття неправильним кольором",
                            "Четвертий: 2 ОЕ відберається за накриття неправильним кольором",
                    },
                    "Хочете провести дуель",
                    "Скільки гравців гратиме? (0 = назад)",
                    "Оберіть ім'я гравцю (нічого = назад)",
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
                    "Вихід...",
                    "Хочете перекинути кубик",
                    "Де розмістити блок?:",
                    "Ваші дії:",
                    new String[] {
                            "Вористати бомбу",
                            "Повернути блок",
                            "Замінити блок",
                            "Поставити блок",
                            "Здатися",
                            "Зберегтися",
                            "Вийти в головне меню",
                    },
                    "Оберіть варіацію оберту блоку (0 = назад)",
                    "Оберіть стовпець блоку (0 = назад)",
                    "Оберіть ряд блоку (0 = назад)",
                    "Цей блок немає куди ставити!",
                    "Бомб не залишилося!",
                    "Недостатньо енергії, щоб повернути болок",
                    "Недостатньо енергії, щоб замінити блок!",
                    "Ще є куди ставити блок!!!",
                    "Для вас гра завершена.",
                    "Оберіть місце для мініатюрного блоку на дошці опонента"
            );
        }
        // TODO: design normal logo
        final String logo = "BRIKKS";

        /// !!! WARNING !!!
        // There are some strange problems with Scanner.nextLine()
        // I do not why, but it or going ahead, meanwhile I have not touched enter
        // or asks again without no reason
        final Brikks game = new Brikks(new ConsoleView(text, logo), new EmptySave(), generateBlocksTable());
        game.start();
    }

    public static Block[][] generateBlocksTable() {
        Block[][] blocks = new Block[BlocksTable.HEIGHT][BlocksTable.WIDTH];


        // White
        blocks[0][0] = mkBlock(
                "    " +
                        "    " +
                        " x  " +
                        "xxx "
                , Color.WHITE);
        blocks[0][1] = blocks[0][0].rotate();
        blocks[0][2] = blocks[0][1].rotate();
        blocks[0][3] = blocks[0][2].rotate();

        // Yellow
        blocks[1][0] = mkBlock(
                "    " +
                        " x  " +
                        " x  " +
                        "xx  "
                , Color.YELLOW);
        blocks[1][1] = blocks[1][0].rotate();
        blocks[1][2] = blocks[1][1].rotate();
        blocks[1][3] = blocks[1][2].rotate();

        // Green
        blocks[2][0] = mkBlock(
                "    " +
                        "x   " +
                        "x   " +
                        "xx  "
                , Color.GREEN);
        blocks[2][1] = blocks[2][0].rotate();
        blocks[2][2] = blocks[2][1].rotate();
        blocks[2][3] = blocks[2][2].rotate();

        // Red
        blocks[3][0] = mkBlock(
                "    " +
                        " x  " +
                        "xx  " +
                        "x   "
                , Color.RED);
        blocks[3][1] = blocks[3][0];
        blocks[3][2] = blocks[3][1].rotate();
        blocks[3][3] = blocks[3][2];

        // Blue
        blocks[4][0] = blocks[3][3].rotate(Color.BLUE);
        blocks[4][1] = blocks[4][0];
        blocks[4][2] = blocks[4][1].rotate();
        blocks[4][3] = blocks[4][2];

        // Black
        blocks[5][0] = mkBlock(
                "    " +
                        "    " +
                        "xx  " +
                        "xx  "
                , Color.BLACK);
        blocks[5][1] = blocks[5][0];
        blocks[5][2] = mkBlock(
                "    " +
                        "    " +
                        "    " +
                        "xxxx"
                , Color.BLACK);
        blocks[5][3] = blocks[5][2].rotate();


        return blocks;
    }


    public static Block mkBlock(final String shape, final Color color) {
        return new Block(mkShape(shape, 'x', Block.LEN), color);
    }

    public static Position[] mkShape(final String shape, final char color, final byte len) {
        final List<Position> block = new ArrayList<>();
        for (byte y = (byte) (len - 1); y >= 0; y--) {
            for (byte x = 0; x < len; x++) {
                if (shape.charAt(y * len + x) == color) {
                    block.add(new Position(x, (byte) (y - len + 1)));
                }
            }
        }

        return block.toArray(Position[]::new);
    }
}
