package com.guessthecry.webservices.game;

import java.util.ArrayList;
import java.util.List;

public class QuestionDTO {
    private String pokemonName;
    private String audioUrl;
    private List<ChoiceDTO> choices; // null when expert mode
    private String imageUrl;
    private List<String> hints = new ArrayList<>();// null when normal mode

    public QuestionDTO() {}

    public QuestionDTO(String pokemonName, String audioUrl, List<ChoiceDTO> choices, String imageUrl, List<String> hints) {
        this.pokemonName = pokemonName;
        this.audioUrl = audioUrl;
        this.choices = choices;
        this.imageUrl = imageUrl;
        this.hints = hints;
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
    public List<String> getHints() {
        return hints;
    }
    public void setHints(List<String> hints) {
        this.hints = hints;
    }
}