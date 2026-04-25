package model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private int totalScore;
    private Map<String, Integer> topicProgress; // Topic -> Score

    public User(String name) {
        this.name = name;
        this.totalScore = 0;
        this.topicProgress = new HashMap<>();
    }

    public String getName() { return name; }
    public int getTotalScore() { return totalScore; }
    public void addScore(int points) { this.totalScore += points; }
    public Map<String, Integer> getTopicProgress() { return topicProgress; }
    
    public void updateTopicProgress(String topic, int score) {
        topicProgress.put(topic, Math.max(topicProgress.getOrDefault(topic, 0), score));
    }
}
