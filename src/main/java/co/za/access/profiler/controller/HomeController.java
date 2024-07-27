package co.za.access.profiler.controller;

import co.za.access.profiler.dataCollection.googleSearch.service.GoogleSearch;
import co.za.access.profiler.dataCollection.googleSearch.service.GoogleSearchImpl;
import co.za.access.profiler.dataCollection.linkedinSearch.service.LinkedinService;
import co.za.access.profiler.dataCollection.linkedinSearch.service.LinkedinServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home")
public class HomeController {

    private GoogleSearch googleSearch;
    private LinkedinService linkedinService;

    public HomeController(GoogleSearch googleSearch, LinkedinService linkedinService) {
        this.googleSearch = googleSearch;
        this.linkedinService = linkedinService;
    }

    @GetMapping
    public String SearchPerson(){
        return "home";
    }

    @PostMapping("/search")
    public String Search(@RequestParam String targetName){

        googleSearch.findByQuery(targetName);
        return "home";

    }
}
