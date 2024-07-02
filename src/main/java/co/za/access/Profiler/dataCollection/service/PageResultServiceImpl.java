package co.za.access.Profiler.dataCollection.service;

import co.za.access.Profiler.config.AppVariable;
import co.za.access.Profiler.dataCollection.exception.PageResultNotFoundException;
import co.za.access.Profiler.dataCollection.exception.QueryNotFoundException;
import co.za.access.Profiler.dataCollection.exception.ResultsNotFoundException;
import co.za.access.Profiler.dataCollection.model.PageResult;
import co.za.access.Profiler.dataCollection.model.Query;
import co.za.access.Profiler.dataCollection.model.Result;
import co.za.access.Profiler.dataCollection.model.Source;
import co.za.access.Profiler.dataCollection.repository.PageResultRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PageResultServiceImpl implements PageResultService {


    private int noOfPages;
    private final PageResultRepository pageResultRepository;
    private final QueryService queryService;
    private final ResultService resultService;
    private final SourceService sourceService;
    private WebDriver driver;
    private final AppVariable appVariable;

    public PageResultServiceImpl(PageResultRepository pageResultRepository,
                                 QueryService queryService, ResultService resultService,
                                 SourceService sourceService, AppVariable appVariable) {
        this.pageResultRepository = pageResultRepository;
        this.queryService = queryService;
        this.resultService = resultService;
        this.sourceService = sourceService;
        this.appVariable = appVariable;
    }

    @Override
    public PageResult findByQuery(String query) {
        try {
            return pullFromDb(query);
        } catch (PageResultNotFoundException | QueryNotFoundException | ResultsNotFoundException ex) {
            return pullFromTheWeb(query);
        }
    }

    @Transactional
    private PageResult pullFromDb(String queryName) throws PageResultNotFoundException, QueryNotFoundException, ResultsNotFoundException {

        PageResult pr = pageResultRepository.findByQueryName(queryName).orElseThrow(() -> new PageResultNotFoundException("Page result of query, " + queryName + " not found"));
        Query query = queryService.findByName(queryName);
        Set<Result> results = resultService.findAllByPageResultId(pr.getId());
        pr.setResults(results);
        pr.setQuery(query);
        return pr;

    }

    private PageResult pullFromTheWeb(String queryName) {
        Query query = new Query();
        query.setName(queryName);
        log.info("Searching for.... " + queryName.trim());
        System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
        driver = new ChromeDriver();


        try {
            driver.get("https://www.google.co.za/search?hl=en");
            rejectCookies();
            WebElement searchBar = driver.findElement(By.name("q"));
            searchBar.sendKeys(queryName.trim());
            searchBar.submit();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement moreResults = driver.findElement(By.cssSelector(".ipz2Oe")); // for the `More Result` button element.

            while (true) {
                noOfPages++;
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", moreResults);
                    Actions actions = new Actions(driver);
                    actions.moveToElement(moreResults).click().perform();
                    WebElement lastLine = driver.findElement(By.cssSelector("i")); // this element is shown when there's no more data to show

                } catch (NoSuchElementException e) {
                    continue;
                } catch (TimeoutException e) {
                    log.error("Timeout! Slow internet{} ", e.getMessage());
                    log.info("Exiting the program!");
                    break;
                }
                System.out.println("here");
                break;
            }

            Document doc = Jsoup.parse(driver.getPageSource());
            Element element = doc.body();
            List<Element> results = element.select(".cvP2Ce").stream().distinct().toList();

            results.forEach(e -> {
                log.info("result: \n {}", e.html());
            });

            Set<Result> searchResults = results.stream()
                    .filter(e ->
                         !e.select("h3.DKV0Md").text().isBlank()
                                && !e.select(".VwiC3b").text().isBlank()
                                && !e.select("a[href^=\"http\"]").attr("href").isBlank()
        ).map(e -> {

                        String title = e.select("h3.DKV0Md").text();
                        String srcLink = e.select("a[href^=\"http\"]").attr("href");
                        String description = e.select(".VwiC3b").text();
                        String srcName = e.getElementsByClass("VuuXrf").text();
                        String srcImg = e.getElementsByClass("XNo5Ab").attr("src");
                        String resultImg = e.select(".ez24Df img").attr("src");
                        Source src = new Source(0L, srcImg, srcLink, srcName);
                        return new Result(0L, src, title, description, resultImg, null);
                    })
                    .distinct()
                    .filter(e -> !e.getSrc().getSrcName().isBlank() && !e.getTitle().isBlank() && !e.getDescription().isBlank())
                    .collect(Collectors.toSet());


            PageResult pr = pageResultRepository.save(new PageResult(0L, query,null, noOfPages));
            searchResults.forEach(r -> r.setPageResult(pr));  // linking results with pageResult
            pr.setResults(resultService.saveAll(searchResults));
            return pr;
        } catch (Exception e) {
           log.error(e.getMessage());
            throw new RuntimeException("Error, procession query!");
        } finally {
            driver.quit();
        }
    }

    public void searchingPerson(String name){

    }

    public void rejectCookies(){
        try {
            log.info("Rejecting cookies...");
            WebElement rejectAllBtn = driver.findElement(By.id("W0wltc"));
            if(rejectAllBtn.isDisplayed()){
                rejectAllBtn.click();
                log.info("Cookies rejected...");
            }

        }catch (NoSuchElementException nsee){
            log.error("Cookies Button not found... Proceeding with operations");
        }
    }
    private void cleaningResult(Set<Result> result) {


    }
}
