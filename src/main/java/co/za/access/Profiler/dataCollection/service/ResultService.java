package co.za.access.Profiler.dataCollection.service;

import co.za.access.Profiler.dataCollection.exception.ResultsNotFoundException;
import co.za.access.Profiler.dataCollection.model.Result;

import java.util.Set;

public interface ResultService {
    Set<Result> findAllByPageResultId(Long prID) throws ResultsNotFoundException;

    Set<Result> saveAll(Set<Result> results);

}
