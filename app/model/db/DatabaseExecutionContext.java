package model.db;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

public class DatabaseExecutionContext extends CustomExecutionContext {

    @Inject DatabaseExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "play.dispatcher");
    }
}
