package co.za.access.Profiler.dataCollection.service;

import co.za.access.Profiler.dataCollection.model.PageResult;

public interface PageResultService {

    PageResult findByQuery(String query);

}
