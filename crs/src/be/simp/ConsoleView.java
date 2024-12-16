package be.simp;

import be.kdg.integration.brikks_project.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class ConsoleView extends View {

    public GameText text;



    @Override
    public Menu menu() {
        Scanner scanner = new Scanner(System.in);

        // Display the menu options
        System.out.println("======================================");
        System.out.println("               Main Menu              ");
        System.out.println("======================================");
        System.out.println("1. New Game");
        System.out.println("2. Load Game");
        System.out.println("3. View Leaderboard");
        System.out.println("4. Exit");
        System.out.println("--------------------------------------");


        int choice = -1;
        while (choice < 1 || choice > 4) {
            System.out.print("Enter your choice (1-4): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }


        switch (choice) {
            case 1:
                return Menu.NEW_GAME;
            case 2:
                return Menu.LOAD;
            case 3:
                return Menu.LIDERBOARD;
            case 4:
                return Menu.EXIT;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
    }



    @Override
    public Level askDifficulty() {
        Scanner scanner = new Scanner(System.in);
        Level level = null;
        while (level == null) {
            System.out.println("Select difficulty level:");
            System.out.println("1. One");
            System.out.println("2. Two");
            System.out.println("3. Three");
            System.out.println("4. Four");
            System.out.print("Enter your choice (1-4): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    level = Level.ONE;
                    break;
                case "2":
                    level = Level.TWO;
                    break;
                case "3":
                    level = Level.THREE;
                    break;
                    case "4":
                        level = Level.FOUR;
                    break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
            }
        }
        return level;
    }




    @Override
    public Mode askMode() {
        Scanner scanner = new Scanner(System.in);
        Mode mode = null;
        while (mode == null) {
            System.out.println("Select game mode:");
            System.out.println("1. Solo");
            System.out.println("2. Standard");
            System.out.println("3. Duel");
            System.out.print("Enter your choice (1-3): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    mode = Mode.SOLO;
                    break;
                case "2":
                    mode = Mode.STANDARD;
                    break;
                    case "3":
                        mode = Mode.DUEL;
                        break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        return mode;
    }


    @Override
    public String[] askNames() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        int playerCount;
        while (true) {
            try {
                playerCount = Integer.parseInt(scanner.nextLine());
                if (playerCount > 0) break;
                else {
                    System.out.print("Please enter a valid number greater than 0: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid number: ");
            }
        }

        String[] playerNames = new String[playerCount];
        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Enter the name of player %d: ", i + 1);
            playerNames[i] = scanner.nextLine();
        }
        return playerNames;

    }




    @Override
    public PlacedBlock[] askFirstChoice(BlocksTable variants, Player[] players) {
        Scanner scanner = new Scanner(System.in);

        // Список для хранения выбранных блоков
        PlacedBlock[] chosenBlocks = new PlacedBlock[players.length];
        List<Block> alreadyChosen = new ArrayList<>(); // Для отслеживания уже выбранных блоков

        for (int i = 0; i < players.length; i++) {
            System.out.printf("Player %s, it's your turn to choose a block.%n", players[i].getName()); // Предполагаем, что Player имеет метод getName()

            // Получить доступные блоки с исключением уже выбранных
            Block[] availableBlocks = variants.getAvailableBlocks(); // Получить полный список блоков
            List<Block> filteredBlocks = new ArrayList<>();

            for (Block block : availableBlocks) {
                if (!alreadyChosen.contains(block)) {
                    filteredBlocks.add(block);
                }
            }

            // Если доступных блоков нет
            if (filteredBlocks.isEmpty()) {
                System.out.println("No blocks available for selection.");
                continue;
            }

            // Показать доступные блоки с визуальной формой
            System.out.println("Available blocks:");
            for (int j = 0; j < filteredBlocks.size(); j++) {
                System.out.printf("%d: Block of color %s\nShape:\n%s\n",
                        j + 1,
                        filteredBlocks.get(j).getColor(),
                        formatShape(filteredBlocks.get(j).getBlock()) // Используем вашу функцию formatShape
                );
            }

            // Запрос выбора у игрока
            int choice = -1;
            while (choice < 1 || choice > filteredBlocks.size()) {
                System.out.print("Enter the number of your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 1 || choice > filteredBlocks.size()) {
                        System.out.println("Invalid choice. Please select a valid block number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // Добавить выбранный блок в список
            Block chosenBlock = filteredBlocks.get(choice - 1);
            alreadyChosen.add(chosenBlock); // Добавляем в уже выбранные блоки
            chosenBlocks[i] = new PlacedBlock(chosenBlock); // Оборачиваем в PlacedBlock
        }

        return chosenBlocks;
    }




    private String formatShape(Position[] positions)  {
        // Визначити межі форми
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;

        for (Position position : positions) {
            minX = Math.min(minX, position.getX());
            maxX = Math.max(maxX, position.getX());
            minY = Math.min(minY, position.getY());
            maxY = Math.max(maxY, position.getY());
        }

        // Визначити розміри матриці
        int rows = maxX - minX + 1;
        int cols = maxY - minY + 1;

        // Створити матрицю форми
        char[][] shapeMatrix = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                shapeMatrix[i][j] = ' '; // Заповнити пробілами
            }
        }

        // Заповнити блоки 'x' на основі позицій
        for (Position position : positions) {
            int adjustedX = position.getX() - minX; // Нормалізувати до матриці
            int adjustedY = position.getY() - minY;
            shapeMatrix[adjustedX][adjustedY] = 'x';
        }

        // Побудувати візуальне представлення форми
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(shapeMatrix[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }




    public void draw(Board board, Energy energy, Bombs bombs, BonusScore bonusScore) {
        // Fetching board dimensions
        int rows = board.getRows(); // Ensure this method exists in Board
        int cols = board.getCols(); // Ensure this method exists in Board

        // Header
        System.out.println("+-----------------------------------------+");
        System.out.println("|                 BRIKKS                  |");
        System.out.println("+-----------------------------------------+");

        // Column headers
        System.out.print("|      ");
        for (int col = 1; col <= cols; col++) {
            System.out.printf("%02d ", col);
        }
        System.out.println("     |");
        System.out.println("|     " + "+--".repeat(cols) + "+     |");

        // Board rows
        for (int row = 0; row < rows; row++) {
            System.out.printf("| %02d  ", row + 1); // Row numbers start at 1
            for (int col = 0; col < cols; col++) {
                String cellContent = board.getCell(row, col); // Adjust when board logic is implemented
                System.out.printf("|%2s", cellContent != null ? cellContent : " ");
            }
            System.out.println(" |");
            System.out.println("|     " + "+--".repeat(cols) + "+     |");
        }

        // Footer
        System.out.println("+-----------------------------------------+");

        // Bonus points and scaling rate
        int scale = bonusScore.getScale();
        int currentScalingRate = bonusScore.getScalingRate(scale);
        String nextScalingRate = (scale < BonusScore.MAXIMUM_SCALE)
                ? String.valueOf(bonusScore.getScalingRate(scale + 1))
                : "MAX"; // Ensure no overflow beyond maximum scale

        System.out.printf("|  Victory Points: %d (%d / %s)%n",
                bonusScore.getVictoryPoints() /* <--- захарджено в UsedBoard */,
                currentScalingRate,
                nextScalingRate);
        System.out.println("+-----------------------------------------+");

        // Extra Points, Energy, and Bombs
        System.out.printf("|  Extra Points: %02d%s|%n",
                bonusScore.getExtraPoints(),
                " ".repeat(37 - String.format("%02d", bonusScore.getExtraPoints()).length()));

        System.out.printf("|  Energy: %02d%s|%n",
                energy.Available(),
                " ".repeat(37 - String.format("%02d", energy.getAvailable()).length()));

        System.out.printf("|  Bombs: %s%s|%n",
                Arrays.toString(bombs.getPoints()),
                " ".repeat(37 - Arrays.toString(bombs.getPoints()).length()));
        System.out.println("+-----------------------------------------+");
    }









    @Override
    public PlaceORSpecial askRoll() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to place a block or use a special action?");
        System.out.println("1. Place a block");
        System.out.println("2. Use a special action");

        int choice = -1;
        while (choice < 1 || choice > 2) {
            System.out.print("Enter your choice (1-2): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > 2) {
                    System.out.println("Invalid choice. Please select 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }


        return (choice == 1) ? PlaceORSpecial.PLACE : PlaceORSpecial.SPECIAL;
    }



    @Override
    public boolean askReroll() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Do you want to reroll? (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please type 'yes' or 'no'.");
            }
        }
    }





    public PlaceORSpecial askPlaceOrSpecial() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Do you want to place a block or use a special move? (place/special): ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("place")) {
                return PlaceORSpecial.PLACE;
            } else if (input.equals("special")) {
                return PlaceORSpecial.SPECIAL;
            } else {
                System.out.println("Invalid input. Please type 'place' or 'special'.");
            }
        }
    }





    public Position askPlacingSpot(Block block, Position[] variants) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("You are placing the following block:");
        displayBlock(block);

        //available placement options
        System.out.println("Available placement spots:");
        for (int i = 0; i < variants.length; i++) {
            System.out.printf("%d: (%d, %d)%n", i + 1, variants[i].getX(), variants[i].getY());
        }

        // ask the player to choose a position
        int choice = -1;
        while (choice < 1 || choice > variants.length) {
            System.out.print("Choose a placement spot (1-" + variants.length + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > variants.length) {
                    System.out.println("Invalid choice. Please select a valid placement number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return variants[choice - 1];
    }


    // helper method to display block color / shape
    private void displayBlock(Block block) {
        System.out.printf("Block Color: %s%n", block.getColor());
        System.out.println("Block Shape:");
        for (Position pos : block.getBlock()) {
            System.out.printf("(%d, %d) ", pos.getX(), pos.getY());
        }
        System.out.println();
    }



    public Position askPlacingMiniblock(Board opponentsBoard, Position[] variants) {
        Scanner scanner = new Scanner(System.in);

        // display the opponent board
        System.out.println("Opponent's board:");
        displayBoard(opponentsBoard);

        // display available placement options
        System.out.println("Available positions for placing the miniblock:");
        for (int i = 0; i < variants.length; i++) {
            System.out.printf("%d: (%d, %d)%n", i + 1, variants[i].getX(), variants[i].getY());
        }

        // ask player to select a position
        int choice = -1;
        while (choice < 1 || choice > variants.length) {
            System.out.print("Choose a placement spot for the miniblock (1-" + variants.length + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > variants.length) {
                    System.out.println("Invalid choice. Please select a valid placement number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // Return the selected position
        return variants[choice - 1];
    }


    private void displayBoard(Board board) {
        int rows = board.getRows(); //add this method to board!!!
        int cols = board.getCols(); //add this method to board!!!

        System.out.println("+--".repeat(cols) + "+");
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String cellContent = board.getCell(row, col); //add this method to board!!!
                System.out.printf("|%2s", cellContent != null ? cellContent : "  ");
            }
            System.out.println("|");
            System.out.println("+--".repeat(cols) + "+");
        }
    }



    public Special askSpecial() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a special action:");
        System.out.println("1. Bomb");
        System.out.println("2. Rotate");
        System.out.println("3. Choice");
        System.out.println("4. Back");


        int choice = -1;
        while (choice < 1 || choice > 4) {
            System.out.print("Enter your choice (1-4): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid choice. Please select a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // Map the player's choice to a Special enum value
        switch (choice) {
            case 1:
                return Special.BOMB;
            case 2:
                return Special.ROTATE;
            case 3:
                return Special.CHOICE;
            case 4:
                return Special.BACK;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);
        }
    }






    public Block askRotation(Block[] variants) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a rotated version of the block:");
        for (int i = 0; i < variants.length; i++) {
            System.out.printf("%d: %s%n", i + 1, formatBlock(variants[i]));
        }

        int choice = -1;
        while (choice < 1 || choice > variants.length) {
            System.out.print("Enter your choice (1-" + variants.length + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > variants.length) {
                    System.out.println("Invalid choice. Please select a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return variants[choice - 1];
    }

    //helper method to format a block for display
    private String formatBlock(Block block) {
        StringBuilder sb = new StringBuilder();
        sb.append("Block Color: ").append(block.getColor()).append(" | Shape: ");
        for (Position pos : block.getBlock()) {
            sb.append(String.format("(%d, %d) ", pos.getX(), pos.getY()));
        }
        return sb.toString().trim();
    }




    public Block askChoice(BlocksTable variants) {
        Scanner scanner = new Scanner(System.in);


        Block[] availableBlocks = variants.getAvailableBlocks(); // add this method to BlocksTable !!!!!!
        if (availableBlocks == null || availableBlocks.length == 0) {
            System.out.println("No blocks available to choose from.");
            return null; // if blocks table is NULL
        }

        //display all blocks
        System.out.println("Available blocks to choose from:");
        for (int i = 0; i < availableBlocks.length; i++) {
            System.out.printf("%d: %s%n", i + 1, formatBlock(availableBlocks[i]));
        }

        // ask  player to make a choice
        int choice = -1;
        while (choice < 1 || choice > availableBlocks.length) {
            System.out.print("Enter your choice (1-" + availableBlocks.length + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > availableBlocks.length) {
                    System.out.println("Invalid choice. Please select a valid block number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return availableBlocks[choice - 1];
    }






    public void endPlayer(Player player) {
        System.out.println("======================================");
        System.out.printf("End of turn for player: %s%n", player.getName()); //add this

        // Display player's score and other relevant statistics
        System.out.printf("Score: %d%n", player.getScore()); // add this
        System.out.printf("Energy: %d%n", player.getEnergy()); // add this
        System.out.printf("Bombs remaining: %d%n", player.getBombs()); // add this

        System.out.println("Thank you for playing this turn!");
        System.out.println("======================================");
    }




    public void end(Player[] players) {
        System.out.println("======================================");
        System.out.println("           Game Over!                 ");
        System.out.println("======================================");

        // Sort players by score in descending order
        for (int i = 0; i < players.length - 1; i++) {
            for (int j = 0; j < players.length - i - 1; j++) {
                if (players[j].getScore() < players[j + 1].getScore()) { //add this methods to Player

                    // Swap players[j] and players[j + 1]
                    Player temp = players[j];
                    players[j] = players[j + 1];
                    players[j + 1] = temp;
                }
            }
        }

        // Display leaderboard
        System.out.println("Final Scores:");
        System.out.println("--------------------------------------");
        for (int i = 0; i < players.length; i++) {
            System.out.printf("%d. %s - Score: %d%n", i + 1, players[i].getName(), players[i].getScore()); //add this methods to Player
        }
        System.out.println("--------------------------------------");

        // Congratulate the winner
        if (players.length > 0) {
            System.out.printf("Congratulations to the winner: %s with %d points!%n",
                    players[0].getName(), players[0].getScore()); //add this methods to Player
        }

        System.out.println("Thank you for playing! See you next time!");
        System.out.println("======================================");
    }



    public void start() {
        System.out.println("======================================");
        System.out.println("           Welcome to BRIKKS!         ");
        System.out.println("======================================");
        System.out.println("Get ready for a fun and challenging game.");
        System.out.println("Rules:");
        System.out.println("- Place blocks strategically to maximize your score.");
        System.out.println("- Use special moves like bombs and rotations wisely.");
        System.out.println("- Compete to achieve the highest score!");
        System.out.println("--------------------------------------");
        System.out.println("Let's get started!");
        System.out.println("======================================");
    }



    public void exit() {
        System.out.println("======================================");
        System.out.println("           Thank You for Playing!     ");
        System.out.println("======================================");
        System.out.println("We hope you had fun playing BRIKKS.");
        System.out.println("Come back soon for more challenges!");
        System.out.println("--------------------------------------");
        System.out.println("Exiting the game...");
        System.out.println("Goodbye!");
        System.out.println("======================================");
    }



}
