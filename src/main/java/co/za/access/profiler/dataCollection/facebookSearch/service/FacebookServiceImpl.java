package co.za.access.profiler.dataCollection.facebookSearch.service;

import ch.qos.logback.core.status.Status;
import co.za.access.Profiler.config.FacebookVariable;
import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.util.Interact;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private void openFacebook(List<CookieData> cookieDataList, String target) {

        log.info("Loading chrome driver...");
        System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
        driver = new ChromeDriver(Interact.options());
        int TIMEOUT = 10;
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        log.info("Opening Facebook...");
        interact = new Interact(driver, wait);
        driver.get("https://web.facebook.com/search/people?q=" + target.replace(" ", "%20"));

        if (cookieDataList != null) {
            cookieDataList.forEach(cookie -> driver.manage().addCookie(interact.addCookie(cookie)));
            driver.navigate().refresh();
        }
    }

    @Override
    public final String searchPerson(String target, List<CookieData> cookieDataList) {
        openFacebook(cookieDataList, target);
        if (cookieDataList == null) { // If you are not using cookies for authentication
            interact.clickBtn(By.id(facebookVariable.getCookieWindow()), false, "cookie"); // rejecting cookies
            logIntoFacebook();
        }

        scroll(1, 3000, 1000);
        Document doc = Jsoup.parse(driver.getPageSource());

        try {
            Elements elements = doc.select(facebookVariable.getProfile()); // gets hold of profiles found
            log.info("Number of profiles: " + elements.size());
          return  elements.stream().map(profile -> {
                String profileLink = profile.select(facebookVariable.getProfileLink()).attr("href");

                if (profileLink.isBlank()) {
                    log.info("Profile link not found");
                    return "Profile link not found";
                } else {
                    log.info("Profile link: " + profileLink);
                    driver.navigate().to(profileLink);
                    int retry = 0;
                    while (true) {
                        try {
                            WebElement image = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(facebookVariable.getProfileImageLink())));
                            WebElement profileName=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(facebookVariable.getProfileName())));
                            log.info("Profile Image: " + image.getAttribute("xlink:href"));
                           return "Name: "+profileName.getText()+"\nImage: "+image;
                        } catch (TimeoutException toe) {
                            if (++retry == 3) {
                                log.error("Couldn't find image");
                                return "Failed to process profile...";
                            }
                            log.info("Try no: " + retry);

                        }

                    }

                }

            }).collect(Collectors.joining("\n"));
        } catch (Selector.SelectorParseException sspe) {
            log.info("Facebook selector error: " + sspe);
            return "No results, try again";
        }


    }

    private void scroll(final int noOfPages, final int delayTime, final int pixel) {
        if (noOfPages > 0) {
            for (int i = 0; i < noOfPages; i++) {
                log.info("Scrolled! {}", i + 1);
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", pixel);
            }
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        driver.navigate().refresh();
    }


    private void logIntoFacebook() {
        log.info("Logging into Facebook as, {} ", appVariable.getLoginEmail());
        interact.sendInput(By.id(facebookVariable.getEmailField()), appVariable.getLoginEmail(), "email", false, false); // insert email
        interact.sendInput(By.id(facebookVariable.getPasswordField()), appVariable.getLoginPassword(), "password", false, false); // insert password
        interact.clickBtn(By.name(facebookVariable.getLoginBtn()), true, "login"); // click login button

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

//    @PreDestroy
//    public void cleanUp() {
//        if (driver != null) {
//            driver.close();
//            System.exit(Status.ERROR);
//        }
//    }


}
