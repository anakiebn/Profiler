package co.za.access.Profiler.googleSearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;



public class Source {

    private String srcImg;
    private String srcLink;
    private String srcName;

    public Source(String srcImg, String srcLink, String srcName) {
        this.srcImg = srcImg;
        this.srcLink = srcLink;
        this.srcName = srcName;
    }

    public String getSrcImg() {
        return srcImg;
    }

    public void setSrcImg(String srcImg) {
        this.srcImg = srcImg;
    }

    public String getSrcLink() {
        return srcLink;
    }

    @Override
    public String toString() {
        return "Source{" +
                "srcImg='" + srcImg + '\'' +
                ", srcLink='" + srcLink + '\'' +
                ", srcName='" + srcName + '\'' +
                '}';
    }

    public void setSrcLink(String srcLink) {
        this.srcLink = srcLink;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }
}
