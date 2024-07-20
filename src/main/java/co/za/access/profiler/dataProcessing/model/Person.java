package co.za.access.profiler.dataProcessing.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private  Long id;

    @Column(nullable = false)
    private final String name;
    private final String surname;

    @ElementCollection
    @CollectionTable(name = "person_potential_name", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "potential_name")
    private final List<String> potentialNames;

    @ElementCollection
    @CollectionTable(name = "person_potential_email", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "potential_email")
    private final Set<String> potentialEmail;

    @ElementCollection
    @CollectionTable(name = "person_potential_phone_no", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "potential_phone_no")
    private final Set<String> potentialPhoneNo;

    private int age;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private final Set<SocialMediaAccount> socialMediaAccounts;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private final Set<Address> potentialAddresses;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private final Set<RelatedPerson> relatedPeople;

    private final boolean employed;

    private final double accuracy;

    @OneToMany(cascade = CascadeType.ALL)
    private final List<Career> careers;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "surname = " + surname + ", " +
                "potentialNames = " + potentialNames + ", " +
                "potentialEmail = " + potentialEmail + ", " +
                "potentialPhoneNo = " + potentialPhoneNo + ", " +
                "age = " + age + ", " +
                "employed = " + employed + ", " +
                "accuracy = " + accuracy + ")";
    }

}
