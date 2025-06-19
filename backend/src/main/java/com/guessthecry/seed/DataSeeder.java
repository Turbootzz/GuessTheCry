package com.guessthecry.seed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guessthecry.model.Pokemon;
import com.guessthecry.repository.PokemonRepository;
import com.guessthecry.webservices.pokemonData.PokemonDataDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class DataSeeder {

    private final PokemonRepository repository;

    public DataSeeder(PokemonRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() throws IOException {
        if (repository.count() > 0) return;

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<PokemonDataDTO>> typeRef = new TypeReference<>() {};
        InputStream input = getClass().getResourceAsStream("/data/pokemon-data.json");

        List<PokemonDataDTO> list = mapper.readValue(input, typeRef);

        List<Pokemon> pokemons = list.stream()
                .map(dto -> new Pokemon(dto.name, dto.generation, dto.audioPath, dto.pokedexId, dto.hints))
                .toList();

        repository.saveAll(pokemons);
    }
}