package co.za.access.Profiler.dataCollection.service;

import co.za.access.Profiler.dataCollection.exception.QueryNotFoundException;
import co.za.access.Profiler.dataCollection.model.Query;
import co.za.access.Profiler.dataCollection.repository.QueryRepository;
import org.springframework.stereotype.Service;


@Service
public class QueryServiceImpl implements QueryService{


    private final QueryRepository queryRepository;


    public QueryServiceImpl(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public Query findByName(String queryName) throws QueryNotFoundException {
       return queryRepository.findByName(queryName).orElseThrow(()->new QueryNotFoundException("Query of: "+queryName+" not found"));
    }
}
