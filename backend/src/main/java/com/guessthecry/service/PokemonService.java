package com.guessthecry.service;

import com.guessthecry.model.Pokemon;
import com.guessthecry.repository.PokemonRepository;
import jakarta.inject.Inject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonService {

    @Inject
    private PokemonRepository pokemonRepository;

    @Cacheable("pokemon-by-generation")
    public List<Pokemon> getPokemonPoolByGeneration(int generationNumber) {
        System.out.println("Fetching Pokémon from DB for generation: " + (generationNumber == 0 ? "All" : generationNumber));

        if (generationNumber == 0) {
            return pokemonRepository.findAll();
        } else {
            String generationString = "gen" + generationNumber;
            return pokemonRepository.findByGeneration(generationString);
        }
    }

    @Cacheable("pokemon-by-pokedex-id")
    public Optional<Pokemon> findByPokedexId(int pokedexId) {
        System.out.println("Fetching Pokémon from DB for Pokedex ID: " + pokedexId);
        return pokemonRepository.findByPokedexId(pokedexId);
    }
}