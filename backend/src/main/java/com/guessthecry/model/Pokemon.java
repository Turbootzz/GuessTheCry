package com.guessthecry.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String generation;
    private String audioPath;
    private int pokedexId;

    @ElementCollection
    private List<String> hints;

    public Pokemon() {}

    public Pokemon(String name, String generation, String audioPath, int pokedexId, List<String> hints) {
        this.name = name;
        this.generation = generation;
        this.audioPath = audioPath;
        this.pokedexId = pokedexId;
        this.hints = hints;
    }

    public String getName() {
        return name;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public int getPokedexId() {
        return pokedexId;
    }

    public List<String> getHints() {
        return hints;
    }
}