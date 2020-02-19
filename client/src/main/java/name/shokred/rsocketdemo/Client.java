package name.shokred.rsocketdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
public class Client implements ApplicationListener<ApplicationReadyEvent> {

    private final RSocketRequester rSocketRequester;

    @Autowired
    public Client(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        rSocketRequester.route("channel.{param}", "hi")
                .data(Flux.interval(Duration.ofSeconds(1))
                        .doOnNext(x -> System.out.println("Sent: " + x))
                        .map(Object::toString), String.class)
                .retrieveFlux(String.class)
                .doOnNext(x -> System.out.println("Received from server: " + x))
                .blockLast();
    }
}
