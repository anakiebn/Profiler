package co.za.access.Profiler.facebookSearch.service;

import co.za.access.Profiler.config.AppVariable;
import co.za.access.Profiler.config.FacebookVariable;
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

    public FacebookServiceImpl(AppVariable appVariable, FacebookVariable facebookVariable) {
        this.appVariable = appVariable;
        this.facebookVariable = facebookVariable;
    }

    private void openFacebook() {
        log.info("Loading chrome driver...");
        System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        log.info("Opening Facebook...");
        driver.get("https://www.facebook.com?locale=en");
    }

    @Override
    public final String searchPerson(String name) {
        openFacebook();
        rejectCookies();
        logIntoFacebook();
        try {
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(facebookVariable.getSearchField())));

            WebElement searchBar = driver.findElement(By.cssSelector(facebookVariable.getSearchField()));
            searchBar.sendKeys(name);
            searchBar.sendKeys(Keys.ENTER);

//            filterBy("Gauteng");
            driver.quit();
            return "found " + name;
        } catch (NoSuchElementException nsee) {
            log.error("Search bar not found! {}", nsee.getMessage());
        } catch (TimeoutException toe) {
            log.error("Timeout! Element still not found! {}", toe.getMessage());
        } catch (IllegalArgumentException iae) {
            log.error("Unexpected error", iae);
        }
        throw new RuntimeException("Error while trying to find " + name);
    }

    private void rejectCookies() {
        try {
            log.info("Rejecting cookies...");
            WebElement rejectAllBtn = driver.findElement(By.id(facebookVariable.getCookieWindow()));
            if (rejectAllBtn.isDisplayed()) {
                rejectAllBtn.click();
                log.info("Cookies rejected...");
            }
        } catch (NoSuchElementException nsee) {
            log.error("Cookies Button not found... Proceeding with operations");
        }

    }

    private void logIntoFacebook() throws NoSuchElementException, IllegalArgumentException {
        try {
            log.info("Logging into Facebook as, {} ", appVariable.getLoginEmail());
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(facebookVariable.getEmailField())));
            WebElement emailField = driver.findElement(By.id(facebookVariable.getEmailField()));
            WebElement passwordField = driver.findElement(By.id(facebookVariable.getPasswordField()));
            WebElement loginBtn = driver.findElement(By.name(facebookVariable.getLoginBtn()));

            emailField.sendKeys(appVariable.getLoginEmail());
            passwordField.sendKeys(appVariable.getLoginPassword());
            loginBtn.submit();
        } catch (NoSuchElementException nsee) {
            log.error("No such element found! Logging in failed!", nsee);
        } catch (TimeoutException toe) {
            log.error("Time out exception, check your network or element doesn't exist. Logging in failed!", toe);
        }
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
