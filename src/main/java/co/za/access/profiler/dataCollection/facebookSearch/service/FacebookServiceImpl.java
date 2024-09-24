package co.za.access.profiler.dataCollection.facebookSearch.service;

import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.config.FacebookVariable;
import co.za.access.profiler.dataProcessing.model.*;
import co.za.access.profiler.util.Interact;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FacebookServiceImpl implements FacebookService {

    private WebDriver driver;
    private HttpClient client;
    private HttpRequest request;
    private WebDriverWait wait;

    private final AppVariable appVariable;
    private final FacebookVariable facebookVariable;
    private Interact interact;

    public FacebookServiceImpl(AppVariable appVariable, FacebookVariable facebookVariable) {
        this.appVariable = appVariable;
        this.facebookVariable = facebookVariable;
    }

    private void openFacebook(List<CookieData> cookieDataList, String target) {
        try {
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

//            if(!interact.elementVisible(By.cssSelector(facebookVariable.getLoggedIn()),"visible")){
//                log.error("Signing in error occurred! Check your cookies ");
//                System.exit(0);
//            }

            log.info("Successfully logged in ");
        } catch (Exception e) {
            log.error("Error opening Facebook: ", e);
            log.error("Shutting down program");
            System.exit(0);
        }
    }

    @Override
    public final List<Target> searchPerson(String target, List<CookieData> cookieDataList, int noOfPages) {
        try {
            openFacebook(cookieDataList, target);
            if (cookieDataList == null) {
                interact.clickBtn(By.id(facebookVariable.getCookieWindow()), false, "cookie");
                logIntoFacebook();
            }

            scroll(noOfPages);
            Document doc = Jsoup.parse(driver.getPageSource());

            Elements elements = doc.select(facebookVariable.getProfile());
            log.info("Number of profiles: " + elements.size());

            return elements.stream().map(profile -> {
                String profileLink = profile.select(facebookVariable.getProfileLink()).attr("href");

                if (profileLink.isBlank()) {
                    log.info("Profile link not found");
                    return new Target("No name", "No Image", "No link", getAbout(profileLink));
                } else {
                    log.info("Profile link: " + profileLink);
                    driver.navigate().to(profileLink);
                    return getProfileDetails(profileLink);
                }

            }).collect(Collectors.toList());

        } catch (Selector.SelectorParseException sspe) {
            log.error("Facebook selector error: ", sspe);
            return null;
        }
    }

    private Target getProfileDetails(String profileLink) {
        int retry = 0;
        while (retry < 3) {
            try {
                WebElement profileImage = driver.findElement(By.cssSelector(facebookVariable.getProfileImageLink()));
                WebElement profileName = driver.findElement(By.cssSelector(facebookVariable.getProfileName()));
                log.info("Profile Image: " + profileImage.getAttribute("xlink:href"));
                log.info("Profile Name: " + profileName.getText());
                return new Target(profileName.getText(), profileImage.getAttribute("xlink:href"), profileLink, getAbout(profileLink));
            } catch (TimeoutException | NoSuchElementException | NullPointerException e) {
                retry++;
                log.info("Retry attempt: " + retry);
            }
        }
        log.error("Couldn't find profile info after 3 retries");
        return new Target("No name", "No Image", "No link", null);
    }

    private void scroll(final int noOfPages) {
        final int PIXELS = 3000;
        final int DELAY_TIME = 2000;

        for (int i = 0; i < noOfPages; i++) {
            try {
                log.info("Scrolled! {}", i + 1);
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", PIXELS);
                Thread.sleep(DELAY_TIME);
            } catch (InterruptedException e) {
                log.error("Error during scrolling: ", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void logIntoFacebook() {
        try {
            log.info("Logging into Facebook as, {}", appVariable.getLoginEmail());
            interact.sendInput(By.id(facebookVariable.getEmailField()), appVariable.getLoginEmail(), "email", false, false);
            interact.sendInput(By.id(facebookVariable.getPasswordField()), appVariable.getLoginPassword(), "password", false, false);
            interact.clickBtn(By.name(facebookVariable.getLoginBtn()), true, "login");
        } catch (Exception e) {
            log.error("Error logging into Facebook: ", e);
        }
    }

    @PreDestroy
    public void cleanUp() {
        if (driver != null) {
            try {
                driver.quit();
                log.info("Web driver closed.");
            } catch (Exception e) {
                log.error("Error closing web driver: ", e);
            }
        }
    }

    private About getAbout(String profileLink) {
        try {

            interact.visitLink(profileLink + facebookVariable.getWorkAndEducationParam());
            List<WebElement> workAndCollege = interact.getElements(By.cssSelector(facebookVariable.getWorkAndCollege()), "Work and College");
            List<WorkOrSchool> jobs = extractWorkOrSchool(workAndCollege.get(0), "jobs");
            List<WorkOrSchool> schools = extractWorkOrSchool(workAndCollege.get(1), "schools");

//            interact.visitLink(profileLink + facebookVariable.getRelationshipParam());
////            List<FamilyMember> familyMembers = extractFamilyMembers();
////            List<InRelationshipWith> inRelationshipWith = extractRelationship();
//
//            interact.visitLink(profileLink + facebookVariable.getPlaceLivedParam());
//            String hometown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(facebookVariable.getHometown()))).getText();
//            String currentCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(facebookVariable.getCurrentCity()))).getText();

            return new About(jobs, schools, null,null, null, null, null, null);

//            return new About(jobs, schools, null,null, hometown, currentCity, null, null);
        } catch (NullPointerException e){
            log.error("Error fetching profile information: {} ", e.getMessage());
            return null;
        } catch ( Exception e) {
            log.error("Error fetching profile information: {}", e.getMessage());
            return null;
        }
    }

    private List<WorkOrSchool> extractWorkOrSchool(WebElement section, String type) {
        try {
            return section
                    .findElements(By.cssSelector(facebookVariable.getWorkAndCollegeName()))
                    .stream()
                    .map(element -> new WorkOrSchool(element.getText()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error extracting {} details: ", type, e);
            return List.of();
        }
    }

    private List<FamilyMember> extractFamilyMembers() {
        try {
            return interact.getElements(By.cssSelector(facebookVariable.getFamily()), "Family")
                    .stream()
                    .map(element -> new FamilyMember(element.getText()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error extracting family members: ", e);
            return List.of();
        }
    }

    private List<InRelationshipWith> extractRelationship() {
        try {
            return interact.getElements(By.cssSelector(facebookVariable.getFamily()), "Relationship")
                    .stream()
                    .map(element -> new InRelationshipWith(element.getText()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error extracting relationship information: ", e);
            return List.of();
        }
    }
}
