package co.za.access.profiler.dataCollection.facebookSearch.service;

import co.za.access.Profiler.config.FacebookVariable;
import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.util.Interact;
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
public class FacebookServiceImpl implements FacebookService {

    private WebDriver driver;
    private WebDriverWait wait;

    private final AppVariable appVariable;
    private final FacebookVariable facebookVariable;

    private Interact interact;

    public FacebookServiceImpl(AppVariable appVariable, FacebookVariable facebookVariable) {
        this.appVariable = appVariable;
        this.facebookVariable = facebookVariable;
    }

    private void openFacebook() {
        log.info("Loading chrome driver...");
        System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
        driver = new ChromeDriver(Interact.options());
      int TIMEOUT = 30;
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        log.info("Opening Facebook...");
        driver.get("https://www.facebook.com?locale=en");
        interact=new Interact(driver,wait);
    }

    @Override
    public final String searchPerson(String name) {
        openFacebook();
        interact.clickBtn(By.id(facebookVariable.getCookieWindow()),false,"cookie"); // rejecting cookies
        logIntoFacebook();
        interact.sendInput(By.cssSelector(facebookVariable.getSearchField()),name,"search",false,true);

            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(facebookVariable.getSearchField())));
        try {
            Thread.sleep(Duration.ofSeconds(6).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if(driver!=null){
                driver.quit();
            }
        }
        return "Search complete";
    }

    private void logIntoFacebook(){
            log.info("Logging into Facebook as, {} ", appVariable.getLoginEmail());
            interact.sendInput(By.id(facebookVariable.getEmailField()),appVariable.getLoginEmail(),"email",false,false); // insert email
            interact.sendInput(By.id(facebookVariable.getPasswordField()),appVariable.getLoginPassword(),"password",false,false); // insert password
            interact.clickBtn(By.name(facebookVariable.getLoginBtn()),true,"login"); // click login button
    }

    private void filterBy(String by) {
        log.info("Filtering by people...");
        try {
            // Log the page source for debugging
            String pageSource = driver.getPageSource();
            log.debug("Page Source: " + pageSource);

            WebElement peopleBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(facebookVariable.getFilterPeopleBtn())));
            if (peopleBtn.isDisplayed()) {
                peopleBtn.click();
            } else {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", peopleBtn);
            }
        } catch (NoSuchElementException nsee) {
            log.error("No such element found!", nsee);
        } catch (TimeoutException toe) {
            log.error("Time out exception, check your network or people button not found!", toe);
        }
    }




}
