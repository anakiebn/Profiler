package co.za.access.Profiler.facebookSearch.controller;


import co.za.access.Profiler.facebookSearch.service.FacebookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiler/api/facebookSearch")
public class FacebookController {


    private final FacebookService facebookService;

    public FacebookController(FacebookService facebookService){
        this.facebookService=facebookService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> searchPerson(@PathVariable String name){
        return new ResponseEntity<>(facebookService.searchPerson(name), HttpStatus.OK);
    }


}
