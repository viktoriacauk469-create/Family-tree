package family_tree.service.implementation;

import family_tree.exception.PersonNotFoundException;
import family_tree.model.PersonalInformation;
import family_tree.repository.PersonalInformationRepository;
import family_tree.service.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonalServiceImpl implements PersonalService {

    private final PersonalInformationRepository personRepository;

    @Override
    public Set<PersonalInformation> getRelatives(Long personId) {
        PersonalInformation person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Person not found: " + personId));
        return person.getRelatives();
    }

    @Override
    public void addRelative(PersonalInformation personToAdd) {

    }

    @Override
    public void deleteRelative(PersonalInformation person) {

    }

    @Transactional
    public void addRelative(Long personId, Long relativeId) {
        PersonalInformation person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Person not found: " + personId));
        PersonalInformation relative = personRepository.findById(relativeId)
                .orElseThrow(() -> new PersonNotFoundException("Relative not found: " + relativeId));

        person.addRelative(relative);

        personRepository.save(person); // update both objects
        personRepository.save(relative);
    }

    @Transactional
    public void deleteRelative(Long personId, Long relativeId) {
        PersonalInformation person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Person not found: " + personId));
        PersonalInformation relative = personRepository.findById(relativeId)
                .orElseThrow(() -> new PersonNotFoundException("Relative not found: " + relativeId));

        person.removeRelative(relative);

        personRepository.save(person);
        personRepository.save(relative);
    }
}
