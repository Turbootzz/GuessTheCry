package com.guessthecry.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HintService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<String> getHints(String pokemonName) {
        List<String> hints = new ArrayList<>();
        try {
            String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName.toLowerCase();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map data = response.getBody();

            // Hint 1: Type(s)
            List<Map> types = (List<Map>) data.get("types");
            List<String> typeNames = types.stream()
                    .map(t -> ((Map)t.get("type")).get("name").toString())
                    .toList();
            hints.add("Type: " + String.join(" / ", typeNames));

            // Hint 2: Height & Weight in cm & kg
            Integer height = (Integer) data.get("height"); // decimeters
            Integer weight = (Integer) data.get("weight"); // hectograms
            if (height != null && weight != null) {
                int heightCm = height * 10;
                double weightKg = weight / 10.0;
                hints.add(String.format("Height: %d cm, Weight: %.1f kg", heightCm, weightKg));
            }

            // other hints to add here:

        } catch (Exception e) {
            hints.add("Hint unavailable.");
        }

        return hints;
    }
}