package com.guessthecry.repository;


import com.guessthecry.model.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    Optional<Pokemon> findByPokedexId(int pokedexId);

    @Query("SELECT p FROM Pokemon p LEFT JOIN FETCH p.hints WHERE p.id = :id")
    Optional<Pokemon> findByIdWithHints(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Pokemon p LEFT JOIN FETCH p.hints")
    List<Pokemon> findAllWithHints();
}