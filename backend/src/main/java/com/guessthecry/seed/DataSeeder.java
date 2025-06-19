package com.guessthecry.seed;

import com.guessthecry.model.Pokemon;
import com.guessthecry.repository.PokemonRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataSeeder {

    private final PokemonRepository repository;

    public DataSeeder(PokemonRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            repository.save(new Pokemon("pikachu", "gen1", "gen1/pikachu.ogg", 25, List.of("Electric", "Starter")));
            repository.save(new Pokemon("bulbasaur", "gen1", "gen1/bulbasaur.ogg", 1, List.of("Grass", "Starter")));
            repository.save(new Pokemon("charmander", "gen1", "gen1/charmander.ogg", 4, List.of("Fire", "Starter")));
            repository.save(new Pokemon("squirtle", "gen1", "gen1/squirtle.ogg", 7,List.of("Water", "Starter")));
        }
    }
}