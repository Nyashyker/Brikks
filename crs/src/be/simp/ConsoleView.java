package be.simp;

import be.kdg.integration.brikks_project.*;

import java.util.Arrays;
import java.util.Scanner;


public class ConsoleView extends View {

    public GameText text;

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
        Block[] chosenBlocks = new Block[players.length];

        for (int i = 0; i < players.length; i++) {
            System.out.printf("Player %s, it's your turn to choose a block.%n", players[i].getName()); // add getName to Player

            // Display available blocks from BlocksTable
            Block[] availableBlocks = variants.getAvailableBlocks(); // add this method to Blocks
            for (int j = 0; j < availableBlocks.length; j++) {
                System.out.printf("%d: Block of color %s with shape: %s%n",
                        j + 1,
                        availableBlocks[j].getColor(),
                        formatShape(availableBlocks[j].getBlock())
                );
            }

            // Prompt the player to make a choice
            int choice = -1;
            while (choice < 1 || choice > availableBlocks.length) {
                System.out.print("Enter the number of your choice: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice < 1 || choice > availableBlocks.length) {
                        System.out.println("Invalid choice. Please select a valid block number.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // Store the chosen block
            chosenBlocks[i] = availableBlocks[choice - 1];
        }

        return (PlacedBlock[]) chosenBlocks;
    }


    private String formatShape(Position[] positions) {
        StringBuilder sb = new StringBuilder();
        for (Position position : positions) {
            sb.append(String.format("(%d, %d) ", position.getX(), position.getY()));
        }
        return sb.toString().trim();
    }




    public void draw(Board board, Energy energy, Bombs bombs, BonusScore bonusScore) {
        //fetching board dimensions (need logic from board + getters)
        int rows = board.getRows();
        int cols = board.getCols();

        //header
        System.out.println("+-----------------------------------------+");
        System.out.println("|                 BRIKKS                  |");
        System.out.println("+-----------------------------------------+");

        System.out.println("|      ");
        for (int col = 1; col <= cols; col++) {
            System.out.printf("%02d", col);
        }

        System.out.println("     |");
        System.out.println("|     +--".repeat(cols) + "+     |");


        //board rows
        for (int row = 0; row < rows; row++) {
            System.out.printf("| %02d  ", row);
            for (int col = 0; col < cols; col++) {
                String cellContent = board.getCell(row, col); //change when board is finished with logic
                System.out.printf("|%2s", cellContent != null ? cellContent : " ");
            }
            System.out.println("|  x" + (4 - row) + " |");
            System.out.println("|     +--".repeat(cols) + "+     |");
        }


        //footer (need logic of energy + getters)
        System.out.println("+-----------------------------------------+");
        System.out.println("|Victory Points: " + bonusScore.getVictoryPoints() + "               |");
        System.out.println("+-----------------------------------------+");
        System.out.println("|  Extra Points: " + bonusScore.getExtraPoints() + "                |");
        System.out.println("|  Energy: " + energy.getPoints() + "                              |");
        System.out.println("|  Bombs: " + Arrays.toString(bombs.getPoints()) + "                              |");
        System.out.println("+-----------------------------------------+");


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





    public Position askPlacingSpot(Block block, Position[] variants);

    public Position askPlacingMiniblock(Board opponentsBoard, Position[] variants);

    public Special askSpecial();

    public Block askRotation(Block[] variants);

    public Block askChoice(BlocksTable variants);


    public void endPlayer(Player player);
    public void end(Player[] player);

    public void start();
    public void exit();



}
