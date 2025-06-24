package com.guessthecry.webservices.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guessthecry.config.S3Config;
import com.guessthecry.model.*;
import com.guessthecry.repository.*;
import com.guessthecry.service.HintService;
import com.guessthecry.service.UserStatsService;
import com.guessthecry.webservices.authentication.UserStatsDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Path("/game")
@RolesAllowed("ROLE_USER")
public class GameResource {

    private static final int GAME_LENGTH = 10;

    @Inject private UserRepository userRepository;
    @Inject private GameSessionRepository gameSessionRepository;
    @Inject private PokemonRepository pokemonRepository;
    @Inject private HintService hintService;
    @Inject private UserStatsService userStatsService; // Service to update stats
    @Inject private ObjectMapper objectMapper; // For JSON conversion

    // helper class for JSON storage
    private record GameQuestion(int pokemonId, String correctAnswer) {}

    // helper
    private List<Pokemon> getPokemonPoolByGeneration(int generationNumber) {
        if (generationNumber == 0) {
            return pokemonRepository.findAll();
        } else {
            String generationString = "gen" + generationNumber;
            return pokemonRepository.findByGeneration(generationString);
        }
    }

    @POST
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startGame(@QueryParam("mode") String mode, @QueryParam("generation") @DefaultValue("0") int generationNumber) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new WebApplicationException(Response.Status.UNAUTHORIZED));

        List<Pokemon> pokemonPool = getPokemonPoolByGeneration(generationNumber);

        if (pokemonPool.size() < GAME_LENGTH) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Not enough Pokémon in this generation to start a game.")
                    .build();
        }

        Collections.shuffle(pokemonPool);
        List<Pokemon> gamePokemon = pokemonPool.subList(0, GAME_LENGTH);

        List<GameQuestion> questions = gamePokemon.stream()
                .map(p -> new GameQuestion(p.getPokedexId(), p.getName()))
                .toList();

        GameSession session = new GameSession();
        session.setUser(user);
        session.setDifficulty(mode);
        session.setGeneration(generationNumber);
        session.setQuestionsJson(objectMapper.writeValueAsString(questions));
        session.setAnswersJson("[]"); // empty array for answers
        gameSessionRepository.save(session);

        // prepare first question
        QuestionDTO firstQuestionDto = createQuestionDTO(gamePokemon.get(0), mode, pokemonPool);
        GameStartDTO responseDto = new GameStartDTO(session.getId(), firstQuestionDto);

        return Response.ok(responseDto).build();
    }

    @POST
    @Path("/{gameId}/answer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitAnswer(@PathParam("gameId") UUID gameId, AnswerRequestDTO answerRequest) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new WebApplicationException(Response.Status.UNAUTHORIZED));

        GameSession session = gameSessionRepository.findByIdAndUser(gameId, user)
                .orElseThrow(() -> new WebApplicationException("Game not found or not owned by user", Response.Status.FORBIDDEN));

        if (session.isCompleted()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Game already completed").build();
        }

        List<GameQuestion> questions = objectMapper.readValue(session.getQuestionsJson(), new TypeReference<>() {});
        List<AnswerResult> answers = objectMapper.readValue(session.getAnswersJson(), new TypeReference<>() {});

        if (answerRequest.getQuestionIndex() != answers.size()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Answering questions out of order").build();
        }

        GameQuestion currentQuestion = questions.get(answerRequest.getQuestionIndex());
        boolean isCorrect = currentQuestion.correctAnswer().equalsIgnoreCase(answerRequest.getUserAnswer());

        answers.add(new AnswerResult(answerRequest.getQuestionIndex(), answerRequest.getUserAnswer(), isCorrect));

        if (isCorrect) {
            session.setScore(session.getScore() + 1);
        }
        session.setAnswersJson(objectMapper.writeValueAsString(answers));

        AnswerResponseDTO responseDto = new AnswerResponseDTO();
        responseDto.setCorrect(isCorrect);
        responseDto.setCorrectAnswerPokemonName(currentQuestion.correctAnswer());
        responseDto.setCorrectAnswerImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + currentQuestion.pokemonId() + ".png");

        if (answers.size() == GAME_LENGTH) {
            session.setCompleted(true);
            UserStatsDTO finalStats = userStatsService.updateUserStats(user, session.getDifficulty(), session.getScore(), GAME_LENGTH);
            responseDto.setFinalResult(finalStats);
            responseDto.setNextQuestion(null);
        } else {
            // get pokemonPool for next question
            List<Pokemon> pokemonPool = getPokemonPoolByGeneration(session.getGeneration());
            Pokemon nextPokemon = pokemonRepository.findByPokedexId(questions.get(answers.size()).pokemonId()).orElseThrow();
            responseDto.setNextQuestion(createQuestionDTO(nextPokemon, session.getDifficulty(), pokemonPool));
            responseDto.setFinalResult(null); // game is not over yet
        }

        gameSessionRepository.save(session);
        return Response.ok(responseDto).build();
    }

    // Helper to make QuestionDTO
    private QuestionDTO createQuestionDTO(Pokemon correct, String mode, List<Pokemon> pokemonPool) {
        QuestionDTO dto = new QuestionDTO();
        dto.setPokemonName(correct.getName());
        dto.setAudioUrl(S3Config.getEndpoint() + "/" + S3Config.getBucket() + "/" + correct.getAudioPath());
        dto.setImageUrl("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + correct.getPokedexId() + ".png");

        if ("normal".equalsIgnoreCase(mode)) {
            Set<Pokemon> choiceSet = new HashSet<>();
            choiceSet.add(correct);

            Random random = new Random();
            while (choiceSet.size() < 4) {
                choiceSet.add(pokemonPool.get(random.nextInt(pokemonPool.size())));
            }

            List<ChoiceDTO> choiceDTOs = choiceSet.stream()
                    .map(p -> new ChoiceDTO(p.getName(), p.getPokedexId()))
                    .collect(Collectors.toList());

            Collections.shuffle(choiceDTOs);
            dto.setChoices(choiceDTOs);
            dto.setHints(new ArrayList<>());
        } else if ("expert".equalsIgnoreCase(mode)) {
            List<String> hints = hintService.getHints(correct.getName());
            dto.setHints(hints != null ? hints : new ArrayList<>());
            dto.setChoices(new ArrayList<>()); // always send empty array and not null
        }

        return dto;
    }

    // Helper class for JSON storage for answers
    private record AnswerResult(int questionIndex, String userAnswer, boolean isCorrect) {}
}