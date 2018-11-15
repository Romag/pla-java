package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import model.db.Wrapper;
import model.db.person.Person;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import com.typesafe.config.Config;
import play.mvc.Result;
import services.PersonService;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static play.libs.Scala.asScala;


public class MyController extends Controller {
    private Config config;
    private PersonService dbService;
    private FormFactory formFactory;
    private HttpExecutionContext ec;

    private final int INITIAL_ELEMENTS = 2;
    private final int ELEMENTS_TO_LOAD = 5;


    @Inject
    public MyController(Config config, PersonService dbService, FormFactory formFactory, HttpExecutionContext ec) {
        this.config = config;
        this.dbService = dbService;
        this.formFactory = formFactory;
        this.ec = ec;

        /*List<Person> persons = Lists.newArrayList(new Person(0, "Kile", 12),
                new Person(1, "Rose", 16),
                new Person(2, "Marie", 32)); //change to dbservice persistence source in future*/
    }

    public Result configuration() {
        return ok( config.entrySet().parallelStream()
                    .map( (entry) ->
                        "Key: "+entry.getKey()
                        +" \t\nValue: "+entry.getValue()
                        +"\n\n"
                    ).collect(Collectors.joining()) );
    }


    public CompletionStage<Result> listPersons() {
        return dbService.readPersons().thenApplyAsync( personStream -> {
             Form<Person> form = formFactory.form(Person.class);
             List<Person> persons = personStream.collect(Collectors.toList());
             return ok(views.html.listPersons.render(persons, form));
        }, ec.current() );
    }


    public CompletionStage<Result> listPersonsDynamically() {
        /*List<Person> collect = null;
        try {
            collect = dbService.readPersons(0, INITIAL_ELEMENTS).toCompletableFuture().get().collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<Person> finalCollect = collect;
        return CompletableFuture.supplyAsync( ()->{return ok(views.html.listDynamically.render(finalCollect));}, ec.current());*/

        return dbService.readPersons(0, INITIAL_ELEMENTS).thenApplyAsync(
                personsStream -> ok(views.html.listDynamically.render(personsStream.collect(Collectors.toList()))),
                    ec.current() );
    }


    public Result personsJson(Long rowsInTable) {
        try (Stream<Person> personStream = dbService.readPersons(rowsInTable.intValue(), ELEMENTS_TO_LOAD).toCompletableFuture().get()) {
            List<Person> collect = personStream.collect(Collectors.toList());
            return ok( Json.toJson(new Wrapper(collect.size(), views.html.list.render(collect).toString())));
        } catch (ExecutionException | InterruptedException e) {
        }

        return internalServerError();
    }

    public Result createPesonRender() { return ok(views.html.addPerson.render(formFactory.form(Person.class))); }

    public CompletionStage<Result> createPerson() {
        Person person = formFactory.form(Person.class).bindFromRequest().get();

        dbService.createPerson(person);

        //return listPersons();
        //return redirect("/listPersons");
        return CompletableFuture.supplyAsync( ()->ok("Person created"), ec.current());
    }

    /*public Result deletePersonRender() { return ok(views.html.deletePerson.render()); }*/

    public CompletionStage<Result> deletePerson(Long toDeleteId) {
        /*DynamicForm df = formFactory.form().bindFromRequest();
        Integer id = Integer.valueOf(df.get("test"));*/

        dbService.deletePerson(toDeleteId.intValue());

        return CompletableFuture.supplyAsync( ()->redirect(routes.MyController.listPersonsDynamically()), ec.current() );
    }

    public Result updatePersonRender() { return ok(views.html.updatePerson.render(formFactory.form((Person.class)))); }

    public CompletionStage<Result> updatePerson() {
        Person person = formFactory.form(Person.class).bindFromRequest().get();

        dbService.updatePerson(person);

        return CompletableFuture.supplyAsync( ()->ok("Person updated"), ec.current());
    }

}
