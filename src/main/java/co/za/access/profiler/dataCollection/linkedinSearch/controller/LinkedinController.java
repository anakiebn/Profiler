package co.za.access.profiler.dataCollection.linkedinSearch.controller;

import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.dataCollection.linkedinSearch.service.LinkedinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("profiler/api/v1/linkedinSearch")
@Slf4j
public class LinkedinController {


    private final LinkedinService linkedinService;

    public LinkedinController(LinkedinService linkedinService) {
        this.linkedinService = linkedinService;
    }


    @GetMapping("/{targetName}")
    public ResponseEntity<String> get(@PathVariable String targetName) {
        return new ResponseEntity<>(linkedinService.linkedinSearch(targetName, null), HttpStatus.OK);
    }


    @GetMapping("/cookieAuth/{targetName}")
    public ResponseEntity<String> get(@PathVariable String targetName, @RequestBody List<CookieData> cookieDataList) {


        for(CookieData cookieData:cookieDataList){

            log.info("Domain " + cookieData.getDomain());
            log.info("Path " + cookieData.getPath());
            log.info("SameSite " + cookieData.getSameSite());
            log.info("HttpOnly " + cookieData.isHttpOnly());
            log.info("Expiry " + cookieData.getExpiry());
            log.info("IsSecure " + cookieData.isSecure());
        }
        return new ResponseEntity<>(linkedinService.linkedinSearch(targetName, cookieDataList), HttpStatus.OK);
    }


}
