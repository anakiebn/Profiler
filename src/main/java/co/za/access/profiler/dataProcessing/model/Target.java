package co.za.access.profiler.dataProcessing.model;


import lombok.Data;

@Data
public class Target {

    private String profileName ;
    private String profileLink;
    private String profileImage;
    private About about;

    public Target(String profileName,String profileImage,String profileLink,About about){
        this.profileName=profileName;
        this.profileImage=profileImage;
        this.profileLink=profileLink;
        this.about=about;
    }
}
