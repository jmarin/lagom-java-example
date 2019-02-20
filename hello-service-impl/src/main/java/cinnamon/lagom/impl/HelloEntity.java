package cinnamon.lagom.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This is an event sourced entity. It has a state, {@link HelloState}, which
 * stores what the greeting should be (eg, "Hello").
 * <p>
 * Event sourced entities are interacted with by sending them commands. This
 * entity supports two commands, ${@link cinnamon.lagom.impl.HelloCommand.UseGreetingMessage} command,
 * which is used to change the greeting, and a ${@link cinnamon.lagom.impl.HelloCommand.Hello} command,
 * which is a read only command which returns a greeting to the name specified by the command.
 * <p>
 * Commands get translated to events, and it's the events that get persisted by
 * the entity. Each event will have an event handler registered for it, and an
 * event handled simply applies an event to the current state. This will be done
 * when the event is first created, and it will also be done when the entity is
 * loaded from the database - each event will be replayed to recreate the state
 * of the entity.
 * <p>
 * This entity defines one event, {@link cinnamon.lagom.impl.HelloEvent.GreetingMessageChanged} event,
 * which is emitted when a ${@link cinnamon.lagom.impl.HelloCommand.UseGreetingMessage} command is received.
 */
public class HelloEntity extends PersistentEntity<HelloCommand, HelloEvent, HelloState> {

    /**
     * An Entity can define different behaviors for different states, but it will always start
     * with an initial behavior. This entity only has one behavior.
     */
    @Override
    public Behavior initialBehavior(Optional<HelloState> snapshotState) {
       /*
        * Behavior is define using a behavior builder. The behavior builder
        * starts with a state, if this entity supports snapshots (an
        * optimization that allows the state itself to be persisted to combine many
        * events into one), then the passed in snapshotState may have a value that
        * can be used.
        *
        * Otherwise, the default state is to use the Hello greeting.
        */

       BehaviorBuilder b = newBehaviorBuilder(
               snapshotState.orElse(new HelloState("Hello", LocalDateTime.now().toString()))
       );


       /*
        * Command handler (read only) for the Hello command.
        */
       b.setReadOnlyCommandHandler(HelloCommand.Hello.class,
               // Get the greeting from the current state, and prepend it to the name
               // that we're sending a greeting to, and reply with that message.
               (cmd, ctx) ->   ctx.reply(state().message + ", " + cmd.name + "!"));

       /*
        * Command handler for the UseGreetingMessage command.
        */
       b.setCommandHandler(HelloCommand.UseGreetingMessage.class, (cmd, ctx) ->
         ctx.thenPersist(new HelloEvent.GreetingMessageChanged(entityId(), cmd.message),
                 evt -> ctx.reply(Done.getInstance()))
       );

       /*
        * Event handler for the GreetingMessageChanged event
        */
       b.setEventHandler(HelloEvent.GreetingMessageChanged.class,
               //We simply update the current state to use the greeting message
               // from the event
               evt -> new HelloState(evt.message, LocalDateTime.now().toString()));

       /*
        * We've defined all our behavior, so build and return it.
        */
        return b.build();
    }
}
