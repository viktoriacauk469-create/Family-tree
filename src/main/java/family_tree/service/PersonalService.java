package family_tree.service;


import family_tree.model.PersonalInformation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PersonalService {

    /**
     * Create a personal record (may be unregistered person: user == null).
     */
    PersonalInformation createPersonal(PersonalInformation personal);

    /**
     * Create a personal record and link it to an existing User.
     * Throws ResourceNotFoundException if user not found.
     */
    PersonalInformation createPersonalForUser(Long userId, PersonalInformation personal);

    /**
     * Link an existing PersonalInformation to an existing User.
     */
    PersonalInformation linkPersonalToUser(Long personalId, Long userId);

    /**
     * Add relative relation between two PersonalInformation records.
     * Relation is made symmetric (both sides updated).
     */
    PersonalInformation addRelative(Long personId, Long relativeId);

    /**
     * Remove a relative relation between two PersonalInformation records (both sides).
     */
    PersonalInformation removeRelative(Long personId, Long relativeId);

    /**
     * Get relatives for a personal record.
     */
    List<PersonalInformation> getRelatives(Long personId);

    /**
     * Get all relatives for all personal records that belong to a given user.
     * Useful when a user has multiple PersonalInformation entries (optional).
     */
    List<PersonalInformation> getRelativesForUser(Long userId);

    /**
     * Find personal record by id.
     */
    Optional<PersonalInformation> findById(Long id);
}
