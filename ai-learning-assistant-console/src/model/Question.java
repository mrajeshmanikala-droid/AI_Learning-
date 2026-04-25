package model;

import java.util.List;

public class Question {
    private String text;
    private List<String> options;
    private int correctOptionIndex;
    private String explanation;
    private String topic;
    private String difficulty;

    public Question(String text, List<String> options, int correctOptionIndex, String explanation, String topic, String difficulty) {
        this.text = text;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
        this.topic = topic;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public String getText() { return text; }
    public List<String> getOptions() { return options; }
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public String getExplanation() { return explanation; }
    public String getTopic() { return topic; }
    public String getDifficulty() { return difficulty; }
}
