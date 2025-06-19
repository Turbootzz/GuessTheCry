package com.guessthecry.webservices.question;

import java.util.List;

public class QuestionDTO {
    private String pokemonName;
    private String audioUrl;
    private List<ChoiceDTO> choices; // null when expert mode
    private String imageUrl;
    private String hint; // null when normal mode

    public QuestionDTO() {}

    public QuestionDTO(String pokemonName, String audioUrl, List<ChoiceDTO> choices, String imageUrl, String hint) {
        this.pokemonName = pokemonName;
        this.audioUrl = audioUrl;
        this.choices = choices;
        this.imageUrl = imageUrl;
        this.hint = hint;
    }

    public String getPokemonName() {
        return pokemonName;
    }
    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }
    public String getAudioUrl() {
        return audioUrl;
    }
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
    public List<ChoiceDTO> getChoices() {
        return choices;
    }
    public void setChoices(List<ChoiceDTO> choices) {
        this.choices = choices;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
}