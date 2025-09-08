import brikks.BlocksTable;
import brikks.Brikks;
import brikks.essentials.enums.Color;
import brikks.essentials.enums.Level;
import brikks.logic.Bombs;
import brikks.logic.BonusScore;
import brikks.logic.Energy;
import brikks.logic.board.Board;
import brikks.save.DatabaseConnection;
import brikks.save.DatabaseSave;
import brikks.save.EmptySave;
import brikks.save.Save;
import brikks.view.ConsoleView;
import brikks.view.View;
import brikks.view.container.GameText;

import java.sql.SQLException;
import java.util.Locale;


public class Main {
    public static void main(final String[] args) {
        final GameText textUkr;
        // Defining Ukrainian text
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
                    """
                            Можливі параметри (записувати через пробіл у довільному порядку; будь-які пункти можуть бути пропущенні):
                            - <число>: обмеження на кількість рядків виводу
                            - imja=<ім'я гравця>: вибрати гравця (ставте \\ перед кожним пробілом у імені)
                            - skladnistq=<1-4>: вибрати складність
                            - gravci=<1-4>: вибрати кількість гравців
                            - duelq: вибрати режим дуелі (ігнорує вказану кількість гравців)
                            - rozvorot: вибрати результати з кінця
                            УВАГА! Це просунутий функціонал, неправильне поводження може призвести до неочікуваних наслідків, як зламу програми.
                            Уведіть запит: \
                            """,
                    "ТАБЛИЦЯ ЛІДЕРІВ",
                    "НІЦ",
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
                    "Ім'я занадто довге! Вкладіться в %d",
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
                    "Оберіть варіацію оберту блоку (0 = назад)",
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
        // Defining English text
        {
            textAngl = new GameText(
                    "Back",
                    "Enter anything to go to the main menu.",
                    "Choose (%d-%d): ",
                    "Enter a number from the specified range!",
                    "MENU",
                    new String[]{
                            "New Game",
                            "Load",
                            "Leaderboard",
                            "Exit"
                    },
                    """
                            Possible options (divide by space; any can be skipped):
                            - <number>: the limit of outputted lines
                            - imja=<the name of the player>: chose the player (add \\ before each space in the name)
                            - skladnistq=<1-4>: chose the difficulty
                            - gravci=<1-4>: chose player count
                            - duelq: see the duel mode (ignores the `gravci`)
                            - rozvorot: get results from the bottom
                            WARNING! It is an advanced feature, incorrect handling may cause unexpected results, such as the crush of the program.
                            Enter your input: \
                            """,
                    "LEADERBOARD",
                    "NONE",
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
                    "The name is too long! Use %d at most",
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
                    "column=%d\trow=%d",
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
                    "Choose block rotation variation (0 = back):",
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
        // Defining Bulgarian text
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
                    """
                            (Unfortunately, translation is not available)
                            Possible options (divide by space; any can be skipped):
                            - <number>: the limit of outputted lines
                            - imja=<the name of the player>: chose the player (add \\ before each space in the name)
                            - skladnistq=<1-4>: chose the difficulty
                            - gravci=<1-4>: chose player count
                            - duelq: see the duel mode (ignores the `gravci`)
                            - rozvorot: get results from the bottom
                            WARNING! It is an advanced feature, incorrect handling may cause unexpected results, such as the crush of the program.
                            Enter your input: \
                            """,
                    "КЛАСАЦИЯ",
                    "Нищо",
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
                    "Името е прекалено дълго! Използвайте %d най-много",
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
                    "колона=%d\tред=%d",
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
                    "Изберете начин на завъртане на блока (0 = назад)",
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


        final GameText text;
        if (args.length == 1) {
            text = switch (args[0].toUpperCase(Locale.ROOT)) {
                case "ANGL", "ENGLISH" -> textAngl;
                case "BG", "BULGARIAN" -> textBg;
                case "UKR", "UKRAINIAN" -> textUkr;
                default -> textUkr;
            };
        } else {
            text = textUkr;
        }

        runGame(text);
    }

    private static void runGame(final GameText text) {
        final String logo = """
                +--+ +--+ +--+ +--+ +--+ +--+
                |BБ| |RЛ| |IО| |KК| |KК| |SИ|
                +--+ +--+ +--+ +--+ +--+ +--+
                """;

        final Save backupSave = new EmptySave();
        try (
                final DatabaseConnection connection = new DatabaseConnection(
                        "jdbc:postgresql://localhost:5432/brikks",
                        "postgres",
                        "postgres"
                )
        ) {
            final View view = new ConsoleView(text, logo);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                                    try { connection.close(); } catch (final SQLException _e) {}
                                    view.exit();
                            }
                    )
            );

            final DatabaseSave save = new DatabaseSave(connection, backupSave);
            //recreateDB(save);

            final Brikks game = new Brikks(view, save);
            game.menu();
        } catch (final SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void recreateDB(final DatabaseSave save) throws SQLException {
        save.dropDB();
        save.recreateDB(
                (byte) Level.values().length,
                Brikks.MAX_PLAYERS,
                BlocksTable.WIDTH,
                BlocksTable.HEIGHT,
                View.MAX_NAME_LEN,
                Bombs.MAX_AMOUNT,
                Energy.MAX_POSITION,
                BonusScore.MAX_SCALE,
                Board.WIDTH,
                Board.HEIGHT,
                (byte) Color.values().length,
                (short) 191
        );
    }
}
