//package ai.selfheal.base;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.openqa.selenium.By;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//public final class LocatorRepository {
//  private static final ObjectMapper JSON = new ObjectMapper();
//  private LocatorFile file;
//  private final Path sourceFile;
//
//  public LocatorRepository(String resourceName) {
//    Path directory = Path.of(System.getenv().getOrDefault("LOCATOR_DIR", "src/main/resources/locators"));
//    Path candidate = directory.resolve(resourceName);
//    try {
//      if (Files.isRegularFile(candidate)) {
//        sourceFile = candidate;
//        file = JSON.readValue(Files.readString(sourceFile), LocatorFile.class);
//      } else {
//        sourceFile = null; // supports an immutable, packaged application too
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream("locators/" + resourceName)) {
//          if (input == null) throw new IllegalArgumentException("Locator JSON not found: " + resourceName);
//          file = JSON.readValue(input, LocatorFile.class);
//        }
//      }
//    } catch (IOException e) { throw new IllegalStateException("Cannot load locator JSON", e); }
//  }
//
//  public LocatorDefinition definition(String name) {
//    LocatorDefinition definition = file.locators().get(name);
//    if (definition == null) throw new IllegalArgumentException("Unknown locator '" + name + "' on " + file.page());
//    return definition;
//  }
//
//  public By by(String name) { return toBy(definition(name)); }
//  /** Persists a verified CSS repair when running from editable JSON files. */
//  public void saveHealing(String name, String cssSelector) {
//    if (sourceFile == null) return;
//    try {
//      Map<String, LocatorDefinition> updated = new LinkedHashMap<>(file.locators());
//      LocatorDefinition old = definition(name);
//      updated.put(name, new LocatorDefinition("css", cssSelector, old.description()));
//      file = new LocatorFile(file.page(), updated);
//      JSON.writerWithDefaultPrettyPrinter().writeValue(sourceFile.toFile(), file);
//    } catch (IOException e) { throw new IllegalStateException("Verified locator but could not save " + sourceFile, e); }
//  }
//  public static By toBy(LocatorDefinition d) {
//    return switch (d.strategy().toLowerCase()) {
//      case "id" -> By.id(d.value()); case "name" -> By.name(d.value()); case "css" -> By.cssSelector(d.value());
//      case "xpath" -> By.xpath(d.value()); case "class" -> By.className(d.value()); case "tag" -> By.tagName(d.value());
//      case "linktext" -> By.linkText(d.value()); default -> throw new IllegalArgumentException("Unsupported locator strategy: " + d.strategy());
//    };
//  }
//}
