package com.guessthecry.webservices.game;

import com.guessthecry.webservices.authentication.UserStatsDTO;

public class AnswerResponseDTO {
    private boolean correct;
    private String correctAnswerPokemonName;
    private String correctAnswerImageUrl;
    private QuestionDTO nextQuestion; // Null when game is over
    private UserStatsDTO finalResult; // Null if game isnt over

    public AnswerResponseDTO() {}

    public AnswerResponseDTO(boolean correct, String correctAnswerPokemonName, String correctAnswerImageUrl, QuestionDTO nextQuestion, UserStatsDTO finalResult) {
        this.correct = correct;
        this.correctAnswerPokemonName = correctAnswerPokemonName;
        this.correctAnswerImageUrl = correctAnswerImageUrl;
        this.nextQuestion = nextQuestion;
        this.finalResult = finalResult;
    }

    public boolean isCorrect() {
        return correct;
    }
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
    public String getCorrectAnswerPokemonName() {
        return correctAnswerPokemonName;
    }
    public void setCorrectAnswerPokemonName(String correctAnswerPokemonName) {
        this.correctAnswerPokemonName = correctAnswerPokemonName;
    }
    public String getCorrectAnswerImageUrl() {
        return correctAnswerImageUrl;
    }
    public void setCorrectAnswerImageUrl(String correctAnswerImageUrl) {
        this.correctAnswerImageUrl = correctAnswerImageUrl;
    }
    public QuestionDTO getNextQuestion() {
        return nextQuestion;
    }
    public void setNextQuestion(QuestionDTO nextQuestion) {
        this.nextQuestion = nextQuestion;
    }
    public UserStatsDTO getFinalResult() {
        return finalResult;
    }
    public void setFinalResult(UserStatsDTO finalResult) {
        this.finalResult = finalResult;
    }
}
