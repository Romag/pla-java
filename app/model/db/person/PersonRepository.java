package model.db.person;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(PersonRepositoryJpaImpl.class)
public interface PersonRepository {
    CompletionStage<Person> add(Person person);

    CompletionStage<Stream<Person>> list(int start, int amount);

    CompletionStage<Person> remove(Integer id);

    CompletionStage<Person> update(Person person);
}
