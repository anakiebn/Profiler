package co.za.access.profiler.dataProcessing.model;

public enum SocialMedia {


    FACEBOOK("Facebook"),LINKEDIN("Linkedin"),X("X.xom"),INSTAGRAM("Instagram"),TIKTOK("Tiktok");
    private final String name;
    SocialMedia(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
