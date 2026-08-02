package ai.selfheal.base3;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page-scoped Selenium wrapper.
 * Example: new SelfHealingDriverClass(driver, "login_page.json").findElement("username");
 */
public class SelfHealingDriverClass {
    private static final Duration TIMEOUT = Duration.ofSeconds(4);
    private static final Duration HEAL_VERIFY_TIMEOUT = Duration.ofSeconds(6);
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String pageFileName;

    public SelfHealingDriverClass(WebDriver driver, String pageFileName) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
        this.pageFileName = pageFileName;
    }


/*    //// heal broken locator
    public WebElement findElement(String locatorKey) {
        By savedLocator = LocatorRepositoryClass.getLocator(pageFileName, locatorKey);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(savedLocator));
        } catch (TimeoutException | NoSuchElementException originalFailure) {
            String reference = pageFileName + "." + locatorKey;
            String message = "Broken locator for [" + reference + "]: " + savedLocator;
            System.err.println(message);
            Allure.addAttachment("Failed Locator", "text/plain", message);

            for (HealingStrategy strategy : HealingStrategy.values()) {
                String domContext = DomOptimizerClass.getDomForStrategy(driver, strategy);
                if (domContext.isBlank() || "[]".equals(domContext)) continue;

                String aiOutput = OllamaClientClass.askForNewLocator(
                        reference, savedLocator.toString(), strategy, domContext);
                LocatorSpec candidate = parseCandidate(aiOutput);
                if (!isAllowedForStage(candidate, strategy)) {
                    System.err.println("Ignored invalid " + strategy + " output: " + aiOutput);
                    continue;
                }

                try {
                    By healedLocator = toBy(candidate);
                    WebElement element = new WebDriverWait(driver, HEAL_VERIFY_TIMEOUT)
                            .until(ExpectedConditions.presenceOfElementLocated(healedLocator));
                    LocatorRepositoryClass.updateLocator(pageFileName, locatorKey, candidate);
                    System.out.println("Self-healed " + reference + " using " + strategy);
                    return element;
                } catch (Exception verificationFailure) {
                    System.err.println(strategy + " candidate failed verification: " + candidate);
                }
            }
            throw new NoSuchElementException("Self-healing failed for: " + reference, originalFailure);
        }
    }

    public WebDriver getWrappedDriver() { return driver; }

    private boolean isAllowedForStage(LocatorSpec candidate, HealingStrategy strategy) {
        if (candidate == null || candidate.type() == null) return false;
        return switch (strategy) {
            case DIRECT -> "id".equals(candidate.type()) || "name".equals(candidate.type());
            case STANDARD -> "css".equals(candidate.type()) || "xpath".equals(candidate.type());
            case COMPLEX -> "xpath".equals(candidate.type()) || "chained".equals(candidate.type());
        };
    }

    private By toBy(LocatorSpec spec) {
        if (!"chained".equals(spec.type())) return LocatorRepositoryClass.buildBy(spec.type(), spec.value());
        if (spec.chain() == null || spec.chain().isEmpty()) {
            throw new IllegalArgumentException("A chained locator requires one or more steps");
        }
        List<By> chain = spec.chain().stream()
                .map(step -> LocatorRepositoryClass.buildBy(step.type(), step.value())).toList();
        return new ByChained(chain.toArray(By[]::new));
    }

    private LocatorSpec parseCandidate(String output) {
        if (output == null || output.isBlank()) return null;
        String selector = output.trim();
        String lower = selector.toLowerCase();
        if (lower.startsWith("id=")) return new LocatorSpec("id", selector.substring(3).trim(), null);
        if (lower.startsWith("name=")) return new LocatorSpec("name", selector.substring(5).trim(), null);
        if (lower.startsWith("css=")) return new LocatorSpec("css", selector.substring(4).trim(), null);
        if (lower.startsWith("xpath=")) return new LocatorSpec("xpath", selector.substring(6).trim(), null);
        if (lower.startsWith("chain=")) {
            String[] steps = selector.substring(6).trim().split("\\s*>>\\s*");
            List<LocatorSpec> chain = new java.util.ArrayList<>();
            for (String step : steps) {
                LocatorSpec parsedStep = parseCandidate(step);
                if (parsedStep == null || "chained".equals(parsedStep.type())) return null;
                chain.add(parsedStep);
            }
            return chain.isEmpty() ? null : new LocatorSpec("chained", null, chain);
        }
        if (selector.startsWith("//") || selector.startsWith("./") || selector.startsWith("(")) {
            return new LocatorSpec("xpath", selector, null);
        }
        return new LocatorSpec("css", selector, null);
    }
}
*/

