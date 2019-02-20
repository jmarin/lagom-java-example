package cinnamon.lagom.api;

import akka.NotUsed;
import akka.Done;
import com.lightbend.lagom.javadsl.api.*;
import static com.lightbend.lagom.javadsl.api.Service.*;

public interface HelloService extends Service {
    /**
     * Example: curl http://localhost:9000/api/hello/Alice
     */
    ServiceCall<NotUsed, String> hello(String id);

    /**
     * Example: curl -H "Contenty-Type: application/json" -X POST -d '{"message": "Hi"}'  http://localhost:9000/api/hello/Alice
     */
    ServiceCall<GreetingMessage, Done> useGreeting(String id);

    @Override
    default Descriptor descriptor() {
        return named("hello").withCalls(
            pathCall("/api/hello/:id", this::hello),
            pathCall("/api/hello/:id", this::useGreeting)
        ).withAutoAcl(true);
    }
}
