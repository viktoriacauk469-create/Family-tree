package family_tree.model;

import family_tree.model.enums.BloodType;
import family_tree.model.enums.Gender;
import family_tree.model.enums.RhesusFactor;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type")
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "rhesus_factor")
    private RhesusFactor rhesusFactor;

    @Column(name = "is_main_profile")
    @Builder.Default
    private Boolean isMainProfile = false;

    // ✅ ДОДАНО: Поля для одного фото
    @Column(name = "photo_file_name")
    private String photoFileName;

    @Column(name = "photo_file_path")
    private String photoFilePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "relatives",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "relative_id")
    )
    @Builder.Default
    private Set<PersonalInformation> relatives = new HashSet<>();

    public boolean hasPhoto() {
        return photoFileName != null && photoFilePath != null;
    }

    public void setPhoto(String fileName, String filePath) {
        this.photoFileName = fileName;
        this.photoFilePath = filePath;
    }

    public void removePhoto() {
        this.photoFileName = null;
        this.photoFilePath = null;
    }

    public void addRelative(PersonalInformation relative) {
        if (relative == null || relative.equals(this)) return;
        this.relatives.add(relative);
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