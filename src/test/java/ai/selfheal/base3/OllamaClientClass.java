package ai.selfheal.base3;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/** Makes the Ollama request; Java parses the returned selector text. */
public final class OllamaClientClass {

    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String MODEL_NAME = "llama3.1:8b";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private OllamaClientClass() {
    }

    public static String askForNewLocator(String keyPath, String failedLocator,
                                          HealingStrategy strategy, String domContext) {
        String prompt = """
            You are a Selenium locator-healing agent.
            Locator key: %s
            Failed locator: %s
            Strategy: %s
            DOM context: %s

            Return ONLY one locator line. No markdown or explanation.
            DIRECT: id=<id-value> or name=<name-value>.
            STANDARD: css=<selector> or xpath=<selector>.
            COMPLEX: xpath=<selector> or
            chain=xpath=<parent-selector> >> css=<child-selector>.
            Never use dynamic IDs, indexes, or generated CSS classes.
            """.formatted(keyPath, failedLocator, strategy.name(), domContext);

        try {
            String requestBody = MAPPER.writeValueAsString(Map.of(
                    "model", MODEL_NAME,
                    "prompt", prompt,
                    "stream", false
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> httpResponse = HTTP_CLIENT.send(
                    request, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
                throw new IllegalStateException("Ollama returned HTTP "
                        + httpResponse.statusCode() + ": " + httpResponse.body());
            }

            String modelOutput = MAPPER.readTree(httpResponse.body())
                    .path("response")
                    .asText()
                    .trim();
            if (modelOutput.isBlank()) {
                throw new IllegalStateException("Ollama returned an empty response");
            }
            return sanitize(modelOutput);
        } catch (Exception e) {
            System.err.println("Ollama locator generation failed: " + e.getMessage());
            return null;
        }
    }

    private static String sanitize(String output) {
        String fence = Character.toString((char) 96);
        return output
                .replace(fence, "")
                .replaceFirst("(?i)^\\s*(selector|xpath|css)\\s*:\\s*", "")
                .trim();
    }
}
