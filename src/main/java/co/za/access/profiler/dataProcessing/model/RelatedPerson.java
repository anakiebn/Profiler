package co.za.access.Profiler.dataProcessing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@ToString
public class RelatedPerson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private final String name;
    private final String surname;

    @ElementCollection
    @CollectionTable(name = "person_potential_name", joinColumns = @JoinColumn(name = "related_person_id"))
    @Column(name = "potential_name")
    private final List<String> potentialNames;

    @ElementCollection
    @CollectionTable(name = "person_potential_email", joinColumns = @JoinColumn(name = "related_person_id"))
    @Column(name = "potential_email")
    private final Set<String> potentialEmail;

    @ElementCollection
    @CollectionTable(name = "person_potential_phone_no", joinColumns = @JoinColumn(name = "related_person_id"))
    @Column(name = "potential_phone_no")
    private final Set<String> potentialPhoneNo;

    private final int age;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private final Person person;

    private final double accuracy;

    public RelatedPerson(){
        this(null,null,null,null,null,0,null,0);

    }

}
