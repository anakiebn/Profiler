package co.za.access.profiler.dataCollection.googleSearch.controller;
import co.za.access.profiler.dataCollection.googleSearch.model.PageResult;
import co.za.access.profiler.dataCollection.googleSearch.service.GoogleSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/profiler/api/v1/googleSearch")
public class GoogleSearchController {

    private final GoogleSearch googleSearch;

    public GoogleSearchController(GoogleSearch googleSearch){

        this.googleSearch = googleSearch;
    }


    @GetMapping("/{query}")
    public ResponseEntity<PageResult> search(@PathVariable String query){
        PageResult pageResult= googleSearch.findByQuery(query.trim());
        log.info("Search complete!");
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
    }
}
