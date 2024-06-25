package co.za.access.Profiler.dataCollection.service;

import co.za.access.Profiler.dataCollection.exception.QueryNotFoundException;
import co.za.access.Profiler.dataCollection.exception.ResultsNotFoundException;
import co.za.access.Profiler.dataCollection.model.PageResult;
import co.za.access.Profiler.dataCollection.model.Query;
import co.za.access.Profiler.dataCollection.model.Result;
import co.za.access.Profiler.dataCollection.repository.PageResultRepository;
import co.za.access.Profiler.dataCollection.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        return new HashSet<>(resultRepository.saveAll(results));
    }
}
