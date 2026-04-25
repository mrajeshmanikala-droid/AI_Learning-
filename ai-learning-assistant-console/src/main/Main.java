package main;

import model.User;
import service.QuizService;
import service.UserService;
import util.DataLoader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("========================================");
        System.out.println("   🚀 Welcome to AI Learning Assistant");
        System.out.println("========================================");

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        User user = new User(name);

        QuizService quizService = new QuizService(DataLoader.loadQuestions());
        UserService userService = new UserService();

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1. Start Java Quiz");
            System.out.println("2. Start DSA Quiz");
            System.out.println("3. Start Ethical Hacking Quiz");
            System.out.println("4. View My Score");
            System.out.println("5. View Leaderboard");
            System.out.println("6. Save & Exit");
            System.out.print("Choose an option: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next(); // clear
            }

            switch (choice) {
                case 1: quizService.startQuiz(user, "Java"); break;
                case 2: quizService.startQuiz(user, "DSA"); break;
                case 3: quizService.startQuiz(user, "Ethical Hacking"); break;
                case 4: 
                    System.out.println("\n👤 --- User Profile --- 👤");
                    System.out.println("Name: " + user.getName());
                    System.out.println("Total Score: " + user.getTotalScore());
                    break;
                case 5:
                    userService.showLeaderboard();
                    break;
                case 6:
                    userService.saveProgress(user);
                    exit = true;
                    System.out.println("Goodbye, " + name + "!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }
}
