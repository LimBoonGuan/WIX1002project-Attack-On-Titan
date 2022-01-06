package Assignment_GRP6;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Scanner;

/* Remember to create the .txt below in the package
 * LEADERBOARD_ENDLESS.txt and LEADERBOARD_NORMAL.txt file content
 * player name
 * hours
 * 
 * [!] always updateLeaderboard before saveLeaderboard
 * [!] always saveLeaderboard before exit the game
 */
public class Leaderboard {
    private String filename;
    private String gameMode;
    private LinkedHashMap<String, Integer> leaderboard;  // use hashmap to store leaderboard
    File file;
    
    public Leaderboard(String filename, String gameMode) {
        file = new File("./Leaderboard/" + filename);
        this.gameMode = gameMode;
        leaderboard = new LinkedHashMap<>();
    }
    
    public String getFilename() {
        return filename;
    }
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    // display the leaderboard
    public void display() {
        System.out.println("____________________________________________");
        System.out.printf("%14s %11s\n", " ", gameMode);
        System.out.printf("    %-16s|%16s\n", "Name", "Hours Survived");
        int count = 1;
        for (String item : leaderboard.keySet()) { 
            System.out.printf("#%d  %-16s|%16d\n", count, item, leaderboard.get(item));
            count++;
        }
        System.out.println("____________________________________________");
    }
    
    // load the leaderboard record from .txt
    public void loadLeaderboard() {
        try {
            Scanner inputStream = new Scanner(new FileInputStream(file));
            while (inputStream.hasNextLine()) {
                String playerName = inputStream.next();
                inputStream.nextLine();
                Integer hours = inputStream.nextInt();
                inputStream.nextLine();
                leaderboard.put(playerName, hours);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(filename + " was not found");
        }
    }
    
    // when game over, check and update leaderboard
    // [!] always updateLeaderboard before saveLeaderboard
    public void updateLeaderboard(String playerName, int hours) {
        LinkedHashMap<String, Integer> sortedLeaderboard = new LinkedHashMap<>();
        ArrayList<Integer> scoreList = new ArrayList<>();
        
        // insert palyer record into leaderboard LinkedHashMap
        leaderboard.put(playerName, (Integer) hours);
        
        // add all the player's scores into an arraylist
        for (Entry<String, Integer> entry : leaderboard.entrySet()) {
            scoreList.add(entry.getValue());
        }
        
        // sort the player's score arrayList in descending order
        Collections.sort(scoreList, Collections.reverseOrder());
        
        // arrange the TOP 3 players with their respective scores in a new leaderboard
        int count = 0;
        for (int score : scoreList) {
            for (Entry<String, Integer> entry : leaderboard.entrySet()) {
                if (entry.getValue().equals(score) && count < 3) {
                    sortedLeaderboard.put(entry.getKey(), score);
                    count++;
                }
            }
        }
        // update the leaderboard
        leaderboard = sortedLeaderboard;
    }
    
    // save the TOP 3 players record on leaderboard to .txt
    // [!] always saveLeaderboard before exit the program
    public void saveLeaderboard() {
        try {
            PrintWriter outputStream = new PrintWriter(new FileOutputStream(file));
            for(Entry<String, Integer> entry : leaderboard.entrySet()) {
                outputStream.println(entry.getKey());
                outputStream.println(entry.getValue());    
            } 
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Problem with file output");
        }
    }
}