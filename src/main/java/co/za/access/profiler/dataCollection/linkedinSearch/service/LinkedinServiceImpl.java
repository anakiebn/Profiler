package co.za.access.profiler.dataCollection.linkedinSearch.service;

import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.config.LinkedinVariable;
import co.za.access.profiler.dataCollection.linkedinSearch.model.Experience;
import co.za.access.profiler.util.Interact;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeFilter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            interact = new Interact(driver, wait);
            driver.get("https://www.linkedin.com");
            if (cookieDataList != null) {
                cookieDataList.forEach(cookie -> driver.manage().addCookie(interact.addCookie(cookie)));
                driver.navigate().refresh();
            }


        } catch (IllegalArgumentException iex) {
            iex.printStackTrace();
            log.error(iex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
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

//        interact.sendInput(By.cssSelector(linkedinVariable.getSearchField()), targetName, "search", false, true); // presses `search` button
//        interact.clickBtn(By.cssSelector(linkedinVariable.getSeeAllPeopleResultBtn()), false, "see all people result button"); // presses `see all people` button
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
                log.info("Page {} document created!",pgNo);
                if (Jsoup.parse(driver.getPageSource()).body().text().contains("No results found")) {
                    log.info("Search complete!");
                    break;
                }



                    log.info("Limited search to {} pages",pgNo);
                    break;


//                clickNext(targetName, ++pgNo, searchId);

            } catch (Exception e) {
                log.error("Error thrown while extracting pages:\n {}", e.getMessage());
                break;
            }
        }
        return doc;
    }

    private String findTextFromElement(String targetName) {
        log.info("getting targets info");
//        log.info("No of pages: {}", allPages(targetName).size());

        driver.navigate().to("https://www.linkedin.com/in/cyril-b-17b3b424/");
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Document doc = Jsoup.parse(driver.getPageSource());
        String profileName=driver.findElement(By.cssSelector(linkedinVariable.getTargetName())).getText();
        String imgLink=driver.findElement(By.cssSelector(linkedinVariable.getProfilePicture().replace("%s", profileName))).getDomAttribute("src");

        getExperience(doc.select(linkedinVariable.getSection()));
        return "Name: "+profileName+"\nImage Link: "+imgLink;




//        return allPages(targetName)
//                .stream()
//                .flatMap(document -> document.select(linkedinVariable.getResult()).stream())
//                .filter(e -> e.select(linkedinVariable.getPersonLink()).attr("href").startsWith(linkedinVariable.getValidLink()))
//                .map(e -> {
//                    String url = e.select(linkedinVariable.getPersonLink()).attr("href"); // get each persons profile link
//                    log.info("Visiting : {}", url);
//                    driver.navigate().to(url); //  visit targets profile
//                    Document doc = Jsoup.parse(driver.getPageSource());
//                    String profileName = doc.select(linkedinVariable.getTargetName()).text();
//                    String imgLink = doc.select(linkedinVariable.getProfilePicture().replace("%s", profileName)).attr("src");
//                    getExperience(doc.select(linkedinVariable.getExperience()).first());
//
//                    return profileName;
//                }).peek(n -> log.info("Accessing {}'s profile\n", n)).collect(Collectors.joining("\n"));

    }
    public List<Experience> getExperience(Elements elements) {
        System.out.println("Property: "+linkedinVariable.getHeader());

        elements.forEach(e-> {

            Element header=e.getE
            log.info("Begins here:\nHeader: {}\n {}",header.text(),e.html());
           });
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
            String link = "https://www.linkedin.com/search/results/people/?keywords=" +
                    name.replace(" ", "%20") +
                    "&origin=GLOBAL_SEARCH_HEADER&page=" +
                    pageNo +
                    "&sid=" +
                    sid;

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
//            driver.close();
//            log.info("Driver closed!");
        }

    }
}

