package family_tree.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "personal_information")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private Integer age;

    /**
     * Optional link to a User. Nullable: person may be NOT linked to a user (unregistered person).
     * Many personal records can reference the same User (One User -> Many PersonalInformation).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Self-referencing many-to-many for relatives.
     * Use helper methods to keep relationship symmetric.
     */
    @ManyToMany
    @JoinTable(
            name = "relatives",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "relative_id")
    )
    @Builder.Default
    private Set<PersonalInformation> relatives = new HashSet<>();

    public void addRelative(PersonalInformation relative) {
        if (relative == null || relative.equals(this)) return;
        this.relatives.add(relative);
        // ensure symmetric relation:
        if (!relative.getRelatives().contains(this)) {
            relative.getRelatives().add(this);
        }
    }

    public void removeRelative(PersonalInformation relative) {
        if (relative == null) return;
        this.relatives.remove(relative);
        relative.getRelatives().remove(this);
    }
}
