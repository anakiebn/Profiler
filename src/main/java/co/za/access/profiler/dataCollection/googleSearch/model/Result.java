package co.za.access.profiler.dataCollection.googleSearch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "src_id")
    private Source src;

    @Column(nullable = false)
    private String title;
    private String description;

    @Lob
    @Column(name = "src_img", columnDefinition = "TEXT")
    private String resultImg;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "page_result_id")
    @JsonIgnore
    private PageResult pageResult;

    public Result(Source src, String title, String description, String resultImg, PageResult pageResult) {
        this.src = src;
        this.title = title;
        this.description = description;
        this.resultImg = resultImg;
        this.pageResult = pageResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result result)) return false;
        return Objects.equals(src, result.src) && Objects.equals(title, result.title) && Objects.equals(description, result.description) ;
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
