package name.shokred.rsocketdemo;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.*;

import java.time.Duration;

@Controller
@RequiredArgsConstructor
public class Server {

    @MessageMapping("fire.and.forget.{param}")
    public Mono<Void> fireAndForget(@DestinationVariable String param, @Payload Mono<String> mono) {
        return mono
                .doOnNext(x -> System.out.println(param + x))
                .then();
    }

    @MessageMapping("request.{param}")
    public Mono<String> request(@DestinationVariable String param, @Payload Mono<String> mono) {
        return mono.map(x -> param + x);
    }

    @MessageMapping("stream.{param}")
    public Flux<String> stream(@DestinationVariable String param, @Payload Mono<String> mono) {
        return mono.flatMapMany(s ->
                Flux.interval(Duration.ofSeconds(1))
                        .map(aLong -> param + s + aLong)
                        .take(2)
        );
    }

    @MessageMapping("channel.{param}")
    public Flux<String> channel(@DestinationVariable String param, @Payload Flux<String> flux) {
        return flux
                .map(x -> param + x)
                .take(2);
    }
}
