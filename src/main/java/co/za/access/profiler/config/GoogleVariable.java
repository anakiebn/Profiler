package co.za.access.profiler.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google")
@Getter
@Setter
public class GoogleVariable {

    private String searchField;
    private String rejectCookieBtn;
    private String moreResultsBtn;
    private String resultDescription;
    private String results;
    private String resultTitle;
    private String resultSrcLink;
    private String resultSrcName;
    private String resultSrcImg;
    private String resultImg;
    private String lastLine;
    private String nextBtn;
    private String nextPage;

}
