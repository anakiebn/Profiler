package co.za.access.Profiler.googleSearch.service;

import co.za.access.Profiler.googleSearch.model.PageResult;

public interface PageResultService {

    PageResult getPageResult(String query,int noOfPages);

}
