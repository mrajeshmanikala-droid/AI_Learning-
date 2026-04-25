package service;

import model.Question;
import model.User;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class QuizService {
    private List<Question> allQuestions;
    private Scanner scanner;

    public QuizService(List<Question> questions) {
        this.allQuestions = questions;
        this.scanner = new Scanner(System.in);
    }

    public void startQuiz(User user, String topic) {
        List<Question> topicQuestions = allQuestions.stream()
            .filter(q -> q.getTopic().equalsIgnoreCase(topic))
            .collect(Collectors.toList());

        if (topicQuestions.isEmpty()) {
            System.out.println("No questions found for topic: " + topic);
            return;
        }

        // Shuffle questions for variety
        java.util.Collections.shuffle(topicQuestions);
        
        System.out.println("\n--- Starting Quiz: " + topic + " ---");
        System.out.println("⚠️ You have 30 seconds for the entire quiz!");
        long startTime = System.currentTimeMillis();
        long timeLimit = 30000; // 30 seconds
        
        int score = 0;

        for (int i = 0; i < topicQuestions.size(); i++) {
            // Check if time is up
            if (System.currentTimeMillis() - startTime > timeLimit) {
                System.out.println("\n⏰ TIME'S UP!");
                break;
            }

            Question q = topicQuestions.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.getText());
            for (int j = 0; j < q.getOptions().size(); j++) {
                System.out.println((j + 1) + ". " + q.getOptions().get(j));
            }

            System.out.print("Your answer: ");
            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next(); // clear invalid input
            }

            if (choice - 1 == q.getCorrectOptionIndex()) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong.");
            }
        }

        long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("\nQuiz Finished! You scored: " + score + "/" + topicQuestions.size());
        System.out.println("Time taken: " + timeTaken + " seconds");
        
        user.addScore(score);
        user.updateTopicProgress(topic, score);
    }
}
