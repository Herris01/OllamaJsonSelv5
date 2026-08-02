package ai.selfheal.base3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/** Loads one locator JSON file selected by the active page object. */
public final class LocatorRepositoryClass {
    private static final Path LOCATORS_DIR = Path.of("src", "test", "resources", "locators");
    private static final ObjectMapper MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    private LocatorRepositoryClass() {}

    public static synchronized By getLocator(String pageFileName, String locatorKey) {
        try {
            JsonNode locatorNode = readPage(pageFileName).path(locatorKey);
            if (locatorNode.isMissingNode()) {
                throw new IllegalArgumentException("Locator '" + locatorKey + "' not found in " + pageFileName);
            }
            return toBy(locatorNode);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load page locators: " + pageFileName, e);
        }
    }

    public static synchronized void updateLocator(String pageFileName, String locatorKey, LocatorSpec spec) {
        try {
            ObjectNode page = (ObjectNode) readPage(pageFileName);
            page.set(locatorKey, MAPPER.valueToTree(spec));
            Path target = pageFile(pageFileName);
            Path temporary = Files.createTempFile(LOCATORS_DIR, "healed-", ".tmp");
            MAPPER.writeValue(temporary.toFile(), page);
            try {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException atomicMoveNotSupported) {
                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            }
            System.out.println("Persisted healed locator '" + locatorKey + "' in " + pageFileName);
        } catch (IOException e) {
            throw new RuntimeException("Unable to persist healed locator in " + pageFileName, e);
        }
    }

    public static By buildBy(String type, String value) {
        return switch (type.toLowerCase()) {
            case "id" -> By.id(value);
            case "name" -> By.name(value);
            case "css" -> By.cssSelector(value);
            case "xpath" -> By.xpath(value);
            case "class" -> By.className(value);
            case "tag" -> By.tagName(value);
            case "linktext" -> By.linkText(value);
            default -> throw new IllegalArgumentException("Unsupported locator type: " + type);
        };
    }

    private static JsonNode readPage(String pageFileName) throws IOException {
        Path file = pageFile(pageFileName);
        if (!Files.exists(file)) throw new IllegalArgumentException("Page locator file not found: " + file);
        return MAPPER.readTree(file.toFile());
    }

    private static Path pageFile(String pageFileName) {
        if (!pageFileName.matches("[A-Za-z0-9_-]+\\.json")) {
            throw new IllegalArgumentException("Page file name must look like login_page.json: " + pageFileName);
        }
        return LOCATORS_DIR.resolve(pageFileName).normalize();
    }

    private static By toBy(JsonNode locatorNode) {
        String type = locatorNode.path("type").asText().toLowerCase();
        if (!"chained".equals(type)) return buildBy(type, locatorNode.path("value").asText());
        List<By> chain = new ArrayList<>();
        for (JsonNode step : locatorNode.path("chain")) {
            chain.add(buildBy(step.path("type").asText(), step.path("value").asText()));
        }
        if (chain.isEmpty()) throw new IllegalArgumentException("Chained locator must contain at least one step");
        return new ByChained(chain.toArray(By[]::new));
    }
}
