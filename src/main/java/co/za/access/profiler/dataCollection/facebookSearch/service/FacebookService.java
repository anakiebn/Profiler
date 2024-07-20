package co.za.access.profiler.dataCollection.facebookSearch.service;

import co.za.access.profiler.config.CookieData;

import java.util.List;

public interface FacebookService {


    String searchPerson(String name, List<CookieData> cookieDataList);
}
