package co.za.access.profiler.dataCollection.linkedinSearch.controller;

import co.za.access.profiler.dataCollection.linkedinSearch.service.LinkedinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("profiler/api/v1/linkedinSearch")
public class LinkedinController {



    private final LinkedinService linkedinService;

    public LinkedinController(LinkedinService linkedinService) {
        this.linkedinService = linkedinService;
    }


    @GetMapping("/{targetName}")
    public ResponseEntity<String> get(@PathVariable String targetName){
        return new ResponseEntity<>(linkedinService.linkedinSearch(targetName), HttpStatus.OK);
    }



}
