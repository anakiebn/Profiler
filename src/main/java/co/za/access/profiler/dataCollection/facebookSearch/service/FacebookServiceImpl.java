package co.za.access.profiler.dataCollection.facebookSearch.service;

import ch.qos.logback.core.status.Status;
import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.config.FacebookVariable;
import co.za.access.profiler.dataProcessing.model.*;
import co.za.access.profiler.util.Interact;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

        log.info("Loading chrome driver...");
//        System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
//        driver = new ChromeDriver(Interact.options());
        int TIMEOUT = 10;
//        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        log.info("Opening Facebook...");

        CookieManager cookieManager=new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        client=HttpClient.newBuilder().cookieHandler(cookieManager).build();
//        driver.get("https://web.facebook.com/search/people?q=" + target.replace(" ", "%20"));

        if (cookieDataList != null) {
        request=HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://web.facebook.com/search/people?q=" + target.replace(" ", "%20")))
                .header("Cookie",cookieDataList.stream().map(Interact::toCookieString).collect(Collectors.joining(";")))
                .build();
            try {
                System.out.println("Request send: "+client.send(request, HttpResponse.BodyHandlers.ofString()).uri());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            interact = new Interact(driver, wait, client);
//
//            cookieDataList.forEach(cookie -> driver.manage().addCookie(interact.addCookie(cookie)));
//            driver.navigate().refresh();
        }
    }

    @Override
    public final List<Target> searchPerson(String target, List<CookieData> cookieDataList, int noOfPages) {
        openFacebook(cookieDataList, target);
        if (cookieDataList == null) { // If you are not using cookies for authentication
            interact.clickBtn(By.id(facebookVariable.getCookieWindow()), false, "cookie"); // rejecting cookies
            logIntoFacebook();
        }
        HttpResponse<String> response=null;
        try {
            response=client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
//        scroll(noOfPages);
        log.info("Beginning: \n{}\nEnd", response.body());
        Document doc = Jsoup.parse(response.body());

        try {
            Elements elements = doc.select(facebookVariable.getProfile()); // gets hold of profiles found
            log.info("Number of profiles: " + elements.size());

            return elements.stream().map(profile -> {
                String profileLink = profile.select(facebookVariable.getProfileLink()).attr("href"); //extract profile link

                if (profileLink.isBlank()) {
                    log.info("Profile link not found");
                    return new Target("No name", "No Image", "No link", null);
                } else {
                    log.info("Visiting Profile link: " + profileLink);

                    Document doc1 = Jsoup.parse(interact.getHtml(profileLink));
                    log.info("Page Loaded...");
                    int retry = 0;
                    while (true) {
                        Element profileImage = null;
                        Element profileName = null;
                        try {
                            profileImage = interact.getElement(facebookVariable.getProfileImageLink(), doc1, "Profile Image"); // second param has no value yet other than making this method overloadable
                            profileName = interact.getElement(facebookVariable.getProfileName(), doc1, "Profile Name");

                            log.info("Profile Image: " + profileImage.attr("xlink:href"));
                            String name = profileName == null ? "Name Not Found" : profileName.text();
                            log.info("Profile Name: " + name);
                            return new Target(name, profileImage.attr("xlink:href"), profileLink, getAbout(profileLink));

                        } catch (TimeoutException | NoSuchElementException toe) {

                            if (++retry == 3) {
                                log.error("Couldn't find image");
                                profileName = interact.getElement(facebookVariable.getProfileName(), doc1, "Profile Name");

                                try {
                                    return new Target(profileName.text(), "No Image", profileLink, getAbout(profileLink));
                                } catch (NoSuchElementException | TimeoutException nsee) {
                                    return new Target("No Name", "No Image", profileLink, getAbout(profileLink));
                                }

                            }
                            log.info("Try no: " + retry);
                        }

                    }

                }

            }).toList();
        } catch (Exception e) {
            log.info("Internal Server error" + e.getClass());
            return null;
        }
    }

    private void scroll(final int noOfPages) {
        final int PIXELS = 3_000; // from research, 1000 pixels is a page
        final int DELAY_TIME = 2_000; // 2 seconds delay is enough just so the content can finish loading.

        if (noOfPages > 1) {
            for (int i = 0; i < noOfPages; i++) {
                log.info("Scrolled! {}", i + 1);
                ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, arguments[0]);", PIXELS);
            }
        }

        try {
            Thread.sleep(DELAY_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void logIntoFacebook() {
        log.info("Logging into Facebook as, {} ", appVariable.getLoginEmail());
        interact.sendInput(By.id(facebookVariable.getEmailField()), appVariable.getLoginEmail(), "email", false, false); // insert email
        interact.sendInput(By.id(facebookVariable.getPasswordField()), appVariable.getLoginPassword(), "password", false, false); // insert password
        interact.clickBtn(By.name(facebookVariable.getLoginBtn()), true, "login"); // click login button
    }


    @PreDestroy
    public void cleanUp() {
        if (driver != null) {
            driver.close();
            System.exit(Status.INFO);
        }
    }


    private About getAbout(String profileLink) {

        interact.visitLink(profileLink + facebookVariable.getWorkAndEducationParam());
        List<WebElement> workAndCollege = interact.getElements(By.cssSelector(facebookVariable.getWorkAndCollege()), "Work and College");

        List<WorkOrSchool> jobs = workAndCollege.get(0).findElements(By.cssSelector(facebookVariable.getWorkNameAndPlaceName()))
                .stream().map(e -> new WorkOrSchool(e.getText()))
                .toList();
        List<WorkOrSchool> schools = workAndCollege.get(1).findElements(By.cssSelector(facebookVariable.getWorkNameAndPlaceName()))
                .stream().map(e -> new WorkOrSchool(e.getText()))
                .toList();
        interact.visitLink(profileLink + facebookVariable.getRelationshipParam());
        List<FamilyMember> familyMembers = interact.getElements(By.cssSelector(facebookVariable.getFamily()), "Family")
                .stream().map(e -> new FamilyMember(e.getText())).toList();

        List<InRelationshipWith> inRelationshipWith = interact.getElements(By.cssSelector(facebookVariable.getFamily()), "Relationship")
                .stream().map(e -> new InRelationshipWith(e.getText())).toList();
        ;

        interact.visitLink(profileLink + facebookVariable.getPlaceLivedParam());
        String hometown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(facebookVariable.getHometown()))).getText();
        String currentCity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(facebookVariable.getCurrentCity()))).getText();
        ;
        interact.visitLink(profileLink + facebookVariable.getContactParam());
        ContactInfo contactInfo = null;
        BasicInfo basicInfo = null;


        return new About(jobs, schools, familyMembers, inRelationshipWith, hometown, currentCity, contactInfo, basicInfo);

    }

}