//    public WebElement findElement(String locatorKey) {
//        By savedLocator = LocatorRepositoryClass.getLocator(pageFileName, locatorKey);
//        try {
//            return wait.until(ExpectedConditions.presenceOfElementLocated(savedLocator));
//        } catch (TimeoutException | NoSuchElementException | InvalidSelectorException originalFailure) {
//            String reference = pageFileName + "." + locatorKey;
//            String reason = originalFailure instanceof InvalidSelectorException
//                    ? "Invalid selector"
//                    : "Broken locator";
//            String message = reason + " for [" + reference + "]: " + savedLocator
//                    + System.lineSeparator() + originalFailure.getMessage();
//            System.err.println(message);
//            Allure.addAttachment("Failed Locator", "text/plain", message);
//
//            for (HealingStrategy strategy : HealingStrategy.values()) {
//                String domContext = DomOptimizerClass.getDomForStrategy(driver, strategy);
//                if (domContext.isBlank() || "[]".equals(domContext)) continue;
//
//                String aiOutput = OllamaClientClass.askForNewLocator(
//                        reference, savedLocator.toString(), strategy, domContext);
//                LocatorSpec candidate = parseCandidate(aiOutput);
//                if (!isAllowedForStage(candidate, strategy)) {
//                    System.err.println("Ignored invalid " + strategy + " output: " + aiOutput);
//                    continue;
//                }
//
//                try {
//                    By healedLocator = toBy(candidate);
//                    WebElement element = new WebDriverWait(driver, HEAL_VERIFY_TIMEOUT)
//                            .until(ExpectedConditions.presenceOfElementLocated(healedLocator));
//                    LocatorRepositoryClass.updateLocator(pageFileName, locatorKey, candidate);
//                    System.out.println("Self-healed " + reference + " using " + strategy);
//                    return element;
//                } catch (Exception verificationFailure) {
//                    System.err.println(strategy + " candidate failed verification: " + candidate);
//                }
//            }
//            throw new NoSuchElementException("Self-healing failed for: " + reference, originalFailure);
//        }
//    }

    public WebElement find(String locatorKey) {
        By savedLocator = LocatorRepositoryClass.getLocator(pageFileName, locatorKey);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(savedLocator));
        } catch (TimeoutException | NoSuchElementException | InvalidSelectorException originalFailure) {
            String reference = pageFileName + "." + locatorKey;
            String reason = originalFailure instanceof InvalidSelectorException
                    ? "Invalid selector"
                    : "Broken locator";
            String message = reason + " for [" + reference + "]: " + savedLocator
                    + System.lineSeparator() + originalFailure.getMessage();
            System.err.println(message);
            Allure.addAttachment("Failed Locator", "text/plain", message);

            for (HealingStrategy strategy : HealingStrategy.values()) {
                String domContext = DomOptimizerClass.getDomForStrategy(driver, strategy);
                if (domContext.isBlank() || "[]".equals(domContext)) continue;

                String aiOutput = OllamaClientClass.askForNewLocator(
                        reference, savedLocator.toString(), strategy, domContext);
                LocatorSpec candidate = parseCandidate(aiOutput);
                if (!isAllowedForStage(candidate, strategy)) {
                    System.err.println("Ignored invalid " + strategy + " output: " + aiOutput);
                    continue;
                }

                try {
                    By healedLocator = toBy(candidate);
                    WebElement element = new WebDriverWait(driver, HEAL_VERIFY_TIMEOUT)
                            .until(ExpectedConditions.presenceOfElementLocated(healedLocator));
                    LocatorRepositoryClass.updateLocator(pageFileName, locatorKey, candidate);
                    System.out.println("Self-healed " + reference + " using " + strategy);
                    return element;
                } catch (Exception verificationFailure) {
                    System.err.println(strategy + " candidate failed verification: " + candidate);
                }
            }
            throw new NoSuchElementException("Self-healing failed for: " + reference, originalFailure);
        }
    }









    public WebDriver getWrappedDriver() { return driver; }

    private boolean isAllowedForStage(LocatorSpec candidate, HealingStrategy strategy) {
        if (candidate == null || candidate.type() == null) return false;
        return switch (strategy) {
            case DIRECT -> "id".equals(candidate.type()) || "name".equals(candidate.type());
            case STANDARD -> "css".equals(candidate.type()) || "xpath".equals(candidate.type());
            case COMPLEX -> "xpath".equals(candidate.type()) || "chained".equals(candidate.type());
        };
    }

    private By toBy(LocatorSpec spec) {
        if (!"chained".equals(spec.type())) return LocatorRepositoryClass.buildBy(spec.type(), spec.value());
        if (spec.chain() == null || spec.chain().isEmpty()) {
            throw new IllegalArgumentException("A chained locator requires one or more steps");
        }
        List<By> chain = spec.chain().stream()
                .map(step -> LocatorRepositoryClass.buildBy(step.type(), step.value())).toList();
        return new ByChained(chain.toArray(By[]::new));
    }

    private LocatorSpec parseCandidate(String output) {
        if (output == null || output.isBlank()) return null;
        String selector = output.trim();
        String lower = selector.toLowerCase();
        if (lower.startsWith("id=")) return new LocatorSpec("id", selector.substring(3).trim(), null);
        if (lower.startsWith("name=")) return new LocatorSpec("name", selector.substring(5).trim(), null);
        if (lower.startsWith("css=")) return new LocatorSpec("css", selector.substring(4).trim(), null);
        if (lower.startsWith("xpath=")) return new LocatorSpec("xpath", selector.substring(6).trim(), null);
        if (lower.startsWith("chain=")) {
            String[] steps = selector.substring(6).trim().split("\\s*>>\\s*");
            List<LocatorSpec> chain = new java.util.ArrayList<>();
            for (String step : steps) {
                LocatorSpec parsedStep = parseCandidate(step);
                if (parsedStep == null || "chained".equals(parsedStep.type())) return null;
                chain.add(parsedStep);
            }
            return chain.isEmpty() ? null : new LocatorSpec("chained", null, chain);
        }
        if (selector.startsWith("//") || selector.startsWith("./") || selector.startsWith("(")) {
            return new LocatorSpec("xpath", selector, null);
        }
        return new LocatorSpec("css", selector, null);
    }
}







