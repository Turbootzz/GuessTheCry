package com.guessthecry.webservices.authentication;

import com.guessthecry.model.User;
import com.guessthecry.model.UserStats;
import com.guessthecry.repository.UserRepository;
import com.guessthecry.repository.UserStatsRepository;
import com.guessthecry.utils.JwtUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private JwtUtil jwtUtil;

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(UserDTO userDto) {
        if (userDto.getUsername() == null || userDto.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing username or password").build();
        }

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Username already exists").build();
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);

        // give response without password
        UserDTO responseDto = new UserDTO(user.getId(), user.getUsername());
        return Response.status(Response.Status.CREATED).entity(responseDto).build();
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserDTO userDto) {
        if (userDto.getUsername() == null || userDto.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing username or password").build();
        }

        Optional<User> userOpt = userRepository.findByUsername(userDto.getUsername());
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPasswordHash())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = jwtUtil.generateToken(user.getUsername(), "USER"); // Pas rol aan indien nodig

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("token", token);

        return Response.ok(responseMap).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User user = userOpt.get();
        UserDTO responseDto = new UserDTO(user.getId(), user.getUsername());
        return Response.ok(responseDto).build();
    }

    @GET
    @Path("/{id}/stats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStats(@PathParam("id") Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User user = userOpt.get();
        List<UserStats> stats = statsRepository.findAllByUser(user);

        // Map to DTO
        List<UserStatsDTO> dtoList = stats.stream()
                .map(stat -> new UserStatsDTO(stat.getDifficulty(), stat.getGamesPlayed(), stat.getAverageAccuracy()))
                .toList();

        return Response.ok(dtoList).build();
    }
}
