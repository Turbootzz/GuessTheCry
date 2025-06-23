package com.guessthecry.webservices.game;

import java.util.UUID;

public class GameStartDTO {
    private UUID gameId;
    private QuestionDTO firstQuestion;

    public GameStartDTO(UUID gameId, QuestionDTO firstQuestion) {
        this.gameId = gameId;
        this.firstQuestion = firstQuestion;
    }

    public GameStartDTO() {}

    public UUID getGameId() {
        return gameId;
    }
    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }
    public QuestionDTO getFirstQuestion() {
        return firstQuestion;
    }
    public void setFirstQuestion(QuestionDTO firstQuestion) {
        this.firstQuestion = firstQuestion;
    }
}