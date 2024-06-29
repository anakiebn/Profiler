package co.za.access.Profiler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "facebook")
public class FacebookVariable {
    private String searchBar;
    private String loginEmail; //
    private String emailField;
    private String passwordField;
    private String loginBtn;
    private String loginPassword; //
    private String peopleDivSection; //
    private String seeAllBtn;
    private String filterPeopleBtn;
    private String filterByCityInput;
    private String filterByEducationInput;
    private String filterByFriendInput;
    private String declineCookieBtn;
    private String loginWindow;
    private String cookieWindow;

}

