package com.guessthecry.model;

import jakarta.persistence.*;

@Entity
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String difficulty; // "normal", "expert", etc.

    private int gamesPlayed = 0;
    private double averageAccuracy = 0.0;

    public UserStats(Long id, User user, String difficulty, int gamesPlayed, double averageAccuracy) {
        this.id = id;
        this.user = user;
        this.difficulty = difficulty;
        this.gamesPlayed = gamesPlayed;
        this.averageAccuracy = averageAccuracy;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
    public double getAverageAccuracy() {
        return averageAccuracy;
    }
    public void setAverageAccuracy(double averageAccuracy) {
        this.averageAccuracy = averageAccuracy;
    }
}