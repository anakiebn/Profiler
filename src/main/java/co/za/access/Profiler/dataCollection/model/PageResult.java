package co.za.access.Profiler.dataCollection.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "query_id",nullable = false,unique = true)
    private Query query;

    @OneToMany(mappedBy = "pageResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Result> results;

    @Column(nullable = false)
    private int noOfPages;

    @Override
    public String toString() {
        return "PageResult{" +
                "results=" + results +
                ", noOfPages=" + noOfPages +
                '}';
    }
}
