package co.za.access.Profiler.googleSearch.service;

import co.za.access.Profiler.googleSearch.model.PageResult;
import co.za.access.Profiler.googleSearch.model.Result;
import co.za.access.Profiler.googleSearch.model.Source;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PageResultServiceImpl implements PageResultService {

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    private int noOfPages;


    private String testGit;


    @Override
    public PageResult getPageResult(String query) {
        log.info("Searching for.... "+query);
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        final WebDriver driver = new ChromeDriver();


        try {
            driver.get("https://www.google.co.za");
            WebElement searchBar = driver.findElement(By.name("q"));
            searchBar.sendKeys(query);
            searchBar.submit();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement moreResults = driver.findElement(By.cssSelector(".ipz2Oe")); // the moreResult button element.

            while(true){
                noOfPages++;
                try{
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moreResults);
                Actions actions = new Actions(driver);
                actions.moveToElement(moreResults).click().perform();
                WebElement lastLine=driver.findElement(By.cssSelector("i")); // you'll see this element when there are no more results to be loaded.

                }catch (NoSuchElementException e){
                    continue;
                }catch (TimeoutException e){
                    log.error("Timeout! Slow internet{} ",e.getMessage());
                    log.info("Exiting the program!");
                    break;
                }
                System.out.println("here");
                break;
            }

            Document doc=Jsoup.parse(driver.getPageSource());
            Element element=doc.body();
            List<Element> results=element.select(".cvP2Ce").stream().distinct().toList();

            results.forEach(e->{
                System.out.println("result: "+"\n"+e.html());
            });
            Set<Result> searchResults=results.stream().map(e->{

                String title = e.select("h3.DKV0Md").text();
                String srcLink = e.select("a[href^=\"http\"]").attr("href");
                String description = e.select(".VwiC3b").text();
                String srcName = e.getElementsByClass("VuuXrf").text();
                String srcImg = e.getElementsByClass("XNo5Ab").attr("src");
                String resultImg = e.select(".ez24Df img").attr("src");

                Source src=new Source(srcImg,srcLink,srcName);
               return new Result(src,title,description,resultImg);
            }).distinct().collect(Collectors.toSet());

            return new PageResult(searchResults,noOfPages);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Error, procession query!");
        } finally {
            driver.quit();
        }
    }


}
