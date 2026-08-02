//package ai.selfheal.base2;
//
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//
///** Supplies only the DOM details permitted for the current healing stage. */
//public final class DomOptimizerClass {
//
//    private static final String EXTRACT_DOM = """
//        return (function(strategy) {
//          const visible = el => !!(el.offsetWidth || el.offsetHeight || el.getClientRects().length);
//          const text = el => (el.innerText || el.getAttribute('aria-label') || el.placeholder || '')
//              .replace(/\\s+/g, ' ').trim();
//          const elements = [...document.querySelectorAll(
//              'input,select,textarea,button,a,[role="button"],[role="textbox"],[role="checkbox"]'
//          )].filter(visible);
//
//          if (strategy === 'DIRECT') {
//            return elements.filter(el => el.id || el.name).map(el => ({
//              tag: el.tagName.toLowerCase(), id: el.id || null, name: el.name || null,
//              label: el.labels?.[0]?.innerText?.trim() || null, text: text(el)
//            }));
//          }
//
//          if (strategy === 'STANDARD') {
//            return elements.map(el => ({
//              tag: el.tagName.toLowerCase(), testId: el.getAttribute('data-testid'),
//              role: el.getAttribute('role'), type: el.type || null,
//              placeholder: el.placeholder || null, ariaLabel: el.getAttribute('aria-label'),
//              text: text(el)
//            }));
//          }
//
//          return elements.map(el => {
//            const container = el.closest('.oxd-input-group,[class*="field"],[class*="group"],[class*="wrapper"],form > div');
//            const label = container?.querySelector('label')?.innerText?.trim()
//                || el.labels?.[0]?.innerText?.trim() || null;
//            return {
//              tag: el.tagName.toLowerCase(), label: label, text: text(el),
//              parentClass: container?.className || null
//            };
//          });
//        })(arguments[0]);
//        """;
//
//    private DomOptimizerClass() {
//    }
//
//    public static String getDomForStrategy(WebDriver driver, HealingStrategy strategy) {
//        try {
//            Object result = ((JavascriptExecutor) driver).executeScript(EXTRACT_DOM, strategy.name());
//            return result == null ? "" : result.toString();
//        } catch (Exception e) {
//            System.err.println("DOM extraction failed: " + e.getMessage());
//            return "";
//        }
//    }
//}
