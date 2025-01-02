import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.*;
import brikks.save.*;
import brikks.view.*;
import brikks.view.container.GameText;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        try (
                final DatabaseConnection db = new DatabaseConnection(
                        "jdbc:postgresql://localhost:5432/brikks",
                        "postgres",
                        "Student_1234"
                )
        ) {

            db.write("DROP TABLE IF EXISTS players");
            db.write("CREATE TABLE IF NOT EXISTS players (name VARCHAR(16), score INTEGER)");
            db.write("INSERT INTO players (name, score) VALUES ('Lars', 9001)");

            final ResultSet rs = db.read("SELECT name, score FROM players");
            while (rs.next()) {
                String name = rs.getString(1);
                int score = rs.getInt(2);

                System.out.println(name + " " + score);
            }

        } catch (SQLException e) {
            System.out.println("Помилочка");
            System.out.println(e.getMessage());
        }
    }

    private static void runGame() {
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
                    "НЕМА",
                    "Додаткові бали: %d (-> %s)",
                    "Очки енергії: %d (+ДБ -> %s)",
                    "Бомби: %d (= %d бал.)",
                    "\tГру завершено!!!",
                    "%s - %s - %d\n",
                    new String[]{
                            "Перший раз?",
                            "Ти можеш більше!",
                            "Молодець!",
                            "А хтось набив руку!",
                            "Насолоджуйся своїми здобутками!",
                            "Ти це бачив?",
                            "Та тобі треба в професіонали йти!",
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
                    new String[]{
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
                    "NONE",
                    "Additional points: %d (-> %s)",
                    "Energy points: %d (+AP -> %s)",
                    "Bombs: %d (= %d pts)",
                    "\tGame over!!!",
                    "%s - %s - %d\n",
                    new String[]{
                            "Your first time?",
                            "You can do more!",
                            "Well done!",
                            "Someone has practiced!",
                            "Enjoy your success!",
                            "Have you seen this?",
                            "You should be doing this professionally!",
                            "You Bragger!",
                            "Brikks-Master 2000!!!",
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
        final GameText textBg;
        {
            textBg = new GameText(
                    "Назад",
                    "Натиснете клавиш, за да влезнете в главното меню.",
                    "Изберете (%d-%d)",
                    "Въведете число от показаният диапазон",
                    "МЕНЮ",
                    new String[]{
                            "Нова игра",
                            "Продължи",
                            "Класация",
                            "Изход"
                    },
                    "КЛАСАЦИЯ",
                    "Играч с такова име вече съществува. Искате ли да го използвате?",
                    "Изберете трудност:",
                    new String[]{
                            "Първо: 1 ЕТ се дава дори и при грешно покриване на цвят",
                            "Второ: 2 ЕТ се дават за правилно покрит цвят (стандарт)",
                            "Трето: 1 ЕТ се маха при грешно покриване на цвят",
                            "Четвърто: 2 ЕТ се махат при грешно пориване на цвят"
                    },
                    "Искате ли дуел?",
                    "Колко играчи? (0 = назад): ",
                    "Изберете име #%d (нищо = назад): ",
                    "Избете запазване: ",
                    "НЯМА",
                    "Допълнителни точки: %d (-> %s)",
                    "Точки от енергията",
                    "Бомби: %d (= %d т)",
                    "\tКрай на играта!!!",
                    "%s - %s - %d\n",
                    new String[]{
                            "Първият ти път?",
                            "Можеш повече!",
                            "Добра работа!",
                            "Някой се е упражнявал!",
                            "Наслади се на успехът ти!",
                            "Виждал ли си го?",
                            "Трябва да играеш на професионално ниво!",
                            "Самохвалко!",
                            "Майстор на Brikks 2000!!!"
                    },
                    "Резюме",
                    "%s победен %s\n",
                    "Излезане...",
                    "Искате ли да хвърлите зарчето отново?",
                    "колона=%\tред=%d",
                    "Къде искате да поставите блока?:",
                    "Вашите действия:",
                    new String[]{
                            "Използвайте бомба",
                            "Завъртете блока",
                            "Изберете друг блок",
                            "Поставете блока",
                            "Отказване",
                            "Запазване",
                            "Изход към главното меню"
                    },
                    "Изберете начин на завъртане на блока: ",
                    "Изберете колонен блок (0 = назад): ",
                    "Изберете редови блок (0 = назад): ",
                    "Блокът няма къде да се постави!",
                    "Свършиха бомбите!",
                    "Нямате достатъчно енергия да завъртите блокът!",
                    "Нямате достатъчно енергия да изберете нов блок!",
                    "Какви ги вършите?! Все още има място да се сложи блок!!!",
                    "Играта приключи за тебе.",
                    "Изберете място за миниблокът на съперната дъска: "

            );
        }
        // TODO: design normal logo
        final String logo = "BRIKKS";

        final Brikks game = new Brikks(new ConsoleView(textUkr, logo), new EmptySave(), generateBlocksTable());
        game.menu();
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
