package co.za.access.profiler.dataProcessing.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private final String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private final CareerCategory category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private final Person person;
    private final double accuracy;

    public Career(){
        this(null,null,null,0);

    }
}
