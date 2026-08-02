package ai.selfheal.pages;

import ai.selfheal.base3.SelfHealingDriverClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProtonLoginPage {

    private final SelfHealingDriverClass selfHeal;

    public ProtonLoginPage (WebDriver driver) {
        selfHeal = new SelfHealingDriverClass(driver, "proton_loginpage.json");
    }

    public WebElement username() {
        return selfHeal.find("username");
    }

    public SelfHealingDriverClass pageDriver(){
        return selfHeal;
    }


}



