package co.za.access.Profiler.googleSearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

public class Result {


    private Source src;
    private String title;
    private String description;
    private String resultImg;

    public Result(Source src, String title, String description, String resultImg) {
        this.src = src;
        this.title = title;
        this.description = description;
        this.resultImg = resultImg;
    }

    public Source getSrc() {
        return src;
    }

    public void setSrc(Source src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResultImg() {
        return resultImg;
    }

    public void setResultImg(String resultImg) {
        this.resultImg = resultImg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result result)) return false;
        return this.title.equals(((Result) o).title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, title, description, resultImg);
    }

    @Override
    public String toString() {
        return "Result{" +
                "src=" + src +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", resultImg='" + resultImg + '\'' +
                '}';
    }
}
