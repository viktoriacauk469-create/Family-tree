package family_tree.service.implementation;

import family_tree.exception.PersonNotFoundException;
import family_tree.model.PersonalInformation;
import family_tree.model.User;
import family_tree.repository.PersonalInformationRepository;
import family_tree.repository.UserRepository;
import family_tree.service.PersonalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService {

    private final PersonalInformationRepository personRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PersonalInformation createPersonal(PersonalInformation personal) {
        personal.setId(null);
        if (personal.getRelatives() == null) {
            personal.setRelatives(new HashSet<>());
        }
        return personRepository.save(personal);
    }

    @Override
    @Transactional
    public PersonalInformation createPersonalForUser(Long userId, PersonalInformation personal) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PersonNotFoundException("User not found: " + userId));

        personal.setUser(user);

        if (personal.getRelatives() == null) personal.setRelatives(new HashSet<>());
        PersonalInformation saved = personRepository.save(personal);

        // keep user's collection consistent (optional)
        user.getPersonalInformation().add(saved);
        return saved;
    }

    @Override
    @Transactional
    public PersonalInformation linkPersonalToUser(Long personalId, Long userId) {
        PersonalInformation personal = personRepository.findById(personalId)
                .orElseThrow(() -> new PersonNotFoundException("Personal not found: " + personalId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new PersonNotFoundException("User not found: " + userId));

        personal.setUser(user);
        return personRepository.save(personal);
    }

    @Override
    @Transactional
    public PersonalInformation addRelative(Long personId, Long relativeId) {
        if (Objects.equals(personId, relativeId)) {
            throw new IllegalArgumentException("Cannot add person as a relative to themself");
        }

        PersonalInformation person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Personal not found: " + personId));
        PersonalInformation relative = personRepository.findById(relativeId)
                .orElseThrow(() -> new PersonNotFoundException("Personal not found: " + relativeId));

        person.addRelative(relative);

        personRepository.save(person);
        personRepository.save(relative);

        return person;
    }

    @Override
    @Transactional
    public PersonalInformation removeRelative(Long personId, Long relativeId) {
        PersonalInformation person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Personal not found: " + personId));
        PersonalInformation relative = personRepository.findById(relativeId)
                .orElseThrow(() -> new PersonNotFoundException("Personal not found: " + relativeId));

        person.removeRelative(relative);

        personRepository.save(person);
        personRepository.save(relative);

        return person;
    }

    @Override
    public List<PersonalInformation> getRelatives(Long personId) {
        PersonalInformation person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Personal not found: " + personId));
        return new ArrayList<>(person.getRelatives());
    }

    @Override
    public List<PersonalInformation> getRelativesForUser(Long userId) {

        return personRepository.findByUserId(userId);
    }

    @Override
    public Optional<PersonalInformation> findById(Long id) {
        return personRepository.findById(id);
    }
}
