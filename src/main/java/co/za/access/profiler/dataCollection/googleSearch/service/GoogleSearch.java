package co.za.access.profiler.dataCollection.googleSearch.service;


import co.za.access.profiler.dataCollection.googleSearch.model.PageResult;

public interface GoogleSearch {

    PageResult findByQuery(String query);

}
