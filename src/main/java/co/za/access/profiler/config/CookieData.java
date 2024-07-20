package co.za.access.profiler.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class CookieData {


    private String name;
    private String value;
    private String domain;
    private String path;
    private Date expiry;
    private String size;
    private boolean httpOnly;
    private boolean isSecure;
    private String sameSite;


}
