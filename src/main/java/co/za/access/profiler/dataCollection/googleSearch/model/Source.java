package co.za.access.profiler.dataCollection.googleSearch.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor@Getter
@Setter
@ToString
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "src_img", columnDefinition = "TEXT")

    private String srcImg;
    private String srcLink;
    private String srcName;

    public Source(String srcImg, String srcLink, String srcName) {
        this.srcImg = srcImg;
        this.srcLink = srcLink;
        this.srcName = srcName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Source source)) return false;
        return Objects.equals(srcImg, source.srcImg) && Objects.equals(srcLink, source.srcLink) && Objects.equals(srcName, source.srcName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, srcImg, srcLink, srcName);
    }
}
