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

    // setters are for tests
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }
    public int getPokedexId() { return pokedexId; }
    public void setPokedexId(int pokedexId) { this.pokedexId = pokedexId; }
    public String getGeneration() { return generation; }
    public void setGeneration(String generation) { this.generation = generation; }
    public List<String> getHints() { return hints; }
    public void setHints(List<String> hints) { this.hints = hints; }
}