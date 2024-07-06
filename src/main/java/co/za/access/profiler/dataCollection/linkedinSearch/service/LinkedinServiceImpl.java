package co.za.access.profiler.dataCollection.linkedinSearch.service;

import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.config.LinkedinVariable;
import co.za.access.profiler.util.Interact;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.time.Duration;
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


    public void openLinkedin(){
        log.info("Loading chrome driver...");
        System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
        driver = new ChromeDriver(Interact.options()); // open chrome using these option
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.linkedin.com/uas/login?session_redirect=https%3A%2F%2Fwww.linkedin.com%2Ffeed%2F");
        interact=new Interact(driver,wait);
    }


    /**
     * This method performs LinkedIn search operation, it logs into the site first then it searches the person
     *
     * @param targetName - Name of the person you want to search
     *
     */

    @Override
    public String linkedinSearch(String targetName) {
        openLinkedin(); // this method will open LinkedIn
        login(); // then logs in using the provided details
        interact.sendInput(By.cssSelector(linkedinVariable.getSearchField()),targetName,"search",false,true); // presses `search` button
        interact.clickBtn(By.cssSelector(linkedinVariable.getSeeAllPeopleResultBtn()),false,"see all people result button"); // presses `see all people` button

        List<WebElement> people = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy((By.cssSelector(linkedinVariable.getResult()))));


        return people.stream().map(e->{
            e.click();
            WebElement name=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(linkedinVariable.getTargetName())));
            driver.navigate().back();
            return name.getTagName();
        }).peek(e-> {
            System.out.println("Names: "+e);
        }).collect(Collectors.joining("\n"));
    }


    /**
     * This method performs login operations.
     *
     */

    private void login() {
        interact.sendInput(By.id(linkedinVariable.getEmailField()),appVariable.getLoginEmail(),"email",false,false);
        interact.sendInput(By.id(linkedinVariable.getPasswordField()),appVariable.getLoginPassword(),"password",false,false);
        interact.clickBtn(By.cssSelector(linkedinVariable.getSignIn()),true,"login");
    }

    private String findTextFromElement(){
        try {
            List<WebElement> people = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy((By.cssSelector(linkedinVariable.getResult()))));


            return people.stream().map(WebElement::getText).peek(e -> {
                System.out.println("Names: " + e);
            }).collect(Collectors.joining("\n"));
        }catch (NoSuchElementException | TimeoutException | StaleElementReferenceException nsee){
            log.error(nsee.getMessage());
        }
        return "No results";
    }


    public void turnIntoModel(){


        // get picture
        // click person
        // get title
        /// get current position
        // get contact info
        // get address
        //get about
        //get experience
        // education



    }

}

