package co.za.access.profiler.dataProcessing.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class SocialMediaAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private final String name;
    private final String link;
    private final String handle;
    private final long followers;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private final Person person;

    private final double accuracy;

    public SocialMediaAccount(){
        this(null,null,null,0,null,0);

    }
}
