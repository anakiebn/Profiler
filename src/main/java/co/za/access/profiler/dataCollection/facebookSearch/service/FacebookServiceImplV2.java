package co.za.access.profiler.dataCollection.facebookSearch.service;

import co.za.access.profiler.config.CookieData;
import co.za.access.profiler.dataProcessing.model.Target;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Scanner;

public class FacebookServiceImplV2 implements FacebookService{


    @Override
    public List<Target> searchPerson(String name, List<CookieData> cookieDataList, int noOfPages) {
        return null;
    }
}
