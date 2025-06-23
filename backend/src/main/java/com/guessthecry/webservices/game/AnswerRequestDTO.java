package com.guessthecry.webservices.game;

public class AnswerRequestDTO {
    private int questionIndex; // index of question (0-9)
    private String userAnswer;

    public AnswerRequestDTO() {}

    public AnswerRequestDTO(int questionIndex, String userAnswer) {
        this.questionIndex = questionIndex;
        this.userAnswer = userAnswer;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }
    public void setQuestionIndex(int questionIndex) {
        this.questionIndex = questionIndex;
    }
    public String getUserAnswer() {
        return userAnswer;
    }
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
