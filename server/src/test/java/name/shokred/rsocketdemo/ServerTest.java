package name.shokred.rsocketdemo;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.*;
import reactor.test.StepVerifier;

import javax.annotation.PostConstruct;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ServerTest {

    @Autowired
    private RSocketStrategies rSocketStrategies;

    private RSocketRequester rSocketRequester;

    @PostConstruct
    void init() {
        rSocketRequester = rSocketRequester(rSocketStrategies);
    }

    @Test
    @SneakyThrows
    void fireAndForget() {
        Mono<Void> voidMono = rSocketRequester
                .route("fire.and.forget.{param}", "str")
                .data(Mono.just("hello"))
                .send();

        StepVerifier.create(voidMono)
                .verifyComplete();
    }

    @Test
    void request() {
        Mono<String> stringMono = rSocketRequester
                .route("request.{param}", "str")
                .data(Mono.just("hello"))
                .retrieveMono(String.class);

        StepVerifier.create(stringMono)
                .expectNext("strhello")
                .verifyComplete();
    }

    @Test
    void stream() {
        Flux<String> stringFlux = rSocketRequester
                .route("stream.{param}", "str")
                .data(Mono.just("hello"))
                .retrieveFlux(String.class);

        StepVerifier.create(stringFlux)
                .expectNext("strhello0")
                .expectNext("strhello1")
                .verifyComplete();
    }

    @Test
    void channel() {
        Flux<String> stringFlux = rSocketRequester
                .route("channel.{param}", "str")
                .data(Flux.just("hello", "hi"))
                .retrieveFlux(String.class);

        StepVerifier.create(stringFlux)
                .expectNext("strhello")
                .expectNext("strhi")
                .verifyComplete();
    }

    public RSocketRequester rSocketRequester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.builder()
                .rsocketStrategies(rSocketStrategies)
                .connectTcp("localhost", 7000)
                .block();
    }
}