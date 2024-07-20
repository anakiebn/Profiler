package co.za.access.profiler.dataCollection.googleSearch.service;

import co.za.access.profiler.config.AppVariable;
import co.za.access.profiler.config.GoogleVariable;
import co.za.access.profiler.dataCollection.googleSearch.exception.PageResultNotFoundException;
import co.za.access.profiler.dataCollection.googleSearch.exception.QueryNotFoundException;
import co.za.access.profiler.dataCollection.googleSearch.exception.ResultsNotFoundException;
import co.za.access.profiler.dataCollection.googleSearch.model.PageResult;
import co.za.access.profiler.dataCollection.googleSearch.model.Result;
import co.za.access.profiler.dataCollection.googleSearch.model.Source;
import co.za.access.profiler.dataCollection.googleSearch.repository.PageResultRepository;
import co.za.access.profiler.dataCollection.googleSearch.repository.ResultRepository;
import co.za.access.profiler.util.Interact;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoogleSearchImpl implements GoogleSearch {

    private static final int TIMEOUT = 10;

    private final PageResultRepository pageResultRepository;
    private final ResultService resultService;
    private final ResultRepository resultRepository;
    private final SourceService sourceService;
    private final AppVariable appVariable;
    private final GoogleVariable googleVariable;

    private WebDriver driver;
    private WebDriverWait wait;
    private List<Element> resultElements;
    private int noOfPages;

    public GoogleSearchImpl(PageResultRepository pageResultRepository,
                            ResultRepository resultRepository,
                            GoogleVariable googleVariable,
                            ResultService resultService,
                            SourceService sourceService,
                            AppVariable appVariable) {
        this.pageResultRepository = pageResultRepository;
        this.resultRepository = resultRepository;
        this.resultService = resultService;
        this.sourceService = sourceService;
        this.appVariable = appVariable;
        this.googleVariable = googleVariable;
        this.resultElements = new ArrayList<>();
    }

    @Override
    public PageResult findByQuery(String query) {
        log.info("Searching target...{}", query);
        try {
            return pullFromDb(query);
        } catch (PageResultNotFoundException | QueryNotFoundException | ResultsNotFoundException ex) {
            log.info("Person not found from database... Searching on Google");
            return pullFromTheWeb(query);
        }
    }

    @Transactional
    private PageResult pullFromDb(String queryName) throws QueryNotFoundException, ResultsNotFoundException, PageResultNotFoundException {
        log.info("Searching target from database...");
        PageResult pr = pageResultRepository.findByQuery(queryName)
                .orElseThrow(() -> new PageResultNotFoundException("Page result of query, " + queryName + " not found"));
        Set<Result> results = resultService.findAllByPageResultId(pr.getId());
        pr.setResults(results);
        pr.setQuery(queryName);
        return pr;
    }

    private void openChrome() {
        try {
            log.info("Loading chrome driver...");
            System.setProperty("webdriver.chrome.driver", appVariable.getChromeDriver());
            ChromeOptions options = Interact.options();
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
            driver.get("https://www.google.co.za/search?hl=en");
        } catch (Exception ex) {
            log.error("Couldn't load or open Chrome", ex);
        }
    }

    @Transactional
    private PageResult pullFromTheWeb(String queryName) {
        log.info("Searching target from Google...");
        try {
            openChrome();
            Interact interact = new Interact(driver, wait);
            interact.clickBtn(By.id(googleVariable.getRejectCookieBtn()), false, "cookie"); // reject cookies
            interact.sendInput(By.name(googleVariable.getSearchField()), queryName, "Input", true, false); // search target on chrome
            getAllResults(); // gets all results from the search

            Set<Result> searchResults = convertElementsToResult(); // save results to database
            PageResult pr = new PageResult(queryName, searchResults, noOfPages);
            searchResults.forEach(r -> r.setPageResult(pr)); // linking results with pageResult
//            resultService.saveAll(searchResults);
            return pageResultRepository.save(pr); // turns all the results into page result
        } catch (Exception e) {
            log.error("Error while searching target from Google", e);
        } finally {
            if (driver != null) {

                System.out.println("Quit...");
//                driver.quit();
            }
        }
        return null;
    }

    private List<Element> extractFromGoogle() {
        log.info("Extracting data from Google into Jsoup Elements");
        Document doc = Jsoup.parse(driver.getPageSource());
        Element element = doc.body();
        List<Element> results = element.select(googleVariable.getResults()).stream().distinct().collect(Collectors.toList());

        System.out.println("t-14 t-normal\n" +
                "                  t-black--light");
        results.forEach(e -> log.info("Result: \n{}", e.html()));
        log.info("Extraction was successful...");
        return results;
    }

    private Set<Result> convertElementsToResult() {


        return resultElements.stream()
                .filter(e -> !e.select(googleVariable.getResultTitle()).text().isBlank()
                        && !e.select(googleVariable.getResultDescription()).text().isBlank()
                        && !e.select(googleVariable.getResultSrcLink()).attr("href").isBlank())
                .map(e -> {
                    String title = e.select(googleVariable.getResultTitle()).text();
                    String srcLink = e.select(googleVariable.getResultSrcLink()).attr("href");
                    String description = e.select(googleVariable.getResultDescription()).text();
                    String srcName = e.select(googleVariable.getResultSrcName()).text();
                    String srcImg = e.select(googleVariable.getResultSrcImg()).attr("src");
                    String resultImg = e.select(googleVariable.getResultImg()).attr("src");
                    Source src = new Source(srcImg, srcLink, srcName);
                    return new Result(src, title, description, resultImg, null);
                })
                .peek(e->log.info("name: {}\ntitle: {}\n\n",e.getSrc().getSrcName(),e.getTitle()))
                .peek(e -> log.info("Successfully converted element to result with title: {}", e.getTitle()))
                .distinct()
                .filter(e -> !e.getSrc().getSrcName().isBlank() && !e.getTitle().isBlank() && !e.getDescription().isBlank())
                .collect(Collectors.toSet());
    }

    private void getAllResults() {
        log.info("Getting all results from Google...");
        while (true) {
            noOfPages++; // counts number of pages visited.
            try {
                resultElements.addAll(extractFromGoogle()); // collects the html elements per page into usable result objects -  the bucket for results
                WebElement next = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(googleVariable.getNextBtn())));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", next);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", next); // click the link to the next page




            } catch (NoSuchElementException e) {
                log.info("Scrapped all results...");
                break; // if the next page section is not found then, this means you've reached the end, no more results to scrap
            } catch (TimeoutException e) {
                log.error("Timeout! Slow internet");
                log.info("Exiting the program! Scrapped all results...");
                break;
            } catch (Exception e) {
                log.error("Something went wrong while getting all results", e);
                break;
            }
        }
    }
}
