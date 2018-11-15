package model.db.person;

import com.google.inject.Provides;
import model.db.DatabaseExecutionContext;
import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

@Singleton
public class PersonRepositoryJpaImpl implements PersonRepository{
    private JPAApi jpaApi;
    private DatabaseExecutionContext databaseExecutionContext;


    @Inject
    public PersonRepositoryJpaImpl(JPAApi jpaApi, DatabaseExecutionContext databaseExecutionContext) {
        this.jpaApi = jpaApi;
        this.databaseExecutionContext = databaseExecutionContext;
    }

    @Override
    public CompletionStage<Person> add(Person person) {
        return CompletableFuture.supplyAsync(()->wrap(em->insert(em, person)), databaseExecutionContext);
    }

    @Override
    public CompletionStage<Stream<Person>> list(int start, int amount) {
        return CompletableFuture.supplyAsync(()->wrap(em->list(em, start, amount)), databaseExecutionContext);
    }

    @Override
    public CompletionStage<Person> remove(Integer id) {
        return CompletableFuture.supplyAsync( ()->wrap(em->delete(em, id)), databaseExecutionContext);
    }

    @Override
    public CompletionStage<Person> update(Person person) {
        return CompletableFuture.supplyAsync( ()->wrap(em->update(em, person)), databaseExecutionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Person insert(EntityManager em, Person p) {
        em.persist(p);
        return p;
    }

    private Stream<Person> list(EntityManager em, int firstElement, int elements_to_add) {
        Stream<Person> personsStream =
                em.createQuery("select p from Person p", Person.class).
                        setFirstResult(firstElement).
                        setMaxResults(elements_to_add).
                        getResultList().
                        stream();

        return  personsStream;
    }

    private Person update(EntityManager em, Person p) {
        return em.merge(p);
    }

    private Person delete(EntityManager em, Integer id) {
        Person person = em.find(Person.class, id);
        em.remove(person);
        return person;
    }

}
