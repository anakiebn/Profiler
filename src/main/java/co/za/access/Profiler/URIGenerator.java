package co.za.access.Profiler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public interface URIGenerator {


    default URI generateURI(String baseURI, Map<String,String> parameters) throws URISyntaxException, InvalidURIParameter {

        StringBuilder createdURI=new StringBuilder(baseURI+"?");
        int count=0;
       for(String parameter:parameters.keySet()){
           String value=parameters.get(parameter);
           if(parameter==null || parameter.isEmpty() || parameter.isBlank()){
               throw new InvalidURIParameter("Invalid URI parameter");
           }
           if(value==null || value.isBlank() || value.isEmpty()){
               throw new InvalidURIParameter("Invalid URI value");
           }
           count++;
           createdURI.append(parameter).append("=").append(value);
           if(count<parameters.size()){
               createdURI.append("&");
           }

       }
       return new URI(createdURI.toString());
    }
}
