package co.za.access.Profiler.linkedinSearch.service;

import co.za.access.Profiler.config.AppVariable;
import co.za.access.Profiler.linkedinSearch.config.LinkedinVariable;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class LinkedinServiceImpl implements LinkedinService {


    private final LinkedinVariable linkedinVariable;
    private final AppVariable appVariable;
    private WebDriver driver;
    private WebDriverWait wait;

    public LinkedinServiceImpl(LinkedinVariable linkedinVariable, AppVariable appVariable) {
        this.linkedinVariable = linkedinVariable;
        this.appVariable = appVariable;
    }


    @Override
    public void openLinkedin(String name) {
        log.info("Loading chrome driver...");
        System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.get("https://www.linkedin.com/login?locale=en");
        login();
        search(name);
    }



    /**
     * This method performs login operations.
     *
     */
    @Override
    public void login() {
        sendInput(By.id(linkedinVariable.getEmailField()),appVariable.getLoginEmail(),"email",false);
        sendInput(By.id(linkedinVariable.getPasswordField()),appVariable.getLoginPassword(),"password",false);
        clickBtn(By.cssSelector(linkedinVariable.getSignIn()),true,"","login");
    }

    /**
     *
     * @param by This is the locator for the button.
     * @param submittable Checks if a button submits a form, or needs to be clicked.
     * @param msg   This is a log message that captures what the button is doing.
     * @param btnName This is the buttons name.
     */
    private void clickBtn(By by,boolean submittable,String msg,String btnName){

        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(by));
            if (submittable) {
                button.submit();
            } else {
                button.click();
            }
        }catch (NoSuchElementException nsee){
            log.error("Button {} not found...{}",btnName,nsee.getMessage());
        }catch (TimeoutException toe){
            log.error("Failed to load {} button..\n {}",btnName,toe.getMessage());
        }catch (java.lang.IllegalArgumentException iae){
            log.error("Invalid argument for button {} not found...\n {}",btnName,iae.getMessage());
        }

    }

    /**
     * This method handles all possible input fields
     *
     * @param locator Used to locate the input field
     * @param input This is the data you want to send
     * @param fieldName The name of the input field
     */

    private void sendInput(By locator,String input,String fieldName,boolean submittable){

        try {
            log.info("Accessing the "+fieldName+ " field...");
            WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            inputField.sendKeys(input);
            if(submittable){
                inputField.submit();
            }
            log.info("Input sent to {} field",fieldName);
        }catch (NoSuchElementException nsee){
            log.error("{} field not found...",nsee.getMessage());
        }catch (TimeoutException toe){
            log.error("Timeout! Failed to load, {} field not found...\n {}",fieldName,toe.getMessage());
        }catch (IllegalArgumentException iae){
            log.error("Invalid argument on {} field not found...\n {}",fieldName,iae.getMessage());
        }
    }
    public void search(String name){
        sendInput(By.id(linkedinVariable.getSearchField()),name,"search",true);
    }

    @Override
    public void logout() {

    }


}

