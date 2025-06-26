package com.guessthecry.webservices.pokemon;

import com.guessthecry.model.Pokemon;
import com.guessthecry.service.PokemonService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pokemon")
public class PokemonResource {

    @Inject
    private PokemonService pokemonService;

    @GET
    @Path("/names")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPokemonNames() {
        // get cached list of all pokemon (0 for all generations)
        List<String> names = pokemonService.getPokemonPoolByGeneration(0).stream()
                .map(Pokemon::getName)
                .sorted() // alphabethic
                .toList();

        return Response.ok(names).build();
    }
}