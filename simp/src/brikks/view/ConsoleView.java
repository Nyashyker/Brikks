package brikks.view;

import brikks.*;
import brikks.container.*;
import brikks.essentials.*;
import brikks.essentials.enums.*;
import brikks.logic.*;
import brikks.logic.board.*;
import brikks.save.*;
import brikks.save.container.*;
import brikks.view.enums.*;

import java.util.Arrays;
import java.util.Scanner;


public class ConsoleView extends View {
    public ConsoleView() {}

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


        return switch (choice) {
            case 1 -> Menu.NEW_GAME;
            case 2 -> Menu.LOAD;
            case 3 -> Menu.LIDERBOARD;
            case 4 -> Menu.EXIT;
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    @Override
    public void liderboard(final PlayerLiderboard[] players) {}

    @Override
    public boolean askUseExistingPlayer(final String name) {return false;}

    @Override
    public SavedGame askChoiceSave(final SavedGame[] variants) {return null;}


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
    public Doing askDoing(final BlocksTable blocks, final Position roll) {
        Scanner scanner = new Scanner(System.in);
        Doing doing = null;
        while (doing == null) {
            System.out.println("Select doing:");
            System.out.println("1. bomb");
            System.out.println("2. rotate");
            System.out.println("3. chose block");
            System.out.println("4. place");
            System.out.println("5. give up");
            System.out.println("6. save");
            System.out.println("7. exit");
            System.out.print("Enter your choice (1-7): ");

            String choice = scanner.nextLine();
            switch (choice.strip()) {
                case "1":
                    doing = Doing.BOMB;
                    break;
                case "2":
                    doing = Doing.ROTATE;
                    break;
                case "3":
                    doing = Doing.CHOICE;
                    break;
                case "4":
                    doing = Doing.PLACE;
                    break;
                case "5":
                    doing = Doing.GIVE_UP;
                    break;
                case "6":
                    doing = Doing.SAVE;
                    break;
                case "7":
                    doing = Doing.EXIT;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        return doing;
    }


    @Override
    public boolean askDuel() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Do you want to duel? (y/n)");

            String choice = scanner.nextLine();
            if (choice.equals("y")) {
                return true;
            } else if (choice.equals("n")) {
                return false;
            }
        }
    }

    @Override
    public byte askPlayerCount(final byte maxPlayers) {
        return 1;
    }

    @Override
    public String askName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of player: ");
        return scanner.nextLine();
    }


    private String formatShape(Position[] positions) {
        StringBuilder sb = new StringBuilder();
        for (Position position : positions) {
            sb.append(String.format("(%d, %d) ", position.getX(), position.getY()));
        }
        return sb.toString().trim();
    }




