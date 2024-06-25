package co.za.access.Profiler.facebookSearch.service;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class FacebookServiceImpl implements FacebookService {

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    @Value("${facebook.loginEmail}")
    private String fbLoginEmail;

    @Value("${facebook.loginPassword}")
    private String fbLoginPassword;
    private WebDriver driver;

    @Value("${facebook.personOfThatName}")
    private String  personOfThatName;

    @Value("${facebook.people}")
    private String people;

    public void openFacebook(){
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options=new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.get("https://www.facebook.com?locale=en");
    }

    @Override
    public String searchPerson(String name) {
        openFacebook();
        logIntoFacebook();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchBar = driver.findElement(By.cssSelector("input.x1i10hfl.xggy1nq.x1s07b3s"));
            searchBar.sendKeys(name);
            searchBar.sendKeys(Keys.ENTER);

            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(people)));

            //                 ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moreResults);
        }catch (NoSuchElementException nsee){
            log.error("Facebook element not found! {}",nsee.getMessage());
        }catch (TimeoutException toe){
            log.error("Timeout! Element still not found! {}",toe.getMessage());
        }
        return "logged in";
    }

    public void organizeResults(){


    }


    public void logIntoFacebook() throws NoSuchElementException, IllegalArgumentException {
            // Here, I'm rejecting cookies
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("._4t2a")));
            WebElement declineCookieBtn = driver.findElement(By.cssSelector("div[aria-label=\"Decline optional cookies\"]"));
            declineCookieBtn.click();

            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("header_block")));
            // login section
            WebElement emailField = driver.findElement(By.id("email"));
            WebElement passwordField = driver.findElement(By.id("pass"));
            WebElement loginButton = driver.findElement(By.name("login"));
            emailField.sendKeys(fbLoginEmail);
            passwordField.sendKeys(fbLoginPassword);
            loginButton.submit();

    }


    @PreDestroy
    private void cleanUp(){
//        driver.quit();
    }


}
