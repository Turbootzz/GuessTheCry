package com.guessthecry.webservices.question;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/quiz")
public class QuestionResource {

    @GET
    @Path("/question")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestion() {
        String sampleQuestion = "{\"pokemonName\":\"pikachu\",\"audioUrl\":\"/cries/pikachu.ogg\"}";
        return Response.ok(sampleQuestion).build();
    }

    @GET
    @Path("/hint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHint() {
        String hint = "{\"hint\":\"Electric type, Generation 1\"}";
        return Response.ok(hint).build();
    }
}
