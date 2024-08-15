package co.za.access.profiler.dataCollection.whatsappCollection.controller;


import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.util.Interact;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/whatsapp")
@Slf4j
public class WhatsappController {

//
//    @GetMapping
//    public ResponseEntity<String> login(@RequestBody List<CookieData> cookieDataList){
//
////        System.setProperty("webdriver.chrome.driver", "C:\\Users\\AnakieMamba\\OneDrive - Mecer Inter-ED\\Desktop\\chromedriver-win64 (126)\\chromedriver-win64\\chromedriver.exe");
//        ChromeOptions options = Interact.options();
//        WebDriver driver = new ChromeDriver(options);
//       WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//       driver.get("https://web.whatsapp.com/");
//       Interact interact=new Interact(driver,wait);
//
////       if(new File(""))
//        try {
//            Thread.sleep(Duration.ofMinutes(1).toMillis());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//
//        if (cookieDataList != null) {
//            cookieDataList.forEach(cookie -> driver.manage().addCookie(interact.addCookie(cookie)));
//            driver.navigate().refresh();
//
//        }
//        return new ResponseEntity<>("Logged in", HttpStatus.OK);
//    }


}
