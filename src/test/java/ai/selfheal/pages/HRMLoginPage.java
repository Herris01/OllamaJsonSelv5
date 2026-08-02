package ai.selfheal.pages;

import ai.selfheal.base3.SelfHealingDriverClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HRMLoginPage {
    private final SelfHealingDriverClass selfHeal;

    public HRMLoginPage(WebDriver driver) {
        selfHeal = new SelfHealingDriverClass(driver, "hrm_loginPage.json");
    }

    public WebElement username() {
        return selfHeal.find("username");
    }

    public SelfHealingDriverClass pageDriver(){
        return selfHeal;
    }


}
