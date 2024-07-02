package co.za.access.Profiler.dataCollection.controller;

import co.za.access.Profiler.dataCollection.model.PageResult;
import co.za.access.Profiler.dataCollection.service.PageResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiler/api/v1/googleSearch")
public class PageResultController {

    private final PageResultService pageResultService;

    public PageResultController(PageResultService pageResultService){

        this.pageResultService=pageResultService;
    }


    @GetMapping("/{query}")
    public ResponseEntity<PageResult> search(@PathVariable String query){

        PageResult pageResult=pageResultService.findByQuery(query.trim());
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }
}
