package services;

import model.db.person.Person;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import model.db.person.PersonRepository;

@Singleton
public class PersonService {
    private PersonRepository personRepository;

    @Inject
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
        initDB(); // h2 database is empty on creation so should be filled before use
    }

    private void initDB() {
        Random random = new Random(50);
        for (int i=0; i<35; i++) {
            personRepository.add( new Person().
                    setName("Rose" + random.nextInt()).
                    setAge(random.nextInt(190)) );
        }
    }

    public CompletionStage<Person> createPerson(Person person) {
        return personRepository.add(person);
    }


    public CompletionStage<Stream<Person>> readPersons() { return readPersons(0, Integer.MAX_VALUE); }

    public CompletionStage<Stream<Person>> readPersons(int start, int amount) { return personRepository.list(start, amount); }



    public CompletionStage<Person> deletePerson(Integer id) {
        return personRepository.remove(id);
    }

    public CompletionStage<Person> updatePerson(Person person) {
        return personRepository.update(person);
    }
}
