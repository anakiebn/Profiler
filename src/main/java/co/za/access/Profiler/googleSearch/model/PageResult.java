package co.za.access.Profiler.googleSearch.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;


public class PageResult {


    private Set<Result> results;
    private int noOfPages;

    public PageResult(Set<Result> results, int noOfPages) {
        this.results = results;
        this.noOfPages = noOfPages;
    }

    public Set<Result> getResults() {
        return results;
    }

    public void setResults(Set<Result> results) {
        this.results = results;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "results=" + results +
                ", noOfPages=" + noOfPages +
                '}';
    }
}
