package co.za.access.Profiler.googleSearch.controller;

import co.za.access.Profiler.googleSearch.model.PageResult;
import co.za.access.Profiler.googleSearch.service.PageResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiler/api/pageResults")
public class PageResultsController {

    private final PageResultService pageResultService;

    public PageResultsController(PageResultService pageResultService){

        this.pageResultService=pageResultService;
    }


    @GetMapping("/{query}/{noOfPages}")
    public ResponseEntity<PageResult> search(@PathVariable String query,@PathVariable String noOfPages){
        PageResult pageResult=pageResultService.getPageResult(query,Integer.parseInt(noOfPages));

        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }
}
