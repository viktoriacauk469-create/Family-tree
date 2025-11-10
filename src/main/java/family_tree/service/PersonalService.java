package family_tree.service;


import family_tree.model.PersonalInformation;

import java.util.Set;

public interface PersonalService {

    Set<PersonalInformation> getRelatives(Long personId);
    void addRelative(PersonalInformation person);
    void deleteRelative(PersonalInformation person);

}