    @Override
    public void draw(Player player) {
        Board board = player.getBoard();
        Energy energy = player.getEnergy();
        Bombs bombs = player.getBombs();
        BonusScore bonusScore = player.getBonusScore();
        //fetching board dimensions (need logic from board + getters)
        int rows = Board.HEIGHT; //add this method to board!!!
        int cols = Board.WIDTH; //add this method to board!!!

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
                String cellContent = board.getUsedBoard()[row][col] ? "XX" : "  "; //change when board is finished with logic
                System.out.printf("|%2s", cellContent);
            }
            System.out.println("|  x" + (4 - row) + " |");
            System.out.println("|     +--".repeat(cols) + "+     |");
        }


        //footer (need logic of energy + getters) !!!!!!!
        System.out.println("+-----------------------------------------+");
        System.out.println("|  Extra Points: " + bonusScore.get() + "                |");
        System.out.println("|  Energy: " + energy.getAvailable() + "                              |");
        System.out.println("|  Bombs: " + Arrays.toString(bombs.getPoints()) + "                              |");
        System.out.println("+-----------------------------------------+");


    }


    @Override
    public boolean askReroll(final BlocksTable blocks, final MatrixDice matrixDie) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(this.formatBlock(blocks.getBlock(matrixDie.get())));

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


    @Override
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



    @Override
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
        int rows = Board.HEIGHT; //add this method to board!!!
        int cols = Board.WIDTH; //add this method to board!!!

        System.out.println("+--".repeat(cols) + "+");
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String cellContent = board.getUsedBoard()[row][col] ? "XX" : "  "; //add this method to board!!!
                System.out.printf("|%2s", cellContent);
            }
            System.out.println("|");
            System.out.println("+--".repeat(cols) + "+");
        }
    }


    @Override
    public byte askRotation(Block[] variants) {
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

        return (byte) (choice - 1);
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


    @Override
    public Position askChoice(BlocksTable variants) {
        Scanner scanner = new Scanner(System.in);


        Block[][] availableBlocks = variants.getTable();

        //display all blocks
        System.out.println("Available blocks to choose from:");
        for (int i = 0; i < availableBlocks.length; i++) {
            for (int j = 0; j < availableBlocks[i].length; j++) {
                System.out.printf("%d: %s%n", i + 1, formatBlock(availableBlocks[i][j]));
            }
        }

        // ask  player to make a choice
        int choiceX = -1;
        while (choiceX < 1 || choiceX > availableBlocks.length) {
            System.out.print("Enter your choice x (1-" + availableBlocks.length + "): ");
            try {
                choiceX = Integer.parseInt(scanner.nextLine());
                if (choiceX < 1 || choiceX > availableBlocks.length) {
                    System.out.println("Invalid choice. Please select a valid block number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        int choiceY = -1;
        while (choiceY < 1 || choiceY > availableBlocks.length) {
            System.out.print("Enter your choice y (1-" + availableBlocks.length + "): ");
            try {
                choiceY = Integer.parseInt(scanner.nextLine());
                if (choiceY < 1 || choiceY > availableBlocks.length) {
                    System.out.println("Invalid choice. Please select a valid block number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return new Position((byte) choiceX, (byte) choiceY);
    }

    @Override
    public void endSolo(Player[] players, final Rank[] ranks, final short finalScore) {
        System.out.println("======================================");
        System.out.println("           Game Over!                 ");
        System.out.println("======================================");

        // Sort players by score in descending order
        for (int i = 0; i < players.length - 1; i++) {
            for (int j = 0; j < players.length - i - 1; j++) {
                if (players[j].calculateFinal() < players[j + 1].calculateFinal()) { //add this methods to Player

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
            System.out.printf("%d. %s - Score: %d%n", i + 1, players[i].name, players[i].calculateFinal()); //add this methods to Player
        }
        System.out.println("--------------------------------------");

        // Congratulate the winner
        if (players.length > 0) {
            System.out.printf("Congratulations to the winner: %s with %d points!%n",
                    players[0].name, players[0].calculateFinal()); //add this methods to Player
        }

        System.out.println("Thank you for playing! See you next time!");
        System.out.println("======================================");
    }
    @Override
    public void endStandard(final Player[] players) {}
    @Override
    public void endDuel(final Player winner, final Player loser) {}

    @Override
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



    @Override
    public void successPlace(final PlacedBlock placed) {
        System.out.println("Block placed");
    }
    @Override
    public void successBomb() {
        System.out.println("bomb used");
    }
    @Override
    public void successRotation(final byte energyCost) {
        System.out.println("block rotated");
    }
    @Override
    public void successChoice(final byte energyCost) {
        System.out.println("block chosen");
    }
    @Override
    public void failPlace() {
        System.out.println("you can not place");
    }
    @Override
    public void failBomb() {
        System.out.println("no bombs available");
    }
    @Override
    public void failRotation() {
        System.out.println("you have no energy");
    }
    @Override
    public void failChoice() {
        System.out.println("you have not enough energy");
    }
    @Override
    public void failGiveUp() {
        System.out.println("you can not give up yet");
    }
    @Override
    public void fail() {
        System.out.println("you are fail");
    }
}
