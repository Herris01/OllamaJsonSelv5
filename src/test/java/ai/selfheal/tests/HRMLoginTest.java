package ai.selfheal.tests;


import ai.selfheal.pages.AutoExerciseLoginPage;
import ai.selfheal.pages.HRMLoginPage;
import ai.selfheal.pages.ProtonLoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class HRMLoginTest {

    private WebDriver driver;
    //private SelfHealingDriver page;
    private HRMLoginPage HRMLoginPage;
    private AutoExerciseLoginPage autoexeLoginPage;
    ProtonLoginPage protonLoginPage;

    @BeforeMethod()
    void start() {
        driver = new ChromeDriver();
//        page = new SelfHealingDriver(driver, "E:\\Java-Projects\\AiAutomation\\wip-java\\OllamaJsonSelv5\\src\\test\\resources/login_page.json");
        HRMLoginPage = new HRMLoginPage(driver);
        autoexeLoginPage = new AutoExerciseLoginPage(driver);
        protonLoginPage= new ProtonLoginPage(driver);

    }

    @AfterMethod()
    void stop(ITestResult result) {
        //driver.quit();
        long executionTime = result.getEndMillis() - result.getStartMillis();
        // Returns 1
        long minutes = TimeUnit.MILLISECONDS.toMinutes(executionTime);
        System.out.println("Execution Time for " + result.getName() + ": " + minutes + " min");
    }

//    @Test()
//    void HRMsignsInTest() {
//        driver.get("https://opensource-demo.orangehrmlive.com/");
//        HRMLoginPage.username().sendKeys("admin");
//        HRMLoginPage.pageDriver().findElement("password").sendKeys("admin123");
////        page.find("submit").click();
//    }
//
//
//    @Test()
//    void AutoExecriseSingUpTest() {
//        driver.get("https://automationexercise.com/login");
//        autoexeLoginPage.username().sendKeys("admin");
//        autoexeLoginPage.pageDriver().findElement("password").sendKeys("admin123");
////        page.find("submit").click();
//    }

    @Test()
    void ProtonSingInTest() {
        driver.get("https://account.proton.me/mail");
        protonLoginPage.username().sendKeys("admin");
        protonLoginPage.pageDriver().find("password").sendKeys("admin123");
        protonLoginPage.pageDriver().find("loginBtn").click();


// Total time:   24.5 sec [no healing no time change]
// Total time:  01:17 min [with 1 healing no time change]
// Total time:  54.261 s  [with 1 heal when heal time decreased 4 secs]
// Total time:  21.118 s  [with 1 heal when heal time decreased to 5 secs]


    }




}