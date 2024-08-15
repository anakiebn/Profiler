package co.za.access.profiler.dataCollection.linkedinSearch.service;

import co.za.access.profiler.config.CookieData;

import java.util.List;

public interface LinkedinService {

    String linkedinSearch(String targetName, List<CookieData> cookieDataList);
}
