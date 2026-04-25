package util;

import model.Question;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataLoader {
    private static final String FILE_PATH = "questions.txt";

    public static List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            System.err.println("Warning: questions.txt not found. Using default internal data.");
            return getInternalBackup();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String topic = parts[0];
                    String difficulty = parts[1];
                    String text = parts[2];
                    List<String> options = Arrays.asList(parts[3].split(","));
                    int correctIndex = Integer.parseInt(parts[4]);
                    String explanation = parts[5];
                    
                    questions.add(new Question(text, options, correctIndex, explanation, topic, difficulty));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading questions.txt: " + e.getMessage());
        }

        return questions.isEmpty() ? getInternalBackup() : questions;
    }

    private static List<Question> getInternalBackup() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question(
            "What is 2+2 in Java?",
            Arrays.asList("3", "4", "5", "6"),
            1,
            "Basic math.",
            "Java",
            "Beginner"
        ));
        return questions;
    }
}
