package service;

import model.User;
import java.io.*;
import java.util.*;

public class UserService {
    private static final String LEADERBOARD_FILE = "leaderboard.txt";

    public void saveProgress(User user) {
        // Save user's session data
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEADERBOARD_FILE, true))) {
            writer.println(user.getName() + "|" + user.getTotalScore());
            System.out.println("Score saved to leaderboard!");
        } catch (IOException e) {
            System.err.println("Error saving leaderboard: " + e.getMessage());
        }
    }

    public void showLeaderboard() {
        List<String[]> scores = new ArrayList<>();
        File file = new File(LEADERBOARD_FILE);
        
        if (!file.exists()) {
            System.out.println("No leaderboard data yet.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    scores.add(parts);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading leaderboard.");
        }

        // Sort by score descending
        scores.sort((a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

        System.out.println("\n🏆 --- LEADERBOARD --- 🏆");
        int count = Math.min(scores.size(), 5);
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + scores.get(i)[0] + " - " + scores.get(i)[1] + " pts");
        }
    }
}
