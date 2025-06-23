package com.guessthecry.webservices.authentication;

import com.guessthecry.model.User;
import com.guessthecry.model.UserStats;
import com.guessthecry.repository.UserRepository;
import com.guessthecry.repository.UserStatsRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserStatsRepository statsRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static class UserDTO {
        public String username;
        public String password;
    }

    @POST
    @Path("/register")
    public Response register(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.username).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Username already exists").build();
        }

        User user = new User();
        user.setUsername(userDTO.username);
        user.setPasswordHash(passwordEncoder.encode(userDTO.password));

        userRepository.save(user);

        return Response.ok(Map.of("message", "User registered")).build();
    }

    @POST
    @Path("/login")
    public Response login(UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findByUsername(userDTO.username);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(userDTO.password, user.getPasswordHash())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok(Map.of("message", "Login successful")).build();
    }

    @GET
    @Path("/stats/{username}")
    public Response getStats(@PathParam("username") String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User user = userOpt.get();
        List<UserStats> stats = statsRepository.findAllByUser(user);

        List<Map<String, Object>> statDtos = new ArrayList<>();
        for (UserStats stat : stats) {
            statDtos.add(Map.of(
                "difficulty", stat.getDifficulty(),
                "gamesPlayed", stat.getGamesPlayed(),
                "averageAccuracy", stat.getAverageAccuracy()
            ));
        }

        return Response.ok(statDtos).build();
    }
}
