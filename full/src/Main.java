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
        final GameText textUkr;
        // Defining text
        {
            textUkr = new GameText(
                    "Назад",
                    "Уведіть будь-що, щоб перейти в головне меню.",
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
                    "Гравець із таким ім'ям уже існує. Бажаєте використати його? ",
                    "Оберіть рівень складности:",
                    new String[]{
                            "Перший: 1 ОЕ дається навіть за накриття неправильним кольором",
                            "Другий: 2 ОЕ дається за накриття правильним кольором (стандартний)",
                            "Третій: 1 ОЕ відбирається за накриття неправильним кольором",
                            "Четвертий: 2 ОЕ відберається за накриття неправильним кольором",
                    },
                    "Хочете провести дуель? ",
                    "Скільки гравців гратиме? (0 = назад)",
                    "Оберіть ім'я гравцю №%d (нічого = назад): ",
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
                    "Хочете перекинути кубик? ",
                    "стовпець=%d\tряд=%d",
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
                    "Оберіть варіацію оберту блоку:",
                    "Оберіть стовпець блоку (0 = назад): ",
                    "Оберіть ряд блоку (0 = назад): ",
                    "Цей блок немає куди ставити!",
                    "Бомб не залишилося!",
                    "Недостатньо енергії, щоб повернути болок",
                    "Недостатньо енергії, щоб замінити блок!",
                    "Ану не тойво мені тут!!! Ще є куди ставити блок!",
                    "Для вас гра завершена.",
                    "Оберіть місце для мініатюрного блоку на дошці опонента:"
            );
        }
        final GameText textAngl;
        // Defining text
        {
            textAngl = new GameText(
                    "Back",
                    "Enter anything to go to the main menu.",
                    "Choose (%d-%d)",
                    "Enter a number from the specified range!",
                    "MENU",
                    new String[]{
                            "New Game",
                            "Load",
                            "Leaderboard",
                            "Exit"
                    },
                    "LEADERBOARD",
                    "A player with this name already exists. Do you want to use it? ",
                    "Choose difficulty level:",
                    new String[]{
                            "First: 1 EP is given even for covering with the wrong color",
                            "Second: 2 EP is given for covering with the correct color (standard)",
                            "Third: 1 EP is deducted for covering with the wrong color",
                            "Fourth: 2 EP is deducted for covering with the wrong color",
                    },
                    "Do you want to duel? ",
                    "How many players will play? (0 = back)",
                    "Choose a name for player #%d (nothing = back): ",
                    "Choose a save:",
                    "Additional points: %d (-> %d)",
                    "Energy points: %d (+AP -> %d)",
                    "Bombs: %d (= %d pts)",
                    "\tGame over!!!",
                    "%s - %s - %d\n",
                    new String[]{
                            "First time?",
                            "You can do better!",
                            "Well done!",
                            "Someone's getting skilled!",
                            "Enjoy your achievements!",
                            "Did you see that?",
                            "You're probably a pro already!",
                            "What a bragger!",
                            "MASTER-BLOKASTER 2000!!!",
                    },
                    "Summary:",
                    "%s defeated %s\n",
                    "Exiting...",
                    "Do you want to reroll the dice? ",
                    "column=%\trow=%d",
                    "Where to place the block?:",
                    "Your actions:",
                    new String[]{
                            "Use a bomb",
                            "Rotate the block",
                            "Choose another block",
                            "Place the block",
                            "Give up",
                            "Save",
                            "Exit to the main menu",
                    },
                    "Choose block rotation variation:",
                    "Choose block column (0 = back): ",
                    "Choose block row (0 = back): ",
                    "This block has nowhere to be placed!",
                    "No bombs left!",
                    "Not enough energy to rotate the block!",
                    "Not enough energy to choose another block!",
                    "What are you doing?! There's still a place to put the block!!!",
                    "The game is over for you.",
                    "Choose a spot for the miniblock on the opponent's board:"
            );

        }
        // TODO: design normal logo
        final String logo = "BRIKKS";

        final Brikks game = new Brikks(new ConsoleView(textUkr, logo), new EmptySave(), generateBlocksTable());
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
