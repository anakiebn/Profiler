package co.za.access.profiler.dataCollection.facebookSearch.service;

import co.za.access.profiler.config.CookieData;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

public class FacebookServiceImplV2 implements FacebookService{



    @Override
    public String searchPerson(String name, List<CookieData> cookieDataList) {


        return null;
    }

    public void search(){

        HttpClient client=HttpClient
                .newHttpClient();


        HttpRequest request=HttpRequest
                .newBuilder()
                .GET()

                .build();

    }
}
