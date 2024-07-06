package co.za.access.profiler.dataCollection.googleSearch.model;




import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@NoArgsConstructor
@Getter
@Setter
public class PageResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String query;

    @OneToMany(mappedBy = "pageResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Result> results;

    @Column(nullable = false)
    private int noOfPages;

    public PageResult(String query, Set<Result> results, int noOfPages) {
        this.query = query;
        this.results = results;
        this.noOfPages = noOfPages;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "results=" + results +
                ", noOfPages=" + noOfPages +
                '}';
    }
}
