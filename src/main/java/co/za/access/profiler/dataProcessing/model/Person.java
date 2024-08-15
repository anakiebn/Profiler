package co.za.access.profiler.dataProcessing.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private  Long id;

    @Column(nullable = false)
    private String name;
    private String surname;

    @ElementCollection
    @CollectionTable(name = "person_potential_name", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "potential_name")
    private List<String> potentialNames;

    @ElementCollection
    @CollectionTable(name = "person_potential_email", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "potential_email")
    private Set<String> potentialEmail;

    @ElementCollection
    @CollectionTable(name = "person_potential_phone_no", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "potential_phone_no")
    private Set<String> potentialPhoneNo;

    private int age;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private Set<SocialMediaAccount> socialMediaAccounts;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private Set<Address> potentialAddresses;

    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL)
    private Set<RelatedPerson> relatedPeople;

    private boolean employed;

    private double accuracy;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Career> careers;

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
