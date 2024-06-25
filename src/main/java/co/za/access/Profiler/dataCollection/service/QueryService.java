package co.za.access.Profiler.dataCollection.service;

import co.za.access.Profiler.dataCollection.exception.QueryNotFoundException;
import co.za.access.Profiler.dataCollection.model.Query;

public interface QueryService {

    Query findByName(String queryName) throws QueryNotFoundException;
}
