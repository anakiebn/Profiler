package co.za.access.profiler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "facebook")
public class FacebookVariable {
    private String searchField;
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
    private String profile;
    private String profileLink;
    private String profileImageLink;
    private String body;
    private String images;
    private String profileName;
    private String about;
    private String aboutSection;
    private String work;
    private String college;

    private String workAndCollege;
    private String workAndEducationParam;
    private String placeLivedParam;
    private String contactParam;
    private String relationshipParam;
    private String loggedIn;

    private String workAndCollegeName;
    private String workStartAndEndDate;
    private String highSchool;
    private String current;
    private String currentCity;
    private String hometown;
    private String placeName;
    private String contactInfo;
    private String relationship;
    private String family;
    private String connectionMemberName;
    private String relationshipType;

}