package com.guessthecry.webservices.authentication;

public class UserStatsDTO {
    private String difficulty;
    private int gamesPlayed;
    private double averageAccuracy;

    public UserStatsDTO() {
    }

    public UserStatsDTO(String difficulty, int gamesPlayed, double averageAccuracy) {
        this.difficulty = difficulty;
        this.gamesPlayed = gamesPlayed;
        this.averageAccuracy = averageAccuracy;
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