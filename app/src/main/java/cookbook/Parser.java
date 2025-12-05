package cookbook;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import cookbook.recipe.Recipe;

public class Parser {
    private static final String URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String REQUEST_STRING = """
{
    "messages": [
        {
            "role": "system",
            "content": ${SYSTEM_PROMPT}
        },
        {
            "role": "user",
            "content": ${USER_PROMPT}
        }
    ],
    "model": "openai/gpt-oss-20b",
    "temperature": 1,
    "max_completion_tokens": 8192,
    "top_p": 1,
    "stream": false,
    "reasoning_effort": "medium",
    "stop": null
}
""";

    private static final String SYSTEM_PROMPT = """
You are a smart cookbook.
The user will input the available ingredients in their house, and you will return a JSON array containing three recipes based on available ingredients.

Each recipe object must have this exact structure:

```json
[
  {
    "name": "Example name",
    "duration": 30,
    "ingredients": [
        {
            "name": "Example ingredient",
            "available": true,
            "unit": "g",
            "quantity": 300
        },
        {
            "name": "Example liquid ingredient (like oil)",
            "available": false,
            "unit": "ml",
            "quantity": 200
        },
        {
            "name": "Example small ingredient (like salt)",
            "available": true,
            "unit": " pinch",
            "quantity": 1
        },
        {
            "name": "Example tablespoon ingredient",
            "available": false,
            "unit": " tablespoons",
            "quantity": 2
        }
    ],
    "steps": [
      "Do something",
      "Do something else after"
    ]
  }
]
```

The maximum duration for a recipe must be 30 minutes.
You are allowed to suggest recipes that require other ingredients, as well as recipes that do not use all the available ones.
If the user tries says only absurd ingredients (metal pipes, poo or the andromeda galaxy), you are allowed to ignore the user input and suggest other recipes without available ingredients (mark them as not available). However, if the user says at least one plausible ingredient, use it.
Respond with only valid JSON. Do not include explanations, notes, or Markdown fences. Do not abbreviate measures like tablespoon to tbsp, only units like grams to g.
""";

    public static List<Recipe> getRecipes(String ingredients) {
        String requestBody = sendRequest(ingredients);

        if (requestBody == null)
            throw new RuntimeException("HTTP request returned null body.");

        return parseResponse(requestBody);
    }

    private static String buildPayload(String userPrompt) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String escapedSystem = mapper.writeValueAsString(SYSTEM_PROMPT);
            String escapedUser = mapper.writeValueAsString(userPrompt);

            return REQUEST_STRING
                    .replace("${SYSTEM_PROMPT}", escapedSystem)
                    .replace("${USER_PROMPT}", escapedUser);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to build JSON payload", e);
        }
    }

    private static String sendRequest(String data) {
        try {
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + AuthorizationHandler.getAuthorization())
                    .POST(HttpRequest.BodyPublishers.ofString(buildPayload(data)))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.out.println("Groq API error " + response.statusCode());
                System.out.println(response.body());
                return null;
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            System.out.println("Error during HTTP request: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static List<Recipe> parseResponse(String body) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(body);

            String content = root.path("choices").get(0)
                                 .path("message").path("content").asText();

            if (content == null || content.isEmpty())
                throw new RuntimeException("Model returned empty content.");

            return mapper.readValue(
                content,
                mapper.getTypeFactory().constructCollectionType(List.class, Recipe.class)
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse model JSON output", e);
        }
    }
}