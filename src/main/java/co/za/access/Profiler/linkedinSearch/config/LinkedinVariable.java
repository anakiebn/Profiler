package co.za.access.Profiler.linkedinSearch.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "linkedin")
@Getter
@Setter
public class LinkedinVariable {

    private String emailField;
    private String passwordField;
    private String signIn;
    private String searchField;



//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String
//    private String



}
