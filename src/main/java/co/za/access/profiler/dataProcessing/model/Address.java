package co.za.access.profiler.dataProcessing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private final String country;
    private final String province;

    private final String generalLocation;

    private final String town;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private final Person person;

    private final double accuracy;
    public Address(){
        this(null,null,null,null,null,0);
    }
}