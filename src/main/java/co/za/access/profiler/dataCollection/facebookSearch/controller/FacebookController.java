package co.za.access.profiler.dataCollection.facebookSearch.controller;


import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.dataCollection.facebookSearch.service.FacebookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/profiler/api/v1/facebookSearch")
public class FacebookController {


    private final FacebookService facebookService;

    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> searchPerson(@PathVariable String name) {
        return new ResponseEntity<>(facebookService.searchPerson(name,null), HttpStatus.OK);
    }

    @GetMapping("cookieAuth/{name}")
    public ResponseEntity<String> searchPerson(@PathVariable String name, @RequestBody List<CookieData> cookieDataList) {
        for (CookieData cookieData : cookieDataList) {

            log.info("Domain " + cookieData.getDomain());
            log.info("Path " + cookieData.getPath());
            log.info("SameSite " + cookieData.getSameSite());
            log.info("HttpOnly " + cookieData.isHttpOnly());
            log.info("Expiry " + cookieData.getExpiry());
            log.info("IsSecure " + cookieData.isSecure());
        }
        return new ResponseEntity<>(facebookService.searchPerson(name,cookieDataList), HttpStatus.OK);
    }


}
