package ai.selfheal.base3;

import java.util.List;

/** JSON shape persisted in a page locator file and returned by Ollama. */
public record LocatorSpec(String type, String value, List<LocatorSpec> chain) {
}
