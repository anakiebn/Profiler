package co.za.access.Profiler.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix="app")
public class AppVariable {

    private String chromeDriver;
    private String loginEmail;
    private String loginPassword;



}
