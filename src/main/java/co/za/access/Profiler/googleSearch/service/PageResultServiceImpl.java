package co.za.access.Profiler.googleSearch.service;

import co.za.access.Profiler.googleSearch.model.PageResult;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class PageResultServiceImpl implements PageResultService {

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    @Override
    public PageResult getPageResult(String query, int noOfPages) {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);


        final WebDriver driver = new ChromeDriver();


        try {
            driver.get("https://www.google.co.za");
            WebElement searchBar = driver.findElement(By.name("q"));
            searchBar.sendKeys(query);
            searchBar.submit();
            WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
            WebElement moreResults = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ipz2Oe")));

            for (int i = 0; i < 10; i++) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moreResults);
                Actions actions = new Actions(driver);
                actions.moveToElement(moreResults).click().perform();
            }

            WebElement page = wait.until(ExpectedConditions.visibilityOfElementLocated((By.cssSelector("#rso , #kp-wp-tab-overview , #kp-wp-tab-overview span , .KsRP6 , .QpPSMb , .wDYxhc+ .wDYxhc , .sdjuGf"))));

            System.out.println(page.getText());


            return null;
        } finally {
            driver.quit();
        }
    }


}
