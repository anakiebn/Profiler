package co.za.access.profiler.dataCollection.linkedinSearch.service;

import ch.qos.logback.core.status.Status;
import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.config.LinkedinVariable;
import co.za.access.profiler.dataCollection.linkedinSearch.model.Experience;
import co.za.access.profiler.util.Interact;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LinkedinServiceImpl implements LinkedinService {
    private final LinkedinVariable linkedinVariable;
    private final AppVariable appVariable;
    private Interact interact;
    private WebDriverWait wait;
    private WebDriver driver;

    public LinkedinServiceImpl(LinkedinVariable linkedinVariable, AppVariable appVariable) {
        this.linkedinVariable = linkedinVariable;
        this.appVariable = appVariable;
    }

    public void openLinkedin(List<CookieData> cookieDataList) {
        try {
            log.info("Loading chrome driver...");
            System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
            driver = new ChromeDriver(Interact.options()); // open chrome using these option
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            interact = new Interact(driver, wait);
            driver.get("https://www.linkedin.com/");
            if (cookieDataList != null) {
                cookieDataList.forEach(cookie -> driver.manage().addCookie(interact.addCookie(cookie)));
                driver.navigate().refresh();
            }
        } catch (IllegalArgumentException iex) {
            log.error("Didn't expect such argument: " + iex.getMessage());
        } catch (WebDriverException w) {
            log.error("Poor network: " + w.getMessage());
        } catch (Exception e) {
            log.error("Unknown error: " + e.getMessage());
        }
    }
    /**
     * This method performs LinkedIn search operation, it logs into the site first then it searches the person
     *
     * @param targetName - Name of the person you want to search
     */

    @Override
    public String linkedinSearch(String targetName, List<CookieData> cookieDataList) {
        if (cookieDataList == null) {
            openLinkedin(null); // this method will open LinkedIn
            login(); // then logs in using the provided details

        } else {
            openLinkedin(cookieDataList);
        }
        interact.sendInput(By.cssSelector(linkedinVariable.getSearchField()), targetName, "search", false, true); // presses `search` button
        interact.clickBtn(By.cssSelector(linkedinVariable.getSeeAllPeopleResultBtn()), false, "see all people result button"); // presses `see all people` button
        return findTextFromElement(targetName);
    }


    /**
     * This method performs login operations.
     */

    private void login() {
        interact.sendInput(By.id(linkedinVariable.getEmailField()), appVariable.getLoginEmail(), "email", false, false);
        interact.sendInput(By.id(linkedinVariable.getPasswordField()), appVariable.getLoginPassword(), "password", false, false);
        interact.clickBtn(By.cssSelector(linkedinVariable.getSignIn()), true, "login");
    }


    private List<Document> allPages(String targetName) {

        List<Document> doc = new ArrayList<>();
        int pgNo = 1; // start from page 1
        int i = driver.getCurrentUrl().lastIndexOf("=");  //Get search id, apparently each search has an id
        String searchId = driver.getCurrentUrl().substring(i + 1);
        log.info("Current search Id is {}", searchId);

        while (true) {
            try {
                log.info("Extracting page {} html", pgNo);
                doc.add(Jsoup.parse(driver.getPageSource()));
                log.info("Page {} document created!", pgNo);
                if (Jsoup.parse(driver.getPageSource()).body().text().contains("No results found")) {
                    log.info("Search complete!");
                    break;
                }

                if(pgNo==2){
                    log.info("Limited search to {} pages", pgNo);
                    break;
                }

                clickNext(targetName, ++pgNo, searchId);

            } catch (Exception e) {
                log.error("Error thrown while extracting pages:\n {}", e.getMessage());
            }
        }
        return doc;
    }

    private String findTextFromElement(String targetName) {

        log.info("getting targets info");
        return allPages(targetName)
                .stream()
                .flatMap(document -> document.select(linkedinVariable.getResult()).stream())
                .filter(e -> e.select(linkedinVariable.getPersonLink()).attr("href").startsWith(linkedinVariable.getValidLink())) // Filter only documents with that have a valid profiler link
                .map(e -> {
                    String url = e.select(linkedinVariable.getPersonLink()).attr("href"); // get each persons profile link
                    log.info("Visiting : {}", url);
                    driver.navigate().to(url); //  visit targets profile
                    int retry=0;
                    while (true) {
                        String profileName=null;
                        String profilePic=null;
                        try {

                            profileName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(linkedinVariable.getTargetName()))).getText();
//                            String pSelector=linkedinVariable.getProfilePicture().replace("%s",profileName);
                            String pSelector=linkedinVariable.getProfilePicture();

                            log.info("Image selector: "+pSelector);
                            profilePic = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(pSelector))).getAttribute("src");

//                            getExperience(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(linkedinVariable.getExperienceSection()))));

                            return "Name: " + profileName + "\nImage Link: " + profilePic;
                        } catch (TimeoutException toe) {
                            if(retry++==3){
                                log.info("Failed to process profile");
                                return "Name: " + profileName==null?"Failed to view profile":profileName + "\nImage Link: " + profilePic==null?"Failed to load img":profilePic;
                            }
                            log.info("Timeout exception... Still waiting for profile to load:");

                        }
                    }
                }).peek(n -> log.info("Accessing {}'s profile\n", n)).collect(Collectors.joining("\n"));

    }

    public List<Experience> getExperience(WebElement element) {
        System.out.println("Property: " + linkedinVariable.getHeader());

        try {
            WebElement header = element.findElement(By.cssSelector(linkedinVariable.getHeader()));
            log.info("Begins here:\nHeader: {}\n", header.getText());

            List<WebElement> experiences = element.findElements(By.cssSelector(linkedinVariable.getExperiences()));
            if (experiences.isEmpty()) {
                log.error("No experience found:");
                log.info("No of experiences: {}", experiences.size());
            }
            experiences.forEach(e -> {
                WebElement companyName = e.findElement(By.cssSelector(linkedinVariable.getCompanyName()));
                log.info("Company name: {}", companyName.getText());
                List<WebElement> positions = e.findElements(By.cssSelector((linkedinVariable.getPositions())));
                log.info("No Of positions: {}", positions.size());
                positions.forEach(p -> {
                    WebElement positionName = p.findElement(By.cssSelector(linkedinVariable.getPositionName()));

                    log.info("Position: {}", positionName.getText());
                    log.info("");

                });
                log.info("---------");

            });
        } catch (NoSuchElementException | NullPointerException ex) {
            log.info("No more headers: {}", ex.getMessage());
        } catch (TimeoutException ex) {
            log.info("Slow network");

        }

        //        log.info(element.select(".soRXgxWJJMgZLzjQJfhmoIJTmInhAao").text()+"\n");
        return null;
    }

    /**
     * I tried to click the next button by just clicking it, unfortunately I couldn't so, I've implemented this logic
     *
     * @param name   - This the name of you are searching
     * @param pageNo - Page number you want to locate
     * @param sid    - Search id, All searches have a unique id
     */

    private void clickNext(String name, int pageNo, String sid) {
        try {
            log.info("Moving to the next page");
            String link = "https://www.linkedin.com/search/results/people/?keywords=" + name.replace(" ", "%20") + "&origin=GLOBAL_SEARCH_HEADER&page=" + pageNo + "&sid=" + sid;
            driver.navigate().to(link);
        } catch (Exception e) {
            log.error("Error occurred while trying to move to the next page");
        }
    }


    public void turnIntoModel() {
        // get picture - Done
        // click person - Done
        // get title - Done
        /// get current position
        // get contact info
        // get address
        //get about
        //get experience
        // education
    }

    @PreDestroy
    public void cleanUp() {
        if (driver != null) {
            driver.close();
            System.exit(Status.ERROR);
        }
    }


}
