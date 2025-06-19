package com.guessthecry.webservices.question;

public class ChoiceDTO {
    private String name;
    private String imageUrl;

    public ChoiceDTO(String name, int pokedexId) {
        this.name = name;
        this.imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokedexId + ".png";
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getName() {
        return name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}