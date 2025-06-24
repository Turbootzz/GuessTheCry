package com.guessthecry.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private int generation;

    // json-string or table to save questions and answers
    @Column(columnDefinition = "TEXT", nullable = false)
    private String questionsJson; // visualise: "[{"pokemonId": 25, "correctAnswer": "Pikachu"}, ...]"

    @Column(columnDefinition = "TEXT")
    private String answersJson; // visualise: "[{"questionIndex": 0, "userAnswer": "Pikachu", "isCorrect": true}, ...]"

    private boolean completed = false;
    private int score = 0;

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public int getGeneration() {
        return generation;
    }
    public void setGeneration(int generation) {
        this.generation = generation;
    }
    public String getQuestionsJson() {
        return questionsJson;
    }
    public void setQuestionsJson(String questionsJson) {
        this.questionsJson = questionsJson;
    }
    public String getAnswersJson() {
        return answersJson;
    }
    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}