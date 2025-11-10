package family_tree.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "personal_information")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO ask team-lead what is the PersonalInformation stand for ?
    private String firstName;
    private String lastName;
    private Integer age;

    @ManyToMany(fetch = FetchType.LAZY) // just don`t know what to put LAZY or EAGER ?
    @JoinTable(
            name = "relatives",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "relative_id")
    )
    private Set<PersonalInformation> relatives = new HashSet<>();

    // helper methods to maintain both sides
    public void addRelative(PersonalInformation person) {
        if (person == null || this.equals(person)) return; // не додаємо себе
        if (relatives.add(person)) {
            person.getRelatives().add(this);
        }
    }

    public void removeRelative(PersonalInformation person) {
        if (person == null) return;
        if (relatives.remove(person)) {
            person.getRelatives().remove(this);
        }
    }
}
