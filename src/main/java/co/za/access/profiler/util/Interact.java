package co.za.access.profiler.util;

import co.za.access.profiler.config.CookieData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.sql.Date;
import java.time.Duration;

@Slf4j
public final class Interact {

    private final WebDriver driver;
    private WebDriverWait wait;
    public Interact(WebDriver driver,WebDriverWait wait){
        this.driver=driver;
        this.wait=wait;
    }
    public static ChromeOptions options(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
//        options.addArguments("--incognito");
//        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--window-size=1920,1080");
        return options;
    }

    /**
     * @param by          - This is the locator for the button.
     * @param submittable - Checks if a button submits a form, or needs to be clicked.
     * @param btnName     - This is the buttons name.
     */
    public void clickBtn(By by, boolean submittable, String btnName) {
        try {
            log.info("Locating {}", btnName);
            WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            if (button.isDisplayed()) {
                log.info("Found {} button", btnName);
                if (submittable) {
                    button.submit();
                } else {
                    button.click();
                }
            }

        } catch (NoSuchElementException nsee) {
            log.error("Button {} not found...{}", btnName, nsee.getMessage());
        } catch (TimeoutException toe) {
            log.error("Timeout, failed to load {} button..\n", btnName);
        } catch (java.lang.IllegalArgumentException iae) {
            log.error("Invalid argument for button {} not found...\n", btnName);
        } catch (Exception e) {
            log.error("Error occurred while {} clicking button", btnName);
        }

    }

    /**
     * This method handles all possible input fields
     *
     * @param locator   Used to locate the input field
     * @param input     This is the data you want to send
     * @param fieldName The name of the input field
     */

    public void sendInput(By locator, String input, String fieldName, boolean submittable, boolean enterKeyNeeded) {

        try {
            log.info("Accessing the " + fieldName + " field...");
            WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            inputField.sendKeys(input);
            if (submittable) {
                inputField.submit();
            } else if (enterKeyNeeded) {
                inputField.sendKeys(Keys.ENTER);
            }
            log.info("Input sent to {} field", fieldName);
        } catch (NoSuchElementException nsee) {
            log.error("{} field not found...", nsee.getMessage());
        } catch (TimeoutException toe) {
            log.error("Timeout! Failed to load, {} field not found...\n {}", fieldName, toe.getMessage());
        } catch (IllegalArgumentException iae) {
            log.error("Invalid argument on {} field not found...\n {}", fieldName, iae.getMessage());
        }
    }

    public Cookie addCookie(CookieData cookieData){
        return new Cookie.Builder(cookieData.getName(),cookieData.getValue())
                .domain(cookieData.getDomain())
                .path(cookieData.getPath())
                .sameSite(cookieData.getSameSite())
                .isHttpOnly(cookieData.isHttpOnly())
                .expiresOn(cookieData.getExpiry())
                .isSecure(cookieData.isSecure())
                .build()
                ;

    }


}


