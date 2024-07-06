package co.za.access.profiler.dataCollection.googleSearch.service;


import co.za.access.profiler.dataCollection.googleSearch.exception.ResultsNotFoundException;
import co.za.access.profiler.dataCollection.googleSearch.model.Result;

import java.util.Set;

public interface ResultService {
    Set<Result> findAllByPageResultId(Long prID) throws ResultsNotFoundException;

    Set<Result> saveAll(Set<Result> results);

}
