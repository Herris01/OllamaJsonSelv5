//package ai.selfheal.base;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.time.Duration;
//
//public final class OllamaClient {
////  private static final ObjectMapper JSON = new ObjectMapper();
////  private final HttpClient http = HttpClient.newHttpClient();
////  private final URI endpoint;
////  private final String model;
////
//////  private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
//////  private static final String MODEL_NAME = "llama3.1:8b"; // Lightweight model for speed
//////  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
//////          .connectTimeout(Duration.ofSeconds(5))
//////          .build();
//////  private static final ObjectMapper MAPPER = new ObjectMapper();
////
////
////
////
////
////  public OllamaClient() {
////    this(
////            System.getenv().getOrDefault("OLLAMA_URL", "http://localhost:11434/api/generate"),
////            System.getenv().getOrDefault("OLLAMA_MODEL", "llama3.1:8b")
////        );
////  }
////  OllamaClient(String endpoint, String model) {
////    this.endpoint = URI.create(endpoint);
////    this.model = model;
////  }
////
////  public String suggestCss(String description, String oldSelector, String pageHtml) {
////    String prompt = "Choose the single element described as: " + description + ". The previous selector was: " + oldSelector + ". " +
////        "Return a robust CSS selector. Prefer id, data-testid, name, aria-label, or stable semantic attributes; never use :nth-child. " +
////        "The candidate elements are:\n" + pageHtml;
////    try {
////      var schema = JSON.createObjectNode().put("type", "object");
////      schema.putObject("properties").putObject("selector").put("type", "string");
////      schema.putArray("required").add("selector");
////      schema.put("additionalProperties", false);
////      String body = JSON.createObjectNode().put("model", model).put("prompt", prompt).put("stream", false).set("format", schema).toString();
////      HttpRequest request = HttpRequest.newBuilder(endpoint).timeout(Duration.ofSeconds(45)).header("Content-Type", "application/json")
////          .POST(HttpRequest.BodyPublishers.ofString(body)).build();
////      HttpResponse<String> httpResponse = http.send(request, HttpResponse.BodyHandlers.ofString());
////      if (httpResponse.statusCode() != 200) throw new IllegalStateException("Ollama returned HTTP " + httpResponse.statusCode() + ": " + httpResponse.body());
////      JsonNode response = JSON.readTree(httpResponse.body());
////      return JSON.readTree(response.path("response").asText()).path("selector").asText().trim();
////    } catch (Exception e) { throw new IllegalStateException("Ollama healing request failed. Is Ollama running and is " + model + " installed?", e); }
////  }
//
//  private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
//  private static final String MODEL_NAME = "llama3.1:8b";
//  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
//          .connectTimeout(Duration.ofSeconds(5))
//          .build();
//  private static final ObjectMapper MAPPER = new ObjectMapper();
//
//  private OllamaClient() {
//  }
//
//  public static String askForNewLocator(String keyPath, String failedLocator,
//                                        HealingStrategy strategy, String domContext) {
//    String prompt = """
//            You are a Selenium locator-healing agent.
//            Locator key: %s
//            Failed locator: %s
//            Strategy: %s
//            DOM context: %s
//
//            Return ONLY one locator line. No markdown or explanation.
//            DIRECT: id=<id-value> or name=<name-value>.
//            STANDARD: css=<selector> or xpath=<selector>.
//            COMPLEX: xpath=<selector> or
//            chain=xpath=<parent-selector> >> css=<child-selector>.
//            Never use dynamic IDs, indexes, or generated CSS classes.
//            """.formatted(keyPath, failedLocator, strategy.name(), domContext);
//
//    try {
//      String requestBody = MAPPER.writeValueAsString(Map.of(
//              "model", MODEL_NAME,
//              "prompt", prompt,
//              "stream", false
//      ));
//
//      HttpRequest request = HttpRequest.newBuilder()
//              .uri(URI.create(OLLAMA_URL))
//              .header("Content-Type", "application/json")
//              .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//              .build();
//
//      HttpResponse<String> httpResponse = HTTP_CLIENT.send(
//              request, HttpResponse.BodyHandlers.ofString());
//
//      if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
//        throw new IllegalStateException("Ollama returned HTTP "
//                + httpResponse.statusCode() + ": " + httpResponse.body());
//      }
//
//      String modelOutput = MAPPER.readTree(httpResponse.body())
//              .path("response")
//              .asText()
//              .trim();
//      if (modelOutput.isBlank()) {
//        throw new IllegalStateException("Ollama returned an empty response");
//      }
//      return sanitize(modelOutput);
//    } catch (Exception e) {
//      System.err.println("Ollama locator generation failed: " + e.getMessage());
//      return null;
//    }
//  }
//
//  private static String sanitize(String output) {
//    String fence = Character.toString((char) 96);
//    return output
//            .replace(fence, "")
//            .replaceFirst("(?i)^\\s*(selector|xpath|css)\\s*:\\s*", "")
//            .trim();
//  }
//}
//
//
//
//
//
