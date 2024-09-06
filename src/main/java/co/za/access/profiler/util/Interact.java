package co.za.access.profiler.util;

import co.za.access.profiler.config.CookieData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Slf4j
public final class Interact {

    private final WebDriver driver;
    private WebDriverWait wait;
    private HttpClient client;

    public Interact(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    public Interact(WebDriver driver, WebDriverWait wait,HttpClient client) {
        this.driver = driver;
        this.wait = wait;
        this.client=client;
        log.info("Interact constructor complete");
    }

    public static ChromeOptions options() {
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
    public void clickBtn(WebElement button, boolean submittable, String btnName) {
        try {
            log.info("Locating {}", btnName);
            if (button.isDisplayed()) {
                log.info("Found {} button", btnName);
                if (submittable) {
                    button.submit();
                } else {
                    button.click();
                }
            }

        } catch (NoSuchElementException nsee) {
            log.error("Button {} not found...{}", btnName, nsee.getClass());
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

    public String getHtml(String url){


        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI(url))
                    .build();
            return client.send(request,HttpResponse.BodyHandlers.ofString()).body();

        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("Error occurred while trying to access {}",url);
            throw new RuntimeException(e);
        }
    }



    public void visitLink(String link){
        log.info("Visiting "+link);
       try{
           driver.navigate().to(link);
           log.info("Successfully accessed "+link);
       }catch (Exception ex){
           log.error("Failed to access "+link);
       }

    }

    public Element getElement(String cssSelector, Document doc,String elementName){
        log.info("Locating {} element",elementName);
        Element element =null;
        try {
            element = doc.selectFirst(cssSelector);
            log.info("{} found!",elementName);

        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException |
                 IllegalArgumentException ex) {
            log.error("Error accessing {} {}",elementName,ex.getClass());
        }
        return element;
    }
    public WebElement getElement(By locator, String elementName) {
        log.info("Locating {} element",elementName);
        WebElement element =null;
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            log.info("{} found!",elementName);

        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException |
                 IllegalArgumentException ex) {
            log.error("Error accessing {} {}",elementName,ex.getClass());
        }
        return element;
    }

    public String getElementString(By locator, String message) {
        String element = "No Activity Found";


        try {
            element = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator))
                    .stream().map(WebElement::getText).collect(Collectors.joining("\n"));
            log.info("{}:\n{}",message,element);

        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException |
                 IllegalArgumentException ex) {
            log.info(element+" "+ex.getClass());
        }
        return element;
    }

    public List<WebElement> getElements(By locator, String elementName){

        List<WebElement> elements=null;

        try{

            log.info("Accessing {} elements",elementName);
            elements=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            log.info("Found {} elements",elementName);
        } catch (Exception ex) {
            log.info(elements+" "+ex.getClass());
            log.error("Failed to find element {} ",elementName);
        }
        return elements;

    }

    public Cookie addCookie(CookieData cookieData) {
        return new Cookie.Builder(cookieData.getName(), cookieData.getValue())
                .domain(cookieData.getDomain())
                .path(cookieData.getPath())
                .sameSite(cookieData.getSameSite())
                .isHttpOnly(cookieData.isHttpOnly())
                .expiresOn(cookieData.getExpiry())
                .isSecure(cookieData.isSecure())
                .build()
                ;

    }
    public static String toCookieString(CookieData cookieData) {
        StringJoiner cookieString = new StringJoiner("; ");

        // Add mandatory fields: name and value
        cookieString.add(cookieData.getName() + "=" + cookieData.getValue());

        // Add optional fields if they are not null
        if (cookieData.getDomain() != null) {
            cookieString.add("Domain=" + cookieData.getDomain());
        }
        if (cookieData.getPath() != null) {
            cookieString.add("Path=" + cookieData.getPath());
        }
        if (cookieData.getExpiry() != null) {
            cookieString.add("Expires=" + cookieData.getExpiry().toString());
        }
        if (cookieData.getSize() != null) {
            cookieString.add("Size=" + cookieData.getSize());
        }

        // Add boolean fields
        if (cookieData.isHttpOnly()) {
            cookieString.add("HttpOnly");
        }
        if (cookieData.isSecure()) {
            cookieString.add("Secure");
        }
        if (cookieData.getSameSite() != null) {
            cookieString.add("SameSite=" + cookieData.getSameSite());
        }

        return cookieString.toString();
    }


}


