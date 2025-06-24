package com.guessthecry.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class HintService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    // text to display and weight of change for hint to appear
    private record Hint(String text, int weight) {}

    @Cacheable("pokemon-hints")
    public Map<String, Object> getPokemonData(String pokemonName) {
        System.out.println("Fetching data from PokeAPI for: " + pokemonName);

        try {
            String url = "https://pokeapi.co/api/v2/pokemon/" + pokemonName.toLowerCase();
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Failed to fetch from PokeAPI for " + pokemonName + ": " + e.getMessage());
            return null;
        }
    }

    public List<String> getHints(String pokemonName) {
        try {
            // get cache method and retrieve pokemondata
            Map<String, Object> data = getPokemonData(pokemonName);

            if (data == null) {
                return List.of("Hint unavailable.");
            }

            List<Hint> possibleHints = new ArrayList<>();

            // Hint 1: Types
            List<Map<String, Object>> types = (List<Map<String, Object>>) data.get("types");
            if (types != null && !types.isEmpty()) {
                List<String> typeNames = types.stream()
                        .map(t -> ((Map<String, String>) t.get("type")).get("name"))
                        .toList();
                possibleHints.add(new Hint("Type: " + String.join(" / ", typeNames), 100));
            }

            // Hint 2: Height & Weight in cm & kg
            Integer height = (Integer) data.get("height"); // decimeters
            Integer weight = (Integer) data.get("weight"); // hectograms
            if (height != null && weight != null) {
                int heightCm = height * 10;
                double weightKg = weight / 10.0;
                possibleHints.add(new Hint(String.format("Height: %d cm, Weight: %.1f kg", heightCm, weightKg), 75));
            }

            List<Map<String, Object>> abilities = (List<Map<String, Object>>) data.get("abilities");
            if (abilities != null && !abilities.isEmpty()) {
                // choose random abillity
                Map<String, Object> randomAbilityMap = abilities.get(random.nextInt(abilities.size()));
                String abilityName = ((Map<String, String>) randomAbilityMap.get("ability")).get("name");

                // format name ("solar-power" -> "Solar Power")
                String formattedAbilityName = formatName(abilityName);
                possibleHints.add(new Hint("Has the ability: " + formattedAbilityName, 80));
            }

            // other hints to add here:

            return selectWeightedRandomHints(possibleHints, 2);
        } catch (Exception e) {
            // Log de error voor debugging
            System.err.println("Error fetching hints for " + pokemonName + ": " + e.getMessage());
            return List.of("Hint unavailable.");
        }
    }

    private List<String> selectWeightedRandomHints(List<Hint> hints, int count) {
        if (hints.isEmpty()) {
            return new ArrayList<>();
        }

        // create weighted list, a hint with weight 3 gets added 3 times.
        List<String> weightedList = new ArrayList<>();
        for (Hint hint : hints) {
            for (int i = 0; i < hint.weight(); i++) {
                weightedList.add(hint.text());
            }
        }

        Collections.shuffle(weightedList);

        Set<String> selectedHints = new LinkedHashSet<>();

        // keep getting a new hint until we have the desired amount, or the list is empty
        int attempts = 0;
        while (selectedHints.size() < count && attempts < weightedList.size()) {
            selectedHints.add(weightedList.get(attempts));
            attempts++;
        }

        return new ArrayList<>(selectedHints);
    }

    // Helper format: "solar-power" -> "Solar Power"
    private String formatName(String name) {
        if (name == null || name.isEmpty()) return "";
        String[] parts = name.split("-");
        StringBuilder formatted = new StringBuilder();
        for (String part : parts) {
            formatted.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1))
                    .append(" ");
        }
        return formatted.toString().trim();
    }
}