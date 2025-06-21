package com.guessthecry.webservices.question;

import com.guessthecry.config.S3Config;
import com.guessthecry.model.Pokemon;
import com.guessthecry.repository.PokemonRepository;
import com.guessthecry.service.HintService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.stream.Collectors;

@Path("/api/quiz")
public class QuestionResource {
    @Inject
    private PokemonRepository pokemonRepository;

    @Inject
    private HintService hintService;

    @GET
    @Path("/question")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion(@QueryParam("mode") String mode) {
        List<Pokemon> all = pokemonRepository.findAllWithHints();
        if (all.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Random random = new Random();
        Pokemon correct = all.get(random.nextInt(all.size()));

        QuestionDTO dto = new QuestionDTO();
        dto.setPokemonName(correct.getName());
        dto.setAudioUrl(S3Config.getEndpoint() + "/" + S3Config.getBucket() + "/" + correct.getAudioPath());
        dto.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + correct.getPokedexId() + ".png");

        if ("normal".equalsIgnoreCase(mode)) {
            Set<Pokemon> choiceSet = new HashSet<>();
            choiceSet.add(correct);
            while (choiceSet.size() < 4) {
                choiceSet.add(all.get(random.nextInt(all.size())));
            }

            List<ChoiceDTO> choiceDTOs = choiceSet.stream()
                    .map(p -> new ChoiceDTO(p.getName(), p.getPokedexId()))
                    .collect(Collectors.toList());

            Collections.shuffle(choiceDTOs);
            dto.setChoices(choiceDTOs);
            dto.setHints(new ArrayList<>()); // empty list instead of null
        } else if ("expert".equalsIgnoreCase(mode)) {
            List<String> hints = hintService.getHints(correct.getName());
            dto.setHints(hints != null ? hints : new ArrayList<>());
            dto.setChoices(null);
        }

        return Response.ok(dto).build();
    }
}