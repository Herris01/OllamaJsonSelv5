//package ai.selfheal.base2;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import org.openqa.selenium.By;
//import org.openqa.selenium.support.pagefactory.ByChained;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.util.ArrayList;
//import java.util.List;
//
///** Reads and updates one JSON locator file per page. */
//public final class LocatorRepositoryClass {
//
//    private static final Path LOCATORS_DIR = Path.of("src", "test", "resources", "locators");
//    private static final ObjectMapper MAPPER = new ObjectMapper()
//            .enable(SerializationFeature.INDENT_OUTPUT);
//
//    private LocatorRepositoryClass() {
//    }
//
//    public static synchronized By getLocator(String keyPath) {
//        LocatorAddress address = splitKey(keyPath);
//        try {
//            JsonNode locatorNode = readPage(address.page()).path(address.element());
//            if (locatorNode.isMissingNode()) {
//                throw new IllegalArgumentException("Locator not found: " + keyPath);
//            }
//            return toBy(locatorNode);
//        } catch (IOException e) {
//            throw new RuntimeException("Unable to load locator: " + keyPath, e);
//        }
//    }
//
//    public static synchronized void updateLocator(String keyPath, LocatorSpec spec) {
//        LocatorAddress address = splitKey(keyPath);
//        try {
//            ObjectNode page = (ObjectNode) readPage(address.page());
//            page.set(address.element(), MAPPER.valueToTree(spec));
//
//            Path target = pageFile(address.page());
//            Path temporary = Files.createTempFile(LOCATORS_DIR, address.page() + "-", ".tmp");
//            MAPPER.writeValue(temporary.toFile(), page);
//            try {
//                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING,
//                        StandardCopyOption.ATOMIC_MOVE);
//            } catch (IOException atomicMoveNotSupported) {
//                Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
//            }
//            System.out.println("Persisted healed locator: " + keyPath);
//        } catch (IOException e) {
//            throw new RuntimeException("Unable to persist healed locator: " + keyPath, e);
//        }
//    }
//
//    public static By buildBy(String type, String value) {
//        return switch (type.toLowerCase()) {
//            case "id" -> By.id(value);
//            case "name" -> By.name(value);
//            case "css" -> By.cssSelector(value);
//            case "xpath" -> By.xpath(value);
//            case "class" -> By.className(value);
//            case "tag" -> By.tagName(value);
//            case "linktext" -> By.linkText(value);
//            default -> throw new IllegalArgumentException("Unsupported locator type: " + type);
//        };
//    }
//
//    private static By toBy(JsonNode locatorNode) {
//        String type = locatorNode.path("type").asText().toLowerCase();
//        if (!"chained".equals(type)) {
//            return buildBy(type, locatorNode.path("value").asText());
//        }
//
//        List<By> chain = new ArrayList<>();
//        for (JsonNode step : locatorNode.path("chain")) {
//            chain.add(buildBy(step.path("type").asText(), step.path("value").asText()));
//        }
//        if (chain.isEmpty()) {
//            throw new IllegalArgumentException("Chained locator must contain at least one step");
//        }
//        return new ByChained(chain.toArray(By[]::new));
//    }
//
//    private static JsonNode readPage(String page) throws IOException {
//        Path file = pageFile(page);
//        if (!Files.exists(file)) {
//            throw new IllegalArgumentException("Page locator file not found: " + file);
//        }
//        return MAPPER.readTree(file.toFile());
//    }
//
//    private static Path pageFile(String page) {
//        return LOCATORS_DIR.resolve(page + ".json");
//    }
//
//    private static LocatorAddress splitKey(String keyPath) {
//        String[] parts = keyPath.split("\\.", 2);
//        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
//            throw new IllegalArgumentException("Key must use page.element format: " + keyPath);
//        }
//        return new LocatorAddress(parts[0], parts[1]);
//    }
//
//    private record LocatorAddress(String page, String element) {
//    }
//}
