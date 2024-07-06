package co.za.access.profiler.dataCollection.googleSearch.service;


import co.za.access.profiler.dataCollection.googleSearch.exception.ResultsNotFoundException;
import co.za.access.profiler.dataCollection.googleSearch.model.Result;
import co.za.access.profiler.dataCollection.googleSearch.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ResultServiceImpl implements ResultService{


    private final ResultRepository resultRepository;

    public ResultServiceImpl(ResultRepository ResultRepository){
        this.resultRepository=ResultRepository;
    }

    @Override
    public Set<Result> findAllByPageResultId(Long prID) throws ResultsNotFoundException {
        return resultRepository.findAllByPageResultId(prID).orElseThrow(()->new ResultsNotFoundException("No Result on page id: "+prID));
    }

    @Override
    public Set<Result> saveAll(Set<Result> results) {
        return null;
    }
}
