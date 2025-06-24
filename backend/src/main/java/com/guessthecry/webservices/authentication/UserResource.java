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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Path("/auth")
public class UserResource {
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserStatsRepository statsRepository;
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private JwtUtil jwtUtil;
    @Inject
    private AuthenticationManager authenticationManager;

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

        try {
            // let string boot validate authentication
            // this will call CustomUserDetailsService and PasswordEncoder
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDto.getUsername(),
                            userDto.getPassword()
                    )
            );

            // if authentication() doesnt give an exception we can continue and safely generate token for user
            User user = userRepository.findByUsername(userDto.getUsername())
                    .orElseThrow(() -> new IllegalStateException("User not found after successful authentication"));

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            UserDTO userResponseDto = new UserDTO(user.getId(), user.getUsername());
            LoginResponseDTO response = new LoginResponseDTO(token, userResponseDto);

            return Response.ok(response).build();

        } catch (AuthenticationException e) {
            // if authentication fails due to user input
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid username or password").build();
        }
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
