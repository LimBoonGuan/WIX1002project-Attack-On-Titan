package Assignment_GRP6;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class AoT {

    static Ground ground;
    static Wall[] walls;
    static Weapon[] weapons;
    static ArmouredTitan[] ArmouredTitans;
    static ColossusTitan[] ColossusTitans;
    static Shield[] shield;
    static Coin coin;
    static Leaderboard boardNormal;
    static Leaderboard boardEndless;
    static Music musicGameStart;
    static Music musicGameOver;
    static Music victoryMusic;
    static Scanner input;

    static int gameHour = 0;
    static int mode = 0;
    static long maxHour = 0L;
    static boolean gameOver = false;
    static String playerName;

    // keep track of titans/shield killed
    static int killedArmouredTitanCounter = 0;
    static int killedColossusTitanCounter = 0;
    static int killedShieldCounter = 0;

    /**********************************************************************
     * Main method to handle the flow of game
     * 
     * Example: - Display Main Menu - Show loading bar - Display the gaming
     * interface (Wall, Ground, Weapon, Titan, Coin, Hour) - Track game hours -
     * Swicthing turns between player and titans - Display Game Over /
     * Congratulations Screen - Playing of anime theme songs as Background Music -
     * Ending Music before exiting the game
     * 
     **********************************************************************/
    public static void main(String[] args) throws InterruptedException, IOException {
        input = new Scanner(System.in);

        createGameObjects();

        SoundSFX.playTrailers();

        musicGameStart = new Music("AOT_BGM_GAME_START.wav");
        musicGameStart.play();
        Thread.sleep(1000);

        // get user input for player name (max 15 characters)
        do {
            System.out.print("Enter player name (maximum 15 characters): ");
            playerName = input.nextLine();
        } while (playerName.length() < 1 || playerName.length() > 15);

        showMainMenu();

        showLoadingBar();

        clearScreen();
        System.out.println("The Game Started");
        Thread.sleep(500);

        label:
        {
            while (gameHour < maxHour) {
                // titans appear after 5 hours
                if (gameHour > 5) {
                    // error handling for player input
                    do {
                        System.out.println("Enemies' turn (Press Enter to proceed)");
                    } while (!input.nextLine().equals(""));

                    titanTurn();
                }

                //stop the game if walls hp = 0
                for (int i = 0; i < walls.length - 1; i++) {
                    if (walls[i].getHP() <= 0) {
                        gameOver = true;
                        break label;
                    }
                }

                System.out.println("Player's turn");
                displayMap();
                askToUpgradeWeapon();
                displayMap();
                askToUpgradeAllWalls();
                displayMap();
                askToUpgradeSelectedWalls();

                //attention frankie and curry
                playerAttack();
                removeDeadObjects();

                //comment out, for debugging purposes
                //displayATHP(); 

                // use nuke to kill all titans on ground
                if (gameHour > 5 && gameHour % 10 == 0 && coin.getBalance() >= 50) {
                    askToCallNuke();
                    displayMap();
                }
                
                System.out.println(coin.addCoinPerHour());
                gameHour++;
            }
        }

        // update and save leaderboard when game is over according to game mode
        switch (mode) {
            case 1:
                boardNormal.updateLeaderboard(playerName, gameHour);
                boardNormal.saveLeaderboard();
                break;
            case 2:
                boardEndless.updateLeaderboard(playerName, gameHour);
                boardEndless.saveLeaderboard();
                break;
        }

        // stop the previous music
        if (musicGameStart != null) {
            musicGameStart.stop();
            musicGameStart.close();
        }

        // display congratulations only for normal mode
        if (!gameOver && mode != 2) {
            displayAsciiArt("CONGRATULATION_SCREEN.txt", "green");
            victoryMusic = new Music("AOT_BGM_VICTORY.wav");
            victoryMusic.play();
            Thread.sleep(1000);
        } else {
            clearScreen();
            displayMap();
            Thread.sleep(4000);
            clearScreen();
            // ending music
            SoundSFX.playCredits();
            displayAsciiArt("GAME_OVER_SCREEN.txt", "red");
        }

        // display the achievements
        System.out.println("Hour(s) Survived                  : " + gameHour);
        System.out.println("Number of Armoured Titan(s) killed: " + killedArmouredTitanCounter);
        System.out.println("Number of Colossus Titan(s) killed: " + killedColossusTitanCounter);
        System.out.println("Number of shield(s) destroyed     : " + killedShieldCounter);
        System.out.println("");

        do {
            System.out.println("Press Enter to exit!");
        } while (!input.nextLine().equals(""));
        
        if (victoryMusic != null) {
            victoryMusic.stop();
            victoryMusic.close();
        }
        System.exit(0);
    }

    /**********************************************************************
     * Helper method to convert string of integer into integer arrays
     * 
     * Example:
     * - "123456" -> {1, 2, 3, 4, 5, 6}
     * - "10 5 4" -> {10, 5, 4}
     **********************************************************************/
    public static int[] convertStringToIntegerArray(String s) {
        String[] before;
        if (s.contains(" ")) {
            before = s.split(" ");
        } else {
            before = s.split("");
        }

        int[] after = new int[before.length];
        for (int i = 0; i < after.length; i++) {
            after[i] = Integer.parseInt(before[i]);
        }
        return after;
    }

    /*************************************************************************
     * Initialize each objects arrays for weapons, titans, walls and ground
     *
     * Load the leaderboard records of different game modes:
     *   - Normal: LEADERBOARD_NORMAL.txt
     *   - Endless: LEADERBOARD_ENDLESS.txt
     * 
     * Fill up Ground[]
     * Fill up Weapon[] with initial level and damage of 0
     * Fill up Wall[] with initial HP of 10
     * Initialise ArmouredtITitan[] and ColossusTitan[] to keep track of titans generated
     * 
     **************************************************************************/
    public static void createGameObjects() throws InterruptedException {
        // coin
        coin = new Coin();

        // leaderboard
        // for normal mode, load leaderboard_normal.txt
        boardNormal = new Leaderboard("LEADERBOARD_NORMAL.txt", "Normal Mode");
        boardNormal.loadLeaderboard();

        // for endless mode, load leaderboard_endless.txt
        boardEndless = new Leaderboard("LEADERBOARD_ENDLESS.txt", "Endless Mode");
        boardEndless.loadLeaderboard();

        // ground
        ground = new Ground();

        // weapons
        weapons = new Weapon[10];
        for (int i = 0; i < weapons.length; i++) {
            weapons[i] = new Weapon("***");
        }

        // walls
        walls = new Wall[10];
        for (int i = 0; i < walls.length; i++) {            
            walls[i] = new Wall(50);
        }

        // titans
        ArmouredTitans = new ArmouredTitan[2000000];
        ColossusTitans = new ColossusTitan[2000000];

        // shields
        shield = new Shield[2000000];        
    }
    
    /***********************************************************************
     * To show loading bar from 0% to 100%
     * 
     * i.e. [####        ] 15%
     * continuously printed on same line using carriage return \r
     * 
     * NOTE: 
     *  - Does not work in Netbeans output window
     *  - Tested in VS Code, IntelliJ IDEA
     * 
     ***********************************************************************/
    public static void showLoadingBar() throws InterruptedException {
        final StringBuilder loadingBar = new StringBuilder();
        String format = "[%-21s]%d%%\r";
        for (int percent = 0; percent <= 100; percent++) {
            if (percent % 5 == 0) {
                loadingBar.append("#");
            }
            System.out.print(Color.colorize(String.format(format, loadingBar, percent), "green"));
            Thread.sleep(15);
        }
    }
 
    /**********************************************************************
     * Read .txt file contaning ASCII Art and display
     * 
     * File Name:
     *  - WELCOME_SCREEN.txt
     *     - before game start
     *  - CONGRATULATION_SCREEN.txt
     *     - after player wins the game
     *  - GAME_OVER_SCREEN.txt
     *     - after player loses the game
     *  
     **********************************************************************/
    public static void displayAsciiArt(String filename, String color) {
        clearScreen();
        try {
            Scanner inputStream = new Scanner(new FileInputStream("./ASCII_Banner/" + filename));
            while (inputStream.hasNextLine()) {
                System.out.println(Color.colorize(inputStream.nextLine(), color));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found");
        }
    }
    
    /**********************************************************************
     * Show Main Menu with welcome screen
     * 
     * Let player decide where to go
     * 
     * Options:
     * 1. Play
     *   - can choose normal / endless mode
     * 2. Leaderboard
     *   - show the leaderboards for normal and endless mode
     * 3. Exit 
     *   - You know it
     **********************************************************************/
    public static void showMainMenu() throws InterruptedException {
        displayAsciiArt("WELCOME_SCREEN.txt", "red");
        Thread.sleep(250);
        System.out.println("\n\t\tMain Menu\n");
        System.out.println("1. Play Game");
        System.out.println("2. Leaderboard");
        System.out.println("3. " + Color.colorize("Quit", "red"));
        int choice;
        // error handling for player input
        do {
            System.out.print("What do you want to do? (1-3): ");
            choice = input.nextInt();
            input.nextLine();
        } while ((choice < 1) || (choice > 3));
        
        System.out.println("");
        switch (choice) {
            case 1:
                selectGameMode();
                break;
            case 2:
                // dsiplay leaderboard for normal and endless mode
                Thread.sleep(500);
                boardNormal.display();
                Thread.sleep(500);
                boardEndless.display();
                
                System.out.print("Press Enter to go back to Main Menu");
                input.nextLine();
                Thread.sleep(500);
                showMainMenu();
                break;
            case 3:
                String answer;
                // error handling for player input
                do {
                    System.out.print("Are you sure you want to quit?(Y/n): ");
                    answer = input.nextLine().replaceAll("\\s", "").toUpperCase();
                } while (!answer.equals("Y") && !answer.equals("N"));

                if (answer.equals("Y")) {
                    System.out.println("Hope to see you again");
                    Thread.sleep(1000);
                    System.exit(0);
                } else {
                    showMainMenu();
                }
                break;
        }
    }
    
    /**********************************************************************
     * Allow player to select game mode
     * 
     * To set the maximum playing hours based on modes selected
     * 
     * Select Modes:
     * 1. Normal
     *   - Maximum playing hours is 80
     *   - Player need to survive the titans attack in order to win
     * 2. Endless
     *   - No maximum playing hours
     *   - Survive as many hours as you can!
     * 
     * [!] NOTE:
     * - If any wall unit hit points(HP) drops below 0, 
     *   the wall is destroyed, 
     *   game is over
     **********************************************************************/
    public static void selectGameMode() {
        displayAsciiArt("WELCOME_SCREEN.txt", "red");

        System.out.println("Select Mode: ");
        System.out.println("1. Normal");
        System.out.println("2. Endless");
        
        do {
            System.out.print("Which one do you choose? (1-3): ");
            mode = input.nextInt();
            input.nextLine();
        } while ((mode < 1) || (mode > 2));
        
        System.out.println("");
        switch (mode) {
            case 1: // normal mode settings
                maxHour = 80;
                break;
            case 2: // endless mode settings
                maxHour = Long.MAX_VALUE;
                break;
            default:
                maxHour = 80;
        }
    }

    // to show the game area
    public static void displayMap() {
        System.out.print(ground.toString(gameHour, coin));
        System.out.print(Weapon.toString(weapons));
        System.out.println(Wall.toString(walls));
    }
    
    // empty the output screen
    public static void clearScreen() {
        // does not work in Netbeans output window
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        
        // in case above code does not work
        for (int i = 0; i < 30; i++)
            System.out.println("");
    }

    /************************************************************************ 
     * 1st Question:
     * Prompt player to upgrade weapons of his choice 
     * 
     * Input: 
     * - Index of weapon to upgrade as a string of integers
     * - 'Enter' to skip
     * 
     * [!] NOTE:
     * - Upgrade as much weapons as possible when coin is not enough
     ************************************************************************/
    public static void askToUpgradeWeapon() {
        String choice;
        // error handling for player input
        do {
            System.out.println("Choose the weapon(s) you would like to upgrade (Type a string of integer or hit Enter to skip)");          
            choice = input.nextLine().replaceAll("\\s", "");   // get rid of extra white spaces
        } while (!choice.equals("") && !choice.matches("[0-9]+"));
        
        // check if a string of integers is typed
        if (choice.matches("[0-9]+")) {
            int[] target = convertStringToIntegerArray(choice);
            int level = 0, totalUpgradeCost = 0;
            
            // upgrade as much weapon as possible if not enough money to upgrade all
            // give priority to upgrade cost which is lower (low level upgrade)
            while (level < 3) {
                for (int i = 0; i < target.length; i++) {
                    
                    // check if target has already been upgraded
                    if (target[i] < 0) {
                        continue;
                    }
                    
                    Weapon weaponToUpgrade = weapons[target[i]];
                    if (weaponToUpgrade.getLevel() == level) {

                        // check if coin balance is sufficient to upgrade weapon to next level
                        int upgradeCost = weaponToUpgrade.getUpgradeCost();
                        if (coin.getBalance() >= upgradeCost) {
                            weaponToUpgrade.upgrade();
                            target[i] = -1;     // remove upgraded weapon index from array
                            coin.pay(upgradeCost);
                            totalUpgradeCost += upgradeCost;
                        } else {
                            System.out.println(Color.colorize("Not enough coin!", "red"));
                        }
                    }
                }
                level++;
            }
            System.out.println("Total Cost: " + totalUpgradeCost);
        }
    }
    
    /************************************************************************ 
     * 2nd Question:
     * Prompt player to upgrade all walls
     * 
     * Input: 
     * - "1" for yes -> go to 3rd Question
     * - 'Enter' to skip
     * 
     * [!] NOTE:
     * - Upgrade as much walls as possible when coin is not enough
     ************************************************************************/
    public static void askToUpgradeAllWalls() {
        String choice;
        // error handling for player input
        do {
            System.out.println("Do you want to upgrade all walls? (press 1 if yes, press Enter if no) Current coin number: " + coin.getBalance());
            choice = input.nextLine().replaceAll("\\s", "");
        } while (!choice.equals("") && !choice.equals("1"));
        
        // set all walls as target to upgrade
        if (choice.equals("1")) {
            addHPToAllWalls();
        }
    }

    /************************************************************************ 
     * 4th Question:
     * Prompt player to upgrade wall units of his choice 
     * 
     * Input: 
     * - Index of wall units to upgrade as a string of integers
     *   i.e. "0123" or "1032" indicates player want to upgrade wall units with index 0, 1, 2, 3
     * - 'Enter' to skip
     * 
     * [!] NOTE:
     * - Order of wall units index is not important
     * - Upgrade as much walls as possible when coin is not enough
     ************************************************************************/
    public static void askToUpgradeSelectedWalls() {
        String choice;
        // error handling for player input
        do {
            System.out.println("Choose the wall(s) that you would like to upgrade (Type a string of integer or hit Enter to skip)");
            choice = input.nextLine().replaceAll("\\s", "");
        } while (!choice.equals("") && !choice.matches("[0-9]+"));
    
        // check input for string of integers
        if (choice.matches("[0-9]+")) {
            int[] target = convertStringToIntegerArray(choice);
            addHPToSelectedWalls(target);
        }
    }
    
    /************************************************************************ 
     * 3rd Question:
     * For upgrading of all walls, ask player to enter amount of HP to be added
     * If not enough coin, show "Not enough coin!", upgrade is neglected
     * 
     * Input: 
     * - HP added in integers
     *   i.e. "10" means 10 HP added to all wall units
     * 
     * [!]NOTE:
     * - Upgrade as much walls as possible when coin is not enough
     * - 1HP = 1 Coin
     ************************************************************************/
    public static void addHPToAllWalls() {
        String choice;
        // error handling for player input
        do {
            System.out.println("How many HP do you want to add up to all the walls? Current coin number: " + coin.getBalance());
            choice = input.nextLine().replaceAll("\\s", "");
        } while (!choice.matches("[0-9]+"));
        
        int HpToAdd = Integer.parseInt(choice);
        int upgradeCost = HpToAdd;
        int totalUpgradeCost = 0;

        for (int i = 0; i < 10; i++) {
            Wall wallToUpgrade = walls[i];

            // check if coin balance is sufficient to upgrade wall
            if (coin.getBalance() >= upgradeCost) {
                wallToUpgrade.upgrade(HpToAdd);
                coin.pay(upgradeCost);
                totalUpgradeCost += upgradeCost;
            } else {
                System.out.println(Color.colorize("Not enough coin!", "red"));
            }
        }
        System.out.println("Total Cost: " + totalUpgradeCost);
    }
    
    /************************************************************************ 
     * 5th Question:
     * For upgrading of slected walls
     * Ask player to enter amount of HP to be added to each selected walls
     * 
     * If not enough coin, show "Not enough coin!", upgrade is neglected
     * 
     * Input: 
     * - HP added to each wall units in integers
     *   i.e. "10 5 6" means +10 HP to Target 1, +5 HP to Target 2, + 6 HP to Target 3
     * 
     * [!] NOTE:
     * - The order of HP is corresponding to the order of wall units index in 4th Question 
     *   Example: Walls    : "213"
     *            HP To Add: "10 5 6"
     *            - Wall index 2 + 10HP
     *            - Wall index 1 + 5HP
     *            - Wall index 3 + 6HP          
     * 
     * - Upgrade as much target wall units as possible when coin is not enough
     * - 1HP = 1 Coin
     ************************************************************************/
    public static void addHPToSelectedWalls(int[] target) {
        String choice;
        int[] HpToAdd;
        // error handling for player input
        do {
            System.out.println("How many HP do you want to add up to the wall(s)? Current coin number: " + coin.getBalance());
            choice = input.nextLine().trim();
            
            if (target.length == 1 && !choice.contains(" ") && choice.matches("[0-9]+")) {
                int hold = Integer.parseInt(choice);
                HpToAdd = new int[1];
                HpToAdd[0] = hold;
                break;
            }

            if (choice.matches("(\\s*[0-9]+){1,9}")) {
                HpToAdd = convertStringToIntegerArray(choice);
                if (HpToAdd.length == target.length) {
                    break;
                }
            }
        } while (true);
 
        int upgradeCost, totalUpgradeCost = 0;
        for (int i = 0; i < target.length; i++) {
            Wall wallToUpgrade = walls[target[i]];
            upgradeCost = HpToAdd[i];

            if (coin.getBalance() >= upgradeCost) {
                wallToUpgrade.upgrade(HpToAdd[i]);
                coin.pay(upgradeCost);
                totalUpgradeCost += upgradeCost;
            } else {
                System.out.println(Color.colorize("Not enough coin!", "red"));
            }
        }
        System.out.println("Total Cost: " + totalUpgradeCost);
    }
    
     /************************************************************************ 
     * Handle Titan turn
     * 
     * - Attacking of Armoured Titans on walls and weapons
     * - Attacking of Colossus Titans on walls
     * - Movement of Armoured Titans towards walls
     *    - Move closer to walls with weapons
     * - Movement of Colossus Titans towards walls 
     *    - Randomly left or right
     * - Generate shield on Ground after Hours 25 
     *    - Protects all the tiitans on the same column
     * - Generate Armoured Titans (Row 0) 
     * - Generate Colossus Titans (Row 9)
     ************************************************************************/
    public static void titanTurn() {
        System.out.println("Game Hour " + gameHour);
        attackArmouredTitan();
        attackColossusTitan();
        movementArmouredTitan();
        movementColossusTitan();
        generateShield();
        generateArmouredTitan();
        generateColossusTitan();
    }
    
    /************************************************************************ 
     * Generate Armoured Titans on Ground on Row 0
     * 
     * Hour     Rate of Generation
     * 06-20 : 1 per 4 hour (for 15 hours)
     * 21-40 : 1 per 3 hour
     * 41-60 : 2 per 4 hour
     * 61-80 : 2 per 3 hour
     * > 80 (endless) : 2 per 2 hour
     * > 100 (hardcore endless) : 2 per 1 hour
     ************************************************************************/
    public static void generateArmouredTitan(){
        int MaxTitanSummon = 0;
        int groundMaxTitanSummon = 0;
        
        MaxTitanSummon = 0;
        groundMaxTitanSummon = ground.checkMaxArmouredTitanCanBeSummoned();
        if ( (gameHour >= 6) && (gameHour <= 20) && ((gameHour-2)%4 == 0) ) {
            MaxTitanSummon = 1;
        }
        else if ( (gameHour >= 21) && (gameHour <= 40) && (gameHour%3 == 0) ) {
            MaxTitanSummon = 1;
        }
        else if ( (gameHour >= 41) && (gameHour <= 60) && ((gameHour-1)%4 == 0) ) {
            MaxTitanSummon = 2;
        }
        else if ( (gameHour>= 61) && (gameHour <= 80) && ((gameHour-1)%3 == 0) ) {
            MaxTitanSummon = 2;
        }
        else if ( (gameHour >= 81) && (gameHour <= 100) && ((gameHour-1)%2 == 0) ) {
            MaxTitanSummon = 2;
        }
        else if (gameHour >= 101) {
            MaxTitanSummon = 2;
        }
        else {
            MaxTitanSummon = 0;
        }
        //print out titans appeared
        if (groundMaxTitanSummon != 0) {
            if (MaxTitanSummon == 1) {
                System.out.println(Color.colorize(MaxTitanSummon + " ARMOURED TITAN APPEARED!", "red"));
                System.out.println(Color.colorize("ARMOURED TITAN COORDINATES : ", "red"));
            }
            else if (MaxTitanSummon > 1) {
                System.out.println(Color.colorize(MaxTitanSummon + " ARMOURED TITANS APPEARED!", "red"));
                System.out.println(Color.colorize("ARMOURED TITANS COORDINATES : ", "red"));
            }
        }
        
        String showcoordinatesTitan = "";
        for (int i=0 ; i<MaxTitanSummon && i<groundMaxTitanSummon ; i++) {
            int index = ArmouredTitan.getArmouredTitanSpawnCounter();
            ArmouredTitans[index] = new ArmouredTitan();
            while (!ground.checkCoordinateForTitanIsEmpty(ArmouredTitans[index])) {
                ArmouredTitans[index].GenerateAgainArmouredTitan();
            }
            if (ground.checkCoordinateForTitanIsEmpty(ArmouredTitans[index])) {
                ground.putArmouredTitan(ArmouredTitans[index]);
                int space = ground.getSpaceAssignedToTitan();
                ArmouredTitans[index].setArmouredTitanCurrentSpace(space);
            }
            showcoordinatesTitan += Color.colorize("Row " + ArmouredTitans[index].getArmouredTitanCurrentRow() 
                    + " Column " + ArmouredTitans[index].getArmouredTitanCurrentColumn() + "\n", "red");
        }
        System.out.println(showcoordinatesTitan);
    }
    
    /************************************************************************ 
     * Generate Colossus Titans on Ground on Row 9
     * 
     * Hours    Rate of Generation
     * 06-20 : 1 per 5 hour (for 15 hours)
     * 21-40 : 1 per 4 hour
     * 41-60 : 2 per 5 hour
     * 61-80 : 2 per 4 hour
     * > 80 (endless) : 2 per 3 hour
     * > 100 (hardcore endless) : 2 per 2 hour
     ************************************************************************/
    public static void generateColossusTitan(){
        int MaxTitanSummon = 0;
        int groundMaxTitanSummon = 0;
        
        MaxTitanSummon = 0;
        groundMaxTitanSummon = ground.checkMaxColossusTitanCanBeSummoned();
        if ( (gameHour >= 6) && (gameHour <= 20) && ((gameHour-1)%5 == 0) ) {
            MaxTitanSummon = 1;
        }
        else if ( (gameHour >= 21) && (gameHour <= 40) && ((gameHour-1)%4 == 0) ) {
            MaxTitanSummon = 1;
        }
        else if ( (gameHour >= 41) && (gameHour <= 60) && ((gameHour-1)%5 == 0) ) {
            MaxTitanSummon = 2;
        }
        else if ( (gameHour >= 61) && (gameHour <= 80) && ((gameHour-1)%4 == 0) ) {
            MaxTitanSummon = 2;
        }
        else if ( (gameHour >= 81) && (gameHour <= 100) && (gameHour%3 == 0) ) {
            MaxTitanSummon = 2;
        }
        else if (gameHour >= 101 && ((gameHour-1)%2 == 0)) {
            MaxTitanSummon = 2;
        }
        else {
            MaxTitanSummon = 0;
        }
        if (groundMaxTitanSummon != 0) {
            if (MaxTitanSummon == 1) {
                System.out.println(Color.colorize(MaxTitanSummon + " COLOSSUS TITAN APPEARED!", "red"));
                System.out.println(Color.colorize("COLOSSUS TITAN COORDINATES : ", "red"));
            }
            else if (MaxTitanSummon > 1) {
                System.out.println(Color.colorize(MaxTitanSummon + " COLOSSUS TITANS APPEARED!", "red"));
                System.out.println(Color.colorize("COLOSSUS TITANS COORDINATES : ", "red"));
            }
        }
        
        String showcoordinatesTitan = "";
        for (int i=0 ; i<MaxTitanSummon && i<groundMaxTitanSummon ; i++) {
            int index = ColossusTitan.getColossusTitanSpawnCounter();
            ColossusTitans[index] = new ColossusTitan();
            while (!ground.checkCoordinateForTitanIsEmpty(ColossusTitans[index])) {
                ColossusTitans[index].GenerateAgainColossusTitan();
            }
            if (ground.checkCoordinateForTitanIsEmpty(ColossusTitans[index])) {
                ground.putColossusTitan(ColossusTitans[index]);
                int space = ground.getSpaceAssignedToTitan();
                ColossusTitans[index].setColossusTitanCurrentSpace(space);
            }
            showcoordinatesTitan += Color.colorize("Row " + ColossusTitans[index].getColossusTitanCurrentRow()
                    + " Column " + ColossusTitans[index].getColossusTitanCurrentColumn() + "\n", "red");
        }
        System.out.println(showcoordinatesTitan);
    }
    
    /************************************************************************ 
     * Generate Shield on Ground that protect titans on the same column
     * 
     * Hour        Number of shields
     * 06-20    : 0
     * 25 && 35 : max 5
     * 45 && 55 : max 10
     * 65 && 75 : max 15
     * 85 && 95 (endless) : max 20 shield
     * >= 105 (hardcore endless) : max 25 shield
     ************************************************************************/
    public static void generateShield(){
        int MaxShieldSummon = 0;
        int groundMaxShieldSummon = 0;
        
        // generate Shield (if hour%5==0 && hour%10!=0)
        // 
        MaxShieldSummon = 0;
        groundMaxShieldSummon = ground.checkMaxShieldCanBeSummoned();
        if ( (gameHour == 25) || (gameHour == 35) ) {
            MaxShieldSummon = 5;
        }
        else if ( (gameHour == 45) || (gameHour == 55) ) {
            MaxShieldSummon = 10;
        }
        else if ( (gameHour == 65) || (gameHour == 75) ) {
            MaxShieldSummon = 15;
        }
        else if ( (gameHour == 85) || (gameHour == 95) ) {
            MaxShieldSummon = 20;
        }
        else if ( (gameHour >= 105) && ((gameHour%5 == 0) || (gameHour%10 != 0)) ) {
            MaxShieldSummon = 25;
        }
        else {
            MaxShieldSummon = 0;
        }
        
        if (MaxShieldSummon != 0) {
            System.out.println(Color.colorize(MaxShieldSummon + " SHIELDS APPEARED!", "red"));
            System.out.println(Color.colorize("SHIELDS COORDINATES : ", "red"));
        }
        
        String showcoordinatesShields = "";
        for (int i=0 ; i<MaxShieldSummon && i<groundMaxShieldSummon ; i++) {
            int index = Shield.getShieldSpawnCounter();
            shield[index] = new Shield();
            while (!ground.checkCoordinateForShieldIsEmpty(shield[index])) {
                shield[index].GenerateAgainShield();
            }
            if (ground.checkCoordinateForShieldIsEmpty(shield[index])) {
                ground.putShield(shield[index]);
            }
            showcoordinatesShields += Color.colorize("Row " + shield[index].getShieldCurrentRow()
                    + " Column " + shield[index].getShieldCurrentColumn() + "\n", "red");
        }
        System.out.println(showcoordinatesShields);
    }
    
    /************************************************************************ 
     * Control movement of Armoured Titans from Row 0 to Row 9
     * 
     * - Only move titan with HP > 0 (alive)
     * - Check for next empty slots on Ground for titans to move
     * - If move is permitted, 
     *    - Remove titan from current position
     *    - Set it to new position
     ************************************************************************/
    public static void movementArmouredTitan(){
        if (ArmouredTitan.getArmouredTitanSpawnCounter() > 0) {
            for (int i=0 ; i<ArmouredTitan.getArmouredTitanSpawnCounter() ; i++) {
                if ( (ArmouredTitans[i].getArmouredTitanHP() > 0) && (ArmouredTitans[i].getArmouredTitanCurrentRow() != 10) ) {
                    ArmouredTitans[i].MoveArmouredTitan();
                    if (ground.checkCoordinateForTitanIsEmpty(ArmouredTitans[i])) {
                        ground.removeArmouredTitan(ArmouredTitans[i]);
                        ArmouredTitans[i].setPermitToMoveArmouredTitan();
                        ground.putArmouredTitan(ArmouredTitans[i]);
                        int space = ground.getSpaceAssignedToTitan();
                        ArmouredTitans[i].setArmouredTitanCurrentSpace(space);
                    }
                    else if (ground.checkCoordinateForTitanIsEmpty(ArmouredTitans[i])) {
                        while (ArmouredTitans[i].getTestMoveCounter() < 4) {
                            ArmouredTitans[i].MoveIsRestrictedArmouredTitan();
                            if (ground.checkCoordinateForTitanIsEmpty(ArmouredTitans[i])) {
                                ground.removeArmouredTitan(ArmouredTitans[i]);
                                ArmouredTitans[i].setPermitToMoveArmouredTitan();
                                ground.putArmouredTitan(ArmouredTitans[i]);
                                int space = ground.getSpaceAssignedToTitan();
                                ArmouredTitans[i].setArmouredTitanCurrentSpace(space);
                                break;  // break while loop
                            }
                        }
                    }
                }
            }
        }
    }
    
    /************************************************************************ 
     * Control movement of Colossus Titans along Row 9
     * 
     * - Only move titan with HP > 0 (alive)
     * - Randomly move left and right along Row 9
     * - Check for next empty slots on Ground for titans to move
     * - If move is permitted, 
     *    - Remove titan from current position
     *    - Set it to new position
     ************************************************************************/
    public static void movementColossusTitan(){
        if (ColossusTitan.getColossusTitanSpawnCounter() > 0) {
            for (int i=0 ; i<ColossusTitan.getColossusTitanSpawnCounter() ; i++) {
                if ( (ColossusTitans[i].getColossusTitanHP() > 0) && (ColossusTitans[i].getColossusTitanCurrentRow() != 10) ) {
                    ColossusTitans[i].MoveColossusTitan();
                    if (ground.checkCoordinateForTitanIsEmpty(ColossusTitans[i])) {
                        ground.removeColossusTitan(ColossusTitans[i]);
                        ColossusTitans[i].setPermitToMoveColossusTitan();
                        ground.putColossusTitan(ColossusTitans[i]);
                        int space = ground.getSpaceAssignedToTitan();
                        ColossusTitans[i].setColossusTitanCurrentSpace(space);
                    }
                    else if (ground.checkCoordinateForTitanIsEmpty(ColossusTitans[i])) {
                        while (ColossusTitans[i].getTestMoveCounter() < 3) {
                            ColossusTitans[i].MoveIsRestrictedColossusTitan();
                            if (ground.checkCoordinateForTitanIsEmpty(ColossusTitans[i])) {
                                ground.removeColossusTitan(ColossusTitans[i]);
                                ColossusTitans[i].setPermitToMoveColossusTitan();
                                ground.putColossusTitan(ColossusTitans[i]);
                                int space = ground.getSpaceAssignedToTitan();
                                ColossusTitans[i].setColossusTitanCurrentSpace(space);
                                break;  // break while loop
                            }
                        }
                    }
                }
            }
        }
    }

    /************************************************************************ 
     * Ask player to use Nuke for every 10 hours
     * 
     * Nuke detroys all titans and shields on Ground (like nuclear)
     * 
     * Input:
     * - "Y" for yes
     * - 'Enter' to skip
     * 
     * Cost: 50 Coins
     ************************************************************************/
    public static void askToCallNuke() throws InterruptedException {
        input = new Scanner(System.in);
        String answer;
        // error handling for user input
        do {
            System.out.println("Do you want to use Nuke to kill all Titans and Shields? Cost : 50");
            System.out.print("Type 'Y' to call Nuke, else press enter to proceed : ");
            answer = input.nextLine().replaceAll("\\s", "").toUpperCase();
        } while (!answer.equals("") && !answer.equals("Y"));

        if (answer.equals("Y")){
            musicGameStart.stop();
            System.out.println("Calling Nuke!");
            coin.pay(50); // 50 coins to use nuke
            SoundSFX.NukeSFX();
            musicGameStart.play();
            Thread.sleep(500);

            //kill all Armoured Titans
            int ATcounter = ArmouredTitan.getArmouredTitanSpawnCounter();
            for (int i = 0; i < ATcounter; i++) {
                if (ArmouredTitans[i].getArmouredTitanHP() > 0 && ArmouredTitans[i].getArmouredTitanCurrentRow() != 10) {
                    ArmouredTitans[i].setDamageOnArmouredTitanHP(1000);
                    if (ArmouredTitans[i].getArmouredTitanHP() <= 0) {
                        ground.removeArmouredTitan(ArmouredTitans[i]);
                        ArmouredTitans[i].setIsKilledArmouredTitan();
                        killedArmouredTitanCounter++;
                    }
                }
            }

            //kill all Colossus Titans
            int CTcounter = ColossusTitan.getColossusTitanSpawnCounter();
            for (int i = 0; i < CTcounter; i++) {
                if (ColossusTitans[i].getColossusTitanHP() > 0 && ColossusTitans[i].getColossusTitanCurrentRow() != 10) {
                    ColossusTitans[i].setDamageOnColossusTitanHP(1000);
                    if (ColossusTitans[i].getColossusTitanHP() <= 0) {
                        ground.removeColossusTitan(ColossusTitans[i]);
                        ColossusTitans[i].setIsKilledColossusTitan();
                        killedColossusTitanCounter++;
                    }
                }
            }

            // kill all shield
            int ShieldCounter = Shield.getShieldSpawnCounter();
            for (int i = 0; i < ShieldCounter; i++) {
                if (shield[i].getShieldHP() > 0 && shield[i].getShieldCurrentRow() != 10) {
                    shield[i].setDamageOnShieldHP(1000);
                    if (shield[i].getShieldHP() <= 0) {
                        ground.removeShield(shield[i]);
                        shield[i].setIsKilledShield();
                        killedShieldCounter++;
                    }
                }
            }
        }
    }

    /************************************************************************ 
     * Control weapons to attack Armoured and Colossus Titans
     * Only attack titans on same column
     * Deal damage to titans based on weapon level (higher level, more damage)
     ************************************************************************/
    public static void attackTitans(int column, int damage) {
        //attack armoured titans
        for (int i = 0; i < ArmouredTitan.getArmouredTitanSpawnCounter(); i++) {
            if(ArmouredTitans[i].getArmouredTitanHP()>0 && ArmouredTitans[i].getArmouredTitanCurrentRow()!=10){
                //int rowAT = ArmouredTitans[i].getArmouredTitanCurrentRow();
                int columnAT = ArmouredTitans[i].getArmouredTitanCurrentColumn();
                if (column == columnAT) {
                    ArmouredTitans[i].setDamageOnArmouredTitanHP(damage);
                }
            }
        }

        //attack colossus titans
        for (int j = 0; j < ColossusTitan.getColossusTitanSpawnCounter(); j++) {
            if(ColossusTitans[j].getColossusTitanHP()>0 && ColossusTitans[j].getColossusTitanCurrentRow()!=10){
                int columnCT = ColossusTitans[j].getColossusTitanCurrentColumn();
                //int rowCT = ColossusTitans[j].getColossusTitanCurrentRow();
                if (column == columnCT) {
                    ColossusTitans[j].setDamageOnColossusTitanHP(damage);
                }
            }
        }
    }

    //debugging purposes
    //[!] Ctrl + "/" to uncomment 
    // public static void displayATHP(){
    //     for (int i = 0; i < ArmouredTitan.getArmouredTitanSpawnCounter(); i++) {
    //         System.out.print("AT" + i + " " + ArmouredTitans[i].getArmouredTitanHP() + " ");
    //     }
    //     System.out.println("");
    //     for (int i = 0; i < ColossusTitan.getColossusTitanSpawnCounter(); i++) {
    //         System.out.print("CT" + i + " " + ColossusTitans[i].getColossusTitanHP() + " ");
    //     }
    //     System.out.println("");
    //     for (int i = 0; i < Shield.getShieldSpawnCounter() ; i++) {
    //         System.out.print("Shield" + i + " " + shield[i].getShieldHP() + " ");
    //     }
    //     System.out.println("");
    // }

    /************************************************************************ 
     * Control Colossus Titans to attack walls on current column
     * Wall units HP is deducted based on titans attack point
     ************************************************************************/
    public static void attackColossusTitan(){
        for (int i=0 ; i<ColossusTitan.getColossusTitanSpawnCounter() ; i++) {
            if(ColossusTitans[i].getColossusTitanHP()>0 && ColossusTitans[i].getColossusTitanCurrentRow()!=10){
                int columnCT = ColossusTitans[i].getColossusTitanCurrentColumn();
                int attackCT = ColossusTitans[i].getColossusTitanAttack();
                walls[columnCT].reduceHP(attackCT);
            }
        }
    }

    //player's weapon attack titans and shields
    /************************************************************************ 
     * Control weapons on walls to attack titans (after Hour 5) and shields (after Hour 25) on Ground
     * Weapon only attack titans / shields on same lane
     * 
     * Only weapons with Level 1 and above can perform attack
     * 
     * Shields (#) will block weapon attacks on the same column
     *  - titans on same column will not take damage if shield is present
     * 
     * Overlapping titans on same coordinates will take same damage from weapons (AOE)
     * i.e. "AC"
     *      Both Armoured and Colossus Titans will take equal damage from the weapon on the same column
     ************************************************************************/
    public static void playerAttack() {
        //loop through column 0-9 to check for weapon
        for (int w = 0; w < weapons.length; w++) {
            //if got weapon
            if(weapons[w].getLevel()>0){
                int damage = weapons[w].getDamage();

                //if statement will enter after shield is being generated (gameHour >= 25)
                if (gameHour >= 25) {
                    int counter = 0; //prevent titan from being attacked multiple times
                    boolean permitToAttackTitan = false;
                    //loop through the whole shield array
                    for(int s=0; s<Shield.getShieldSpawnCounter(); s++) {

                        //if shield alive, check if shield column same with the weapons column(w)
                        if(shield[s].getShieldHP()>0 && shield[s].getShieldCurrentRow() != 10){
                            //int rowShield = shield[s].getShieldCurrentRow();
                            int columnShield = shield[s].getShieldCurrentColumn();
                            //if shield column = weapon column(w), attack shield only
                            if(columnShield == w){
                                shield[s].setDamageOnShieldHP(damage);
                                counter++; //when counter = 1, weapon will not attack titans
                            }
                        }

                        /*
                        this if statemnet will only be functional if there's no shield on the column and loops through the whole shield array
                        (counter == 0) is to check whether the shield protected the titans or not, if false, means shield already protect titans, so weapons cannot attack titans
                        (s == (Shield.getShieldSpawnCounter() - 1)) is to check all the shield available to protect the titans, if false, then weapon will attack titan
                         */
                        //loop through the entire shield array, if no shield on the same column as titans, set attack titan
                        if ((s == (Shield.getShieldSpawnCounter() - 1)) && (counter == 0)) {
                            permitToAttackTitan = true;
                        }

                        //else if no shield on the column, attack the titans
                        if (permitToAttackTitan) {
                            attackTitans(w, damage);
                            counter++;
                        }
                    }
                }
                //for game hour 5-24
                else{
                    attackTitans(w, damage);
                }
            }
        }
    }

    // remove dead obejcts from ground
    public static void removeDeadObjects(){
        //remove dead Armoured titan
        int ATcounter = ArmouredTitan.getArmouredTitanSpawnCounter();
        for (int i = 0; i < ATcounter; i++) {
            if (ArmouredTitans[i].getArmouredTitanHP() <= 0 && ArmouredTitans[i].getArmouredTitanCurrentRow() != 10) {
                ground.removeArmouredTitan(ArmouredTitans[i]);
                ArmouredTitans[i].setIsKilledArmouredTitan();
                killedArmouredTitanCounter++;
            }
        }

        //remove dead Colossus Titans
        int CTcounter = ColossusTitan.getColossusTitanSpawnCounter();
        for (int i = 0; i < CTcounter; i++) {
            if (ColossusTitans[i].getColossusTitanHP() <= 0 && ColossusTitans[i].getColossusTitanCurrentRow() != 10) {
                ground.removeColossusTitan(ColossusTitans[i]);
                ColossusTitans[i].setIsKilledColossusTitan();
                killedColossusTitanCounter++;
            }
        }

        //remove destroyed shield
        int ShieldCounter = Shield.getShieldSpawnCounter();
        for (int i = 0; i < ShieldCounter; i++) {
            if (shield[i].getShieldHP() <= 0 && shield[i].getShieldCurrentRow() != 10) {
                ground.removeShield(shield[i]);
                shield[i].setIsKilledShield();
                killedShieldCounter++;
            }
        }
    }

    /************************************************************************ 
     * Control Armoured Titans movement from Row 0 to Row 9
     * 
     * Calculate absolute distance between the Armoured Titan and each weapon on walls
     * Find out the closest weapon column to the titan
     * 
     * If there is 2 weapons of same distance, randomly choose 1 to move closer
     * 
     * Row 0 - 8:
     *  - If there is weapon on walls, move closer to the weapon one column at a time
     * 
     * Row 9:
     *  - If there is weapon on current coulmn, 
     *      - Attack the weapon on wall
     *      - For each attack, weapon level drops by 1
     *        i.e. 3 -> 2 after one attack from Armoured Titan
     *  - If there is still weapon on walls on another column, 
     *      - Move closer to the weapon one column at a time
     *  - If there is no weapon on walls, 
     *      - Start damage one wall unit at the current column
     ************************************************************************/
    public static void attackArmouredTitan() {

        // loop through Armoured Titans generated and stored in arrays
        for (int i = 0; i < ArmouredTitan.getArmouredTitanSpawnCounter(); i++) {
            ArmouredTitan currentArmouredTitan = ArmouredTitans[i];
            int ArmouredTitanHP = currentArmouredTitan.getArmouredTitanHP();
            int currentRow = currentArmouredTitan.getArmouredTitanCurrentRow();

            // check if ArmouredTitan is alive and on Row 9
            if (ArmouredTitanHP > 0 && (currentRow >= 0 && currentRow <= 9)) {
                int columnAT = currentArmouredTitan.getArmouredTitanCurrentColumn();
                int attackAT = currentArmouredTitan.getArmouredTitanAttack();

                // check if current titan column has weapon on wall
                if (weapons[columnAT].getLevel() > 0) {
                    if (currentRow >= 0 && currentRow <= 8) {
                        currentArmouredTitan.setClosestWeaponColumn(columnAT);
                        currentArmouredTitan.MoveArmouredTitan();
                    }
                    // if ArmouredTitan on Row 9, attack the weapon
                    if (currentRow == 9) {
                        // weapon is detroyed by titan (level decrease by 1)
                        weapons[columnAT].takeDamageFromTitan();
                    }
                } else {
                    // store distance between titan and each weapon
                    int[] distance = new int[10];
                
                    // calculate distance between titan and each weapons
                    for (int j = 0; j < weapons.length; j++) {
                        int weaponLevel = weapons[j].getLevel();
                        
                        // only calculate distance if weapon is not destroyed
                        if (weaponLevel != 0) {
                            // get absolute value for distance
                            distance[j] = Math.abs(j - columnAT);
                        } else {
                            // no weapon on that particular wall, distance is neglected
                            distance[j] = -1;
                        }
                    }

                    // ArmouredTitan move to closest weapon column (smallest distance)
                    int closestDistance = 10;
                    ArrayList<Integer> closestWeaponIndex = new ArrayList<Integer>();
                    for (int k = 0; k < distance.length; k++) {
                        if (distance[k] >= 0 && distance[k] < closestDistance) {
                            closestWeaponIndex.add(k);
                        } 
                    }

                    // start to attack wall at current column when no weapon on all walls
                    if (closestWeaponIndex.size() == 0) {
                        if (currentRow == 9) {
                            walls[columnAT].reduceHP(attackAT);
                        }
                    } else {
                        // randomly choose direction to move if they are 2 same distances
                        // i.e.   <- AT ->
                        //      ***     ***
                        //      --- --- --- ---
                        Collections.shuffle(closestWeaponIndex);

                        // always get the first weapon index in ArrayList
                        currentArmouredTitan.setClosestWeaponColumn(closestWeaponIndex.get(0));
                        currentArmouredTitan.MoveArmouredTitan();
                    }
                }             
            }
        }
    }
}