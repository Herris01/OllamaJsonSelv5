# Self-healing locator framework

Copy `src` into your Maven or Gradle project. These classes intentionally have no `package` declaration; add the same package statement to every Java file when placing them in your project.

Dependencies: Selenium Java, Jackson Databind, Allure Java Commons, and the Selenium support module that contains `ByChained`.

Locator keys use `page.element`, for example `loginPage.username`. Each page has a separate file under `src/test/resources/locators`.

Ollama must be running locally with the configured model installed. The model receives one strategy at a time and may return only a locator type permitted by that stage.
