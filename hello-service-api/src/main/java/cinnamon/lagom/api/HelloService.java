package cinnamon.lagom.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.*;
import static com.lightbend.lagom.javadsl.api.Service.*;

public interface HelloService extends Service {
    /**
     * Example: curl http://localhost:9000/api/hello/Alice
     */
    ServiceCall<NotUsed, String> hello(String id);

    /**
     * Example: curl http://localhost:9000/api/hello-proxy/Alice
     */
    ServiceCall<NotUsed, String> helloProxy(String id);

    @Override
    default Descriptor descriptor() {
        return named("hello").withCalls(
            pathCall("/api/hello/:id", this::hello),
            pathCall("/api/hello-proxy/:id", this::helloProxy)
        ).withAutoAcl(true);
    }
}
