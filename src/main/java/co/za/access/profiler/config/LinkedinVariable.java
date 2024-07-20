package co.za.access.profiler.config;

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
    private String validLink; // link to a persons profile
    private String passwordField;
    private String signIn;
    private String searchField;
    private String seeAllPeopleResultBtn; // Button used to view all people found in the result section, But will view all people for that page
    private String result; // These are all the person that match your targets name
    private String targetName; // This is the target's profile name as a header shown when you view their profile
    private String personLink; // The link that I'll click to view a persons profile
    private String nextBtn;
    private String bottom;
    private String doneExtracting;
    private String profilePicture;
    private String experience;
    private String sameCompanyExperience;
    private String role;
    private String duration;
    private String location;


}
