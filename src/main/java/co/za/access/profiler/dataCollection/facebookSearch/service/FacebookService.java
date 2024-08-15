package co.za.access.profiler.dataCollection.facebookSearch.service;

import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.dataProcessing.model.Target;

import java.util.List;

public interface FacebookService {


    List<Target>  searchPerson(String name, List<CookieData> cookieDataList,int noOfPages);
}
